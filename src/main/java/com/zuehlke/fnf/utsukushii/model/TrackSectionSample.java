package com.zuehlke.fnf.utsukushii.model;

import com.zuehlke.carrera.relayapi.messages.SensorEvent;
import com.zuehlke.carrera.relayapi.messages.VelocityMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class TrackSectionSample {

    @Getter
    private final TrackSectionType type;

    @Getter
    private final List<SensorEvent> sensorEvents = new ArrayList<>();

    @Getter
    @Setter
    private VelocityMessage velocityAtEntry;

    @Getter
    @Setter
    private VelocityMessage velocityAtExit;

    @Setter
    private boolean velocitiesAreGood = false;

    @Setter
    @Getter
    private TrackSectionSample previous;

    @Setter
    @Getter
    private TrackSectionSample next;

    TrackSectionSample(TrackSectionType type) {
        this.type = type;
    }

    public void add(SensorEvent event) {
        sensorEvents.add(event);
    }

    void finalizeSection() {
        sensorEvents.sort((l, r) -> (int) (r.getTimeStamp() + r.getTimeStamp() - l.getTimeStamp() - l.getTimeStamp()));
    }

    int getDurationInMs() {
        int size = sensorEvents.size();
        if (size <= 1) return 0;
        if (sensorEvents.get(0).getTimeStamp() == 0) {
            return sensorEvents.get(size - 1).getT() - sensorEvents.get(0).getT();
        } else {
            return (int) (sensorEvents.get(size - 1).getTimeStamp() - sensorEvents.get(0).getTimeStamp());
        }
    }

    /**
     * @return the length of the section, if it can be calculated
     */
    Optional<Integer> getLengthInCm() {
        if (velocityAtEntry != null && velocityAtExit != null) {
            int v_avg = (int) (velocityAtEntry.getVelocity()
                    + velocityAtExit.getVelocity()) / 2;
            return Optional.of(v_avg * getDurationInMs() / 1000);

        } else if (velocityAtEntry != null) {
            return Optional.of((int) velocityAtEntry.getVelocity() * getDurationInMs() / 1000);
        } else if (velocityAtExit != null) {
            return Optional.of((int) velocityAtExit.getVelocity() * getDurationInMs() / 1000);
        }
        return Optional.empty();
    }

    /**
     * @return true if both velocities are timely well-correlated with entry and exit time stamps. false otherwise
     */
    boolean velocitiesAreGood() {
        return velocitiesAreGood;
    }

    boolean isCurve() {
        return type == TrackSectionType.LEFT_CURVE || type == TrackSectionType.RIGHT_CURVE;
    }

    static TrackSectionSample concatenate(List<TrackSectionSample> samples) {
        TrackSectionSample sample = new TrackSectionSample(samples.get(0).getType());
        for (TrackSectionSample sample1 : samples) {
            sample.getSensorEvents().addAll(sample1.getSensorEvents());
        }
        sample.setVelocityAtEntry(samples.get(0).getVelocityAtEntry());
        sample.setVelocityAtExit(samples.get(samples.size() - 1).getVelocityAtExit());
        return sample;
    }

    int endsAtTime () {
        return sensorEvents.get(sensorEvents.size()-1).getT();
    }

    int startsAtTime () {
        return sensorEvents.get(0).getT();
    }

    int middleAtTime () {
        return (endsAtTime() + startsAtTime()) / 2;
    }

}
