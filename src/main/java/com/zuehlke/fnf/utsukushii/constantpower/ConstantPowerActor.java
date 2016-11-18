package com.zuehlke.fnf.utsukushii.constantpower;

import akka.actor.Props;
import com.zuehlke.carrera.relayapi.messages.RaceStartMessage;
import com.zuehlke.carrera.relayapi.messages.RaceStopMessage;
import com.zuehlke.fnf.actorbus.ActorBusActor;
import com.zuehlke.fnf.actorbus.ScheduledTask;
import com.zuehlke.fnf.actorbus.Subscriptions;
import com.zuehlke.fnf.actorbus.logging.MessageCode;
import com.zuehlke.fnf.utsukushii.ScheduleNames;
import com.zuehlke.fnf.utsukushii.strategy.PowerRecommendation;
import com.zuehlke.fnf.utsukushii.strategy.RecommendationStrategy;
import com.zuehlke.fnf.utsukushii.strategy.SituationalContext;

public class ConstantPowerActor extends ActorBusActor {

    public static Subscriptions subscriptions = Subscriptions
            .forClass(RaceStartMessage.class)
            .andForClass(RaceStopMessage.class)
            .andForClass(SetPowerCommand.class);

    public static Props props(ConstantPowerProperties properties) {
        return Props.create(ConstantPowerActor.class, () -> new ConstantPowerActor(properties));
    }

    private int currentPower = 110;
    private ConstantPowerProperties props;

    private ConstantPowerActor(ConstantPowerProperties props) {
        super("ConstantPowerActor");
        this.props = props;
    }

    @Override
    protected void onReceive2(Object message) throws Exception {

        if (message instanceof SetPowerCommand) {
            this.currentPower = ((SetPowerCommand) message).getPower();
            info(MessageCode.USAGE, "Setting power to " + currentPower, getSender().path().name());

        } else if (message instanceof RaceStartMessage) {
            info(MessageCode.USAGE, "Received Start Message.", getSender().path().name());
            scheduleRecurring(durationBetweenPublishing(), ScheduleNames.POWER);

        } else if (message instanceof RaceStopMessage) {
            info(MessageCode.USAGE, "Received Stop Message.", getSender().path().name());
            cancelSchedule(ScheduleNames.POWER);

        } else if (message instanceof ScheduledTask) {
            info(MessageCode.USAGE, "Publishing Power value " + currentPower, getSender().path().name());
            // there's only one kind of scheduled task here, so we don't check the taskId
            publish(new PowerRecommendation(RecommendationStrategy.CONSTANT_POWER, currentPower, SituationalContext.ANY));
        }

    }



    /**
     * @return duration in ms from frequency in Hz
     */
    private int durationBetweenPublishing() {
        return (int) (1000 / frequencyInHertz());
    }

    /**
     * fail-safe implementation with fallback to 1Hz
     *
     * @return the frequency in Hertz
     */
    private float frequencyInHertz() {
        if (props == null || props.getFrequencyInHertz() == 0) return 1;
        return props.getFrequencyInHertz();
    }
}
