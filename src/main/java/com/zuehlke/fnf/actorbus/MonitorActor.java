package com.zuehlke.fnf.actorbus;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

import java.util.HashMap;
import java.util.Map;

class MonitorActor extends UntypedActor{

    private final Map<String, UsageStatistics> actorStats = new HashMap<>();

    private final ActorRef dispatcher;
    private final int usagePeriod;

    private MonitorActor(ActorRef dispatcher, int usagePeriod ) {
        this.dispatcher = dispatcher;
        this.usagePeriod = usagePeriod;
    }

    public static Props props (ActorRef dispatcher, int usagePeriod ) {
        return Props.create(MonitorActor.class, ()->new MonitorActor( dispatcher, usagePeriod ));
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if ( message instanceof UsageRecord) {

            String senderName = getSender().path().name();
            actorStats.putIfAbsent(senderName, new UsageStatistics(senderName, usagePeriod));
            UsageStatistics stats = actorStats.get(senderName);
            UsageRecord record = (UsageRecord) message;
            //System.out.println("Usage for " + senderName + ": " + record.d_busy + ", " + record.d_idle);
            dispatcher.tell(stats.with(record), getSelf());
        } else {
            unhandled(message);
        }

    }
}
