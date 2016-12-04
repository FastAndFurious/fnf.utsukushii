package com.zuehlke.fnf.utsukushii.web;

import akka.actor.Props;
import com.zuehlke.fnf.actorbus.ActorBusActor;
import com.zuehlke.fnf.actorbus.Subscriptions;
import com.zuehlke.fnf.actorbus.UsageReport;
import com.zuehlke.fnf.actorbus.logging.LogReport;
import com.zuehlke.fnf.actorbus.logging.MessageCode;
import com.zuehlke.fnf.utsukushii.replay.ReplayStatus;

public class WebSocketPublisherActor extends ActorBusActor {

    public static final Subscriptions subscriptions = Subscriptions
            .forClass(LogReport.class)
            .andForClass(ReplayStatus.class)
            .andForClass(UsageReport.class);

    private WebSocketHandler logReportHandler;
    private WebSocketHandler replayStatusHandler;
    private WebSocketHandler usageStatsHandler;

    public static Props props(WebSocketHandler logReportHandler, WebSocketHandler replayStatusHandler, WebSocketHandler usageStatsHandler ) {
        return Props.create(WebSocketPublisherActor.class,
                () -> new WebSocketPublisherActor(logReportHandler, replayStatusHandler, usageStatsHandler));
    }

    private WebSocketPublisherActor(
            WebSocketHandler logReportHandler,
            WebSocketHandler replayStatusHandler,
            WebSocketHandler usageStatsHandler) {

        super("WebSocketPublisherActor");
        this.logReportHandler = logReportHandler;
        this.replayStatusHandler = replayStatusHandler;
        this.usageStatsHandler = usageStatsHandler;
    }

    @Override
    protected void onReceive2(Object message) throws Exception {

        if (message instanceof LogReport) {
            if ( logReportHandler.isConnected()) {
                logReportHandler.send(message);
            }

        } else if (message instanceof ReplayStatus) {
            if ( replayStatusHandler.isConnected()) {
                replayStatusHandler.send(message);
            }

        } else if (message instanceof UsageReport) {
            if ( usageStatsHandler.isConnected()) {
                usageStatsHandler.send(message);
                //System.out.print(".");
            }

        } else {
            warn(MessageCode.ILLEGAL_MESSAGE, "Not ready to send message of type "
                    + message.getClass(), getSender().toString());
            unhandled( message );
        }

    }
}
