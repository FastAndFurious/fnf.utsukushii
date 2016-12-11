package com.zuehlke.fnf.utsukushii.replay;

import akka.actor.Props;
import com.zuehlke.carrera.relayapi.messages.RaceStartMessage;
import com.zuehlke.carrera.relayapi.messages.RaceStopMessage;
import com.zuehlke.carrera.relayapi.messages.SensorEvent;
import com.zuehlke.carrera.relayapi.messages.VelocityMessage;
import com.zuehlke.fnf.actorbus.ActorBusActor;
import com.zuehlke.fnf.actorbus.ScheduledTask;
import com.zuehlke.fnf.actorbus.Subscriptions;
import com.zuehlke.fnf.actorbus.logging.MessageCode;
import com.zuehlke.fnf.utsukushii.ScheduleNames;
import com.zuehlke.fnf.utsukushii.UtsukushiiProperties;

public class ReplayActor extends ActorBusActor{

    public static final Subscriptions subscriptions = Subscriptions
            .forClass(RaceData.class)
            .andForClass(SetFrequencyCommand.class)
            .andForClass(SuspendReplayCommand.class)
            .andForClass(ResumeReplayCommand.class)
            .andForClass(StopReplayCommand.class)
            .andForClass(UtsukushiiProperties.class);

    public static Props props () {
        return Props.create(ReplayActor.class, ReplayActor::new);
    }

    private static final int STATUS_UPDATE_FREQUENCY_WHEN_IDLE = 2000; // milliseconds
    private static final int STATUS_UPDATE_FREQUENCY_WHEN_PLAYING = 100; // milliseconds
    private int index;
    private int vs;
    private int ss;
    private int vi;
    private int si;
    private float frequency = 50.0f;
    private RaceData data;
    private ReplayStatus.Status status = ReplayStatus.Status.OFF;
    private UtsukushiiProperties properties;

    private ReplayActor() {
        super("ReplayActor");
        scheduleRecurring(STATUS_UPDATE_FREQUENCY_WHEN_IDLE, ScheduleNames.PUBLISH);
    }

    @Override
    protected void onReceive2(Object message) throws Exception {

        if ( message instanceof RaceData ) {
            startIfPossible((RaceData) message);

        } else if ( message instanceof SuspendReplayCommand ) {
            suspend();

        } else if ( message instanceof ResumeReplayCommand ) {
            resume();

        } else if ( message instanceof StopReplayCommand ) {
            stop();

        } else if ( message instanceof SetFrequencyCommand ) {
            handleFrequencyUpdate(((SetFrequencyCommand)message).getFrequency());

        } else if ( message instanceof ScheduledTask ) {

            if (ScheduleNames.REPLAY.equals(((ScheduledTask)message).getTaskId())) {
                handleNextMessage();
            } else if ( ScheduleNames.PUBLISH.equals(((ScheduledTask)message).getTaskId())) {
                publishStatus ();
            }
        } else if ( message instanceof UtsukushiiProperties ) {
            handlePropertiesUpdate ( (UtsukushiiProperties) message );
        }
    }

    private void handlePropertiesUpdate(UtsukushiiProperties properties) {
        handleFrequencyUpdate(properties.getReplayProperties().getFrequency());
    }

    private void handleFrequencyUpdate ( float newFrequency ) {
        if ( frequency != newFrequency ) {
            frequency = newFrequency;
            if ( status == ReplayStatus.Status.PLAYING ) {
                cancelSchedule(ScheduleNames.REPLAY);
                scheduleRecurring((int) (1000 / frequency), ScheduleNames.REPLAY);
            }
        }
    }

    private void publishStatus () {
        int milliesSinceStart = 0;
        ReplayStatus newStatus;
        if ( data == null ) {
            newStatus = ReplayStatus.OFF;
        } else {
            newStatus = new ReplayStatus(status, data.getId(), milliesSinceStart);
        }
        publish(newStatus);
    }

    private void startIfPossible(RaceData data) {

        if ( status == ReplayStatus.Status.PLAYING ) {
            warn(MessageCode.ALREADY_PLAYING, "Can't start new replay. Already playing.", getSender().path().name());
            return;
        }
        status = ReplayStatus.Status.PLAYING;
        setNewStatusUpdateFrequency( STATUS_UPDATE_FREQUENCY_WHEN_PLAYING );
        publishStartMessage(data);

        this.data = data;
        vs = data.getVelocityMessages().size();
        ss = data.getSensorEvents().size();
        vi = 0;
        si = 0;
        index = 0;

        scheduleRecurring((int) (1000 / frequency), ScheduleNames.REPLAY);
        setNewStatusUpdateFrequency( STATUS_UPDATE_FREQUENCY_WHEN_PLAYING );
    }

    private void suspend () {
        status = ReplayStatus.Status.SUSPENDED;
        cancelSchedule(ScheduleNames.REPLAY);
        setNewStatusUpdateFrequency( STATUS_UPDATE_FREQUENCY_WHEN_IDLE );
    }

    private void resume () {

        if ( data != null ) {
            status = ReplayStatus.Status.PLAYING;
            scheduleRecurring((int) (1000 / frequency), ScheduleNames.REPLAY);
        }
    }

    private void stop () {
        status = ReplayStatus.Status.OFF;
        setNewStatusUpdateFrequency( STATUS_UPDATE_FREQUENCY_WHEN_IDLE );
        cancelSchedule(ScheduleNames.REPLAY);
        publishStopMessage(data);
        this.data = null;
    }

    private void setNewStatusUpdateFrequency(int newFrequency) {
        cancelSchedule(ScheduleNames.PUBLISH);
        scheduleRecurring(newFrequency, ScheduleNames.PUBLISH);
    }

    private void handleNextMessage () {

        if ( data == null ) {
            error(MessageCode.SERVER_ERROR,
                    "Shouldn't get here with no data.", "Scheduled Message.");
        }

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
            publishStopMessage(data);
            cancelSchedule(ScheduleNames.REPLAY);
        }
    }

    private void publishStartMessage(RaceData data) {
        String track = data.getTrackId();
        String team = data.getTeamId();
        long now = System.currentTimeMillis();
        publish(new RaceStartMessage(track, "replay", team, now, "replayed from file", false));
    }

    private void publishStopMessage(RaceData data) {
        String track = data.getTrackId();
        String team = data.getTeamId();
        long now = System.currentTimeMillis();
        publish(new RaceStopMessage(track, team, now, "replay"));
    }


}
