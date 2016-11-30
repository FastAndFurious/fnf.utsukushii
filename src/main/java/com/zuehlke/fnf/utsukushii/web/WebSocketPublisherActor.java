package com.zuehlke.fnf.utsukushii.web;

import akka.actor.Props;
import com.zuehlke.fnf.actorbus.ActorBusActor;
import com.zuehlke.fnf.actorbus.Subscriptions;
import com.zuehlke.fnf.actorbus.logging.LogReport;

public class WebSocketPublisherActor extends ActorBusActor {

    private WebSocketHandler webSocketHandler;

    public static Props props(WebSocketHandler wsHandler) {
        return Props.create(WebSocketPublisherActor.class, () -> new WebSocketPublisherActor(wsHandler));
    }

    public static Subscriptions subscriptions = Subscriptions.forClass(LogReport.class);

    private WebSocketPublisherActor(WebSocketHandler webSocketHandler) {

        super("StompPublisherActor");
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    protected void onReceive2(Object message) throws Exception {

        if (message instanceof LogReport) {
            webSocketHandler.send((LogReport) message);
        }

    }
}
