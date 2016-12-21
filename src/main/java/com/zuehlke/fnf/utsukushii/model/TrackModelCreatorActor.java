package com.zuehlke.fnf.utsukushii.model;

import akka.actor.Props;
import com.zuehlke.carrera.relayapi.messages.VelocityMessage;
import com.zuehlke.fnf.actorbus.ActorBusActor;
import com.zuehlke.fnf.actorbus.Subscriptions;
import com.zuehlke.fnf.actorbus.logging.MessageCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrackModelCreatorActor extends ActorBusActor {

    enum Status {
        WAITING_FOR_STRAIGHT,
        EXPECTING_CURVE,
        NEXT_STRAIGHT_TERMINATES, READY
    }

    enum OpsState {
        WARMING_UP, // just wait for the first track sections to pass by
        RECOGNITION, // try find a recurring pattern
        MAINTENANCE, // add more samples
        READY, // ignore velocities and track sections
    }

    @AllArgsConstructor
    @Getter
    private class LocatedCurve {
        private final int index;
        private final TrackSectionType type;
    }

    public static Props props(TrackModelActorProperties properties) {
        return Props.create(TrackModelCreatorActor.class, () -> new TrackModelCreatorActor(properties));
    }

    public static Subscriptions subscriptions = Subscriptions
            .forClass(TrackSectionSample.class)
            .andForClass(VelocityMessage.class);

    private TrackModel trackModel;
    private Status status = Status.WAITING_FOR_STRAIGHT;
    private TrackModelActorProperties properties;
    private List<TrackSectionSample> history = new ArrayList<>();
    private List<LocatedCurve> majorCurves = new ArrayList<>();

    private List<VelocityMessage> velocities = new ArrayList<>();

    private OpsState opsState = OpsState.WARMING_UP;
    private int warmupCounter;

    private TrackModelCreatorActor(TrackModelActorProperties properties) {
        super("TrackModelActor");
        this.properties = properties;
    }

    @Override
    protected void onReceive2(Object message) throws Exception {

        switch (opsState) {
            case WARMING_UP:
                if (message instanceof VelocityMessage) {
                    collectVelocity((VelocityMessage) message);
                    return;
                }

                int sectionsToWait = properties.getAwaitSectionsBeforeRecognition();
                if (warmupCounter++ > sectionsToWait) {
                    info (MessageCode.USAGE, "Starting track recognition after " + sectionsToWait + " sections.",
                            getSender().path().name());
                    opsState = OpsState.RECOGNITION;
                }
                break;
            case RECOGNITION:
                if (message instanceof TrackSectionSample) {
                    collectSample((TrackSectionSample) message);
                } else if (message instanceof VelocityMessage) {
                    collectVelocity((VelocityMessage) message);
                } else {
                    unhandled(message);
                }
                break;

            default: {
                warn(MessageCode.ILLEGAL_MESSAGE, "Post-Trackmodel behaviour Not implemented yet.", getSender().path().name());
                // ignore for the time being
            }
        }
    }

    private void collectVelocity(VelocityMessage message) {
        this.velocities.add(message);
    }

    private Optional<Integer> findSectionAfter(long t) {
        for (int i = 0; i < history.size(); i++) {
            if (history.get(i).getSensorEvents().get(0).getT() > t) {
                return Optional.of(i - 1);
            }
        }
        return Optional.empty();
    }


    private void collectSample(TrackSectionSample sample) {

        addToHistory(sample);

        if (sample.getType() == TrackSectionType.STRAIGHT && sample.getDurationInMs() > properties.getMinStraightLength()) {
            if (status == Status.WAITING_FOR_STRAIGHT) {
                status = Status.EXPECTING_CURVE;

            } else if ( status == Status.NEXT_STRAIGHT_TERMINATES ) { // found the track model!!
                List<TrackSectionExperience> experiences = createExperienceList(majorCurves, history);
                for ( TrackSectionExperience experience : experiences ) {
                    for (TrackSectionSample s : experience.getSamples()) {
                        findAndSetEntryAndExitFor(s, velocities);
                    }
                }
                info(MessageCode.USAGE, "Found track model", getSender().path().name());
                trackModel = new TrackModel( experiences );
                opsState = OpsState.READY;
            }
            return;
        }

        if (status == Status.EXPECTING_CURVE) {
            if (sample.isCurve()) {
                if (learnFromSample(history.size() - 1, sample)) {
                    status = Status.NEXT_STRAIGHT_TERMINATES;
                } else {
                    status = Status.WAITING_FOR_STRAIGHT;
                }
            }
        }
    }

    private void addToHistory ( TrackSectionSample sample ) {
        TrackSectionSample prev = null;
        if (history.size() > 0 ) {
            prev = history.get(history.size()-1);
            prev.setNext ( sample );
        }
        sample.setPrevious ( prev );
        history.add(sample);
    }

    /**
     * @return true if a recurring pattern has been found
     */
    private boolean learnFromSample(int index, TrackSectionSample sample) {
        majorCurves.add(new LocatedCurve(index, sample.getType()));
        int minCurves = properties.getMinNumberOfCurves();
        if (majorCurves.size() >= 2 * minCurves) {
            for (int allegedSize = minCurves; allegedSize <= majorCurves.size() / 2; allegedSize++) {
                boolean found = tryFindRecurringPattern(allegedSize);
                if (found) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * After having found a recurring pattern,
     * we know that majorCurves contains two subsequent sequences each representing a complete lap
     */
    private static List<TrackSectionExperience> createExperienceList(List<LocatedCurve> majorCurves, List<TrackSectionSample> history ) {
        int M = majorCurves.size() / 2;
        List<TrackSectionExperience> experiences = new ArrayList<>();


        for (int m = 0; m < M; m++) { // collect major curves per single lap and their subsequent straights.

            List<TrackSectionSample> samplesLap1 = new ArrayList<>();
            // first lap: find all the possibly multiple samples for a section (complex curves have multiples)
            LocatedCurve curve1 = majorCurves.get(m);

            for ( int h = curve1.getIndex(); h < majorCurves.get(m+1).getIndex() - 1; h++ ) {
                samplesLap1.add(history.get(h)); // collect all but the trailing straight
            }
            // concatenate all subsections of this curve, if it is a combination of curves
            TrackSectionSample concatenatedCurve = TrackSectionSample.concatenate(samplesLap1);
            // create an experience record from the result
            TrackSectionExperience curveExperience = new TrackSectionExperience(concatenatedCurve);
            // and add it to the list of section experiences
            experiences.add(curveExperience);

            // then add the subsequent straight from the history
            TrackSectionExperience straightExperience = new TrackSectionExperience(
                    history.get(majorCurves.get(m+1).getIndex() - 1));
            experiences.add(straightExperience);



            // subsequent lap: merge new experience to corresponding previous experience
            List<TrackSectionSample> samplesLap2 = new ArrayList<>();
            LocatedCurve curve2 = majorCurves.get(m+M);
            int nextIndex;
            if ( m < M - 1 ) { // start of the next major curve
                nextIndex = majorCurves.get(M + m + 1).getIndex() - 1;

            } else { // if m = M-1, stop at the end of the history
                nextIndex = history.size() - 1;
            }
            for ( int h = curve2.getIndex(); h < history.size()-1 && h < nextIndex; h++ ) {
                samplesLap2.add(history.get(h));
            }
            curveExperience.merge(TrackSectionSample.concatenate(samplesLap2));
            straightExperience.merge(history.get(nextIndex));

        }

        List<TrackSectionSample> experiencedSamples = new ArrayList<>();
        for ( TrackSectionExperience exp : experiences ) {
            experiencedSamples.addAll(exp.getSamples());
        }
        experiencedSamples.sort((l,r)->l.getSensorEvents().get(0).getT() - r.getSensorEvents().get(0).getT());
        TrackSectionSample prev = null;
        for ( TrackSectionSample s : experiencedSamples ) {
            s.setPrevious(prev);
            if ( prev != null ) {
                prev.setNext(s);
            }
            prev = s;
        }

        return experiences;
    }

    /**
     * Associate matching velocities with entry and exit. Use last known velocity as default if either isn't found.
     * Velocities are considered good if and only if entry velocity has been measured between the middle of the previous
     * section and the middle of the current section. Exit velocity likewise. If
     *
     *                   good entry velo for the sample
     *                        |               good exit velo
     *                        |                    |
     *                        V                    V
     * |------------M------------|_____M______|-------M--------|
     *          prev. straight     left sample    subseq straight
     */
     static void findAndSetEntryAndExitFor ( TrackSectionSample sample, List<VelocityMessage> allVelos) {

        boolean entryIsGood = false;
        boolean exitIsGood = false;

        for ( VelocityMessage m: allVelos ) {

            // default, if no matching velocities are found
            long t_v = m.getT();

            // break if velocity is to late for both entry and exit
            if ( sample.getNext() != null ) {
                if ( t_v > sample.getNext().middleAtTime()) break;
            }

            // Check if velocity is too late to be considered for velocity at entry
            if ( t_v < sample.middleAtTime()) {
                sample.setVelocityAtEntry(m);
            }

            sample.setVelocityAtExit(m);
            long cm = sample.middleAtTime();

            TrackSectionSample prev = sample.getPrevious();
            if ( prev != null && !entryIsGood) {
                long pm = prev.middleAtTime();
                if (t_v > pm && t_v < cm) {
                    entryIsGood = true;
                }
            }

            TrackSectionSample next = sample.getNext();
            if ( next != null ) {
                long nm = next.middleAtTime();
                if (t_v > cm && t_v < nm) {
                    exitIsGood = true;
                }
            }
            sample.setVelocitiesAreGood(entryIsGood && exitIsGood );
            if ( exitIsGood ) {
                break;
            }

        }
    }


    private boolean tryFindRecurringPattern(int allegedSize) {

        int index1 = majorCurves.size() - allegedSize;
        int index2 = majorCurves.size() - 2 * allegedSize;
        for (int i = 0; i < allegedSize; i++) {
            if (majorCurves.get(index1 + i).getType() != majorCurves.get(index2 + i).getType()) {
                return false;
            }
        }
        return true;
    }


}
