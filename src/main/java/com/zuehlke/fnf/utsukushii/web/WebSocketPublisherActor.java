package com.zuehlke.fnf.utsukushii.web;

import akka.actor.Props;
import com.zuehlke.fnf.actorbus.ActorBusActor;
import com.zuehlke.fnf.actorbus.Subscriptions;
import com.zuehlke.fnf.actorbus.logging.LogReport;
import com.zuehlke.fnf.actorbus.logging.MessageCode;
import com.zuehlke.fnf.utsukushii.replay.ReplayStatus;

public class WebSocketPublisherActor extends ActorBusActor {

    public static final Subscriptions subscriptions = Subscriptions
            .forClass(LogReport.class)
            .andForClass(ReplayStatus.class);

    private WebSocketHandler logReportHandler;
    private WebSocketHandler replayStatusHandler;

    public static Props props(WebSocketHandler logReportHandler, WebSocketHandler replayStatusHandler) {
        return Props.create(WebSocketPublisherActor.class, () -> new WebSocketPublisherActor(logReportHandler, replayStatusHandler));
    }

    private WebSocketPublisherActor(WebSocketHandler logReportHandler, WebSocketHandler replayStatusHandler) {

        super("WebSocketPublisherActor");
        this.logReportHandler = logReportHandler;
        this.replayStatusHandler = replayStatusHandler;
    }

    @Override
    protected void onReceive2(Object message) throws Exception {

        if (message instanceof LogReport) {
            logReportHandler.send(message);

        } else if (message instanceof ReplayStatus) {
            replayStatusHandler.send(message);

        } else {
            warn(MessageCode.ILLEGAL_MESSAGE, "Not ready to send message of type "
                    + message.getClass(), getSender().toString());
            unhandled( message );
        }

    }
}
