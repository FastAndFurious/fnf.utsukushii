package com.zuehlke.fnf.actorbus;

class UsageRecord {

    String name;
    long created;
    int d_busy; // period between recent in and recent out: the busy time
    int d_idle; // period between previous out and recent in: the idle time

    UsageRecord(String name, int d_busy, int d_idle) {
        this.created = System.currentTimeMillis();
        this.name = name;
        this.d_busy = d_busy;
        this.d_idle = d_idle;
    }

}
