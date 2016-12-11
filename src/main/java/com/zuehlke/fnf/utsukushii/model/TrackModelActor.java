package com.zuehlke.fnf.utsukushii.model;

import akka.actor.Props;
import com.zuehlke.fnf.actorbus.ActorBusActor;
import com.zuehlke.fnf.actorbus.Subscriptions;

import java.util.ArrayList;
import java.util.List;

public class TrackModelActor extends ActorBusActor {

    enum Status {
        WAITING_FOR_STRAIGHT,
        EXPECTING_CURVE,
        READY
    }

    public static Props props ( TrackModelActorProperties properties) {
        return Props.create ( TrackModelActor.class, ()->new TrackModelActor(properties));
    }

    public static Subscriptions subscriptions = Subscriptions.forClass(TrackSectionSample.class);

    private TrackModel trackModel;
    private Status status = Status.WAITING_FOR_STRAIGHT;
    private TrackModelActorProperties properties;
    private List<TrackSectionSample> history = new ArrayList<>();
    private List<TrackSectionType> majorCurves = new ArrayList<>();


    private TrackModelActor(TrackModelActorProperties properties) {
        super("TrackModelActor");
        this.properties = properties;
    }

    @Override
    protected void onReceive2(Object message) throws Exception {

        if ( message instanceof TrackSectionSample ) {
            collectSample ( (TrackSectionSample) message );
        }
    }

    private void collectSample(TrackSectionSample sample) {
        history.add(sample);
        if ( status == Status.WAITING_FOR_STRAIGHT) {
            if ( sample.getType() == TrackSectionType.STRAIGHT && sample.lengthInMs() > properties.getMinStraightLength()) {
                status = Status.EXPECTING_CURVE;
            }
        } else if ( status == Status.EXPECTING_CURVE ) {
            if ( sample.getType() == TrackSectionType.LEFT_CURVE || sample.getType() == TrackSectionType.RIGHT_CURVE ) {
                learnFromSample(sample);
            }
        }
    }

    private void learnFromSample(TrackSectionSample sample) {
        majorCurves.add(sample.getType());
        int minCurves = properties.getMinNumberOfCurves();
        if ( majorCurves.size() >= 2 * minCurves) {
            for ( int allegedSize = minCurves; allegedSize <= majorCurves.size() / 2; allegedSize++ ) {
                boolean found = tryFindRecurringPattern ( allegedSize );
            }
        }
    }

    private boolean tryFindRecurringPattern ( int allegedSize ) {

        int index1 = majorCurves.size() - allegedSize;
        int index2 = majorCurves.size() - 2 * allegedSize;
        for ( int i = 0; i < allegedSize; i ++ ) {
            if ( majorCurves.get(index1 + i ) != majorCurves.get(index2 + i)) {
                return false;
            }
        }
        return true;
    }
}
