package com.zuehlke.fnf.utsukushii.web;

import akka.actor.Props;
import com.zuehlke.fnf.actorbus.ActorBusActor;
import com.zuehlke.fnf.actorbus.Subscriptions;
import com.zuehlke.fnf.actorbus.logging.LogReport;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class StompPublisherActor extends ActorBusActor {

    public static Props props () {
        return Props.create(StompPublisherActor.class, ()->new StompPublisherActor());
    }

    public static Subscriptions subscriptions = Subscriptions.forClass(LogReport.class);

    private StompPublisherActor() {
        super("StompPublisherActor");
    }

    @Override
    protected void onReceive2(Object message) throws Exception {

        if ( message instanceof LogReport ) {
            //System.out.println("Would send log report, but not implemented yet");
        }

    }
}
