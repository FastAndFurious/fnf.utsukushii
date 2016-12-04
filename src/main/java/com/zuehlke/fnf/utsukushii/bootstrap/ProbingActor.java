package com.zuehlke.fnf.utsukushii.bootstrap;

import akka.actor.Props;
import com.zuehlke.fnf.actorbus.ActorBusActor;
import com.zuehlke.fnf.actorbus.Subscriptions;
import com.zuehlke.fnf.actorbus.logging.MessageCode;
import com.zuehlke.fnf.utsukushii.ScheduleNames;

import java.util.Random;

/**
 * Good for nothing but probing the monitoring functionality
 */
public class ProbingActor extends ActorBusActor {

    public static Subscriptions subscriptions = Subscriptions.none();

    private ProbingActor() {
        super("ProbingActor");
        scheduleRecurring(1000, ScheduleNames.PUBLISH);
    }

    public static Props props() {
        return Props.create(ProbingActor.class, ProbingActor::new);
    }

    @Override
    protected void onReceive2(Object message) throws Exception {

        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch ( Exception e) {
            warn(MessageCode.SERVER_ERROR, "Got interrupted. Don't worry.", e.getMessage());
        }
    }
}
