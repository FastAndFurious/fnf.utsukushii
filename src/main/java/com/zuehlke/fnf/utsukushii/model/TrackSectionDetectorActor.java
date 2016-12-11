package com.zuehlke.fnf.utsukushii.model;

import akka.actor.Props;
import com.zuehlke.carrera.relayapi.messages.SensorEvent;
import com.zuehlke.fnf.actorbus.ActorBusActor;
import com.zuehlke.fnf.actorbus.Subscriptions;

public class TrackSectionDetectorActor extends ActorBusActor {

    public static Props props(TrackSectionStartDetector detector) {
        return Props.create(TrackSectionDetectorActor.class, () -> new TrackSectionDetectorActor(detector));
    }

    public static Subscriptions subscriptions = Subscriptions.forClass(SensorEvent.class);

    private TrackSectionStartDetector detector;
    private long startTime;
    private TrackSectionSample currentSample;

    private TrackSectionDetectorActor(TrackSectionStartDetector detector) {
        super("TrackSectionDetectorActor");
        this.detector = detector;
    }

    @Override
    protected void onReceive2(Object message) throws Exception {

        if (message instanceof SensorEvent) {
            publishIfSectionStart((SensorEvent) message);
        }
    }

    private void publishIfSectionStart(SensorEvent event) {

        int t;
        if (event.getT() == 0) {
            if (startTime == 0) startTime = event.getTimeStamp();
            t = (int) (event.getTimeStamp() - startTime);
        } else {
            t = event.getT();
        }

        if ( currentSample != null ) {
            currentSample.add(event);
        }
        detector.putAndDetect(t, event.getG()[2])
                .ifPresent((sectionStart) -> {
                    if ( currentSample != null ) {
                        currentSample.finalizeSection();
                        publish(currentSample);
                    }
                    currentSample = new TrackSectionSample(sectionStart.getType());
                    publish(sectionStart);
                });
    }


}
