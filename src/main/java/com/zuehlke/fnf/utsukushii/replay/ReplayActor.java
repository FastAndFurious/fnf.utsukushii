package com.zuehlke.fnf.utsukushii.replay;

import akka.actor.Props;
import com.zuehlke.carrera.relayapi.messages.RaceStartMessage;
import com.zuehlke.carrera.relayapi.messages.RaceStopMessage;
import com.zuehlke.carrera.relayapi.messages.SensorEvent;
import com.zuehlke.carrera.relayapi.messages.VelocityMessage;
import com.zuehlke.fnf.actorbus.ActorBusActor;
import com.zuehlke.fnf.actorbus.ScheduledTask;
import com.zuehlke.fnf.actorbus.Subscriptions;
import com.zuehlke.fnf.utsukushii.ScheduleNames;

public class ReplayActor extends ActorBusActor{

    public static Subscriptions subscriptions = Subscriptions
            .forClass(RaceData.class);

    public static Props props () {
        return Props.create(ReplayActor.class, ReplayActor::new);
    }

    private int index;
    private int vs;
    private int ss;
    private int vi;
    private int si;
    private float frequency = 50;
    private RaceData data;

    private ReplayActor() {
        super("ReplayActor");
    }

    @Override
    protected void onReceive2(Object message) throws Exception {

        if ( message instanceof RaceData ) {
            handleRaceData((RaceData) message);

        } else if ( message instanceof SetFrequencyCommand ) {
            this.frequency = ((SetFrequencyCommand)message).getFrequency();

        } else if ( message instanceof ScheduledTask ) {

            if (ScheduleNames.REPLAY.equals(((ScheduledTask)message).getTaskId())) {
                handleNextMessage();
            }
        }
    }

    private void handleRaceData(RaceData data) {

        createStartMessage(data);

        this.data = data;
        vs = data.getVelocityMessages().size();
        ss = data.getSensorEvents().size();
        vi = 0;
        si = 0;
        index = 0;

        scheduleRecurring((int) (1000 / frequency), ScheduleNames.REPLAY);
    }

    private void handleNextMessage () {

        if ( index < vs + ss ) {
            index ++;

            VelocityMessage currentVelocity;
            if ( vi < vs  ) {
                currentVelocity = data.getVelocityMessages().get(vi);
            } else {
                if ( si < ss ) {
                    publish(data.getSensorEvents().get(si++));
                }
                return;
            }

            SensorEvent currentEvent;
            if ( si < ss  ) {
                currentEvent = data.getSensorEvents().get(si);
            } else {
                if ( vi < vs ) {
                    publish(data.getVelocityMessages().get(vi++));
                }
                return;
            }


            if ( currentVelocity.getT() > currentEvent.getT()) {
                publish(currentEvent);
                si++;
            } else {
                publish(currentVelocity);
                vi++;
            }

        } else {
            createStopMessage(data);
            cancelSchedule(ScheduleNames.REPLAY);
        }
    }

    private void createStartMessage(RaceData data) {
        String track = data.getTrackId();
        String team = data.getTeamId();
        long now = System.currentTimeMillis();
        publish(new RaceStartMessage(track, "replay", team, now, "replayed from file", false));
    }

    private void createStopMessage(RaceData data) {
        String track = data.getTrackId();
        String team = data.getTeamId();
        long now = System.currentTimeMillis();
        publish(new RaceStopMessage(track, team, now, "replay"));
    }


}
