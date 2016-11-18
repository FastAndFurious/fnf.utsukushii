package com.zuehlke.fnf.actorbus;

import akka.actor.ActorRef;

public class MonitorAnnouncement {
    private ActorRef monitor;

    public MonitorAnnouncement(ActorRef monitor) {
        this.monitor = monitor;
    }

    public ActorRef getMonitor() {
        return monitor;
    }
}
