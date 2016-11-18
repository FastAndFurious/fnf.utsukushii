package com.zuehlke.fnf.actorbus.logging;

public class LogEntryCounter {
    public int count;
    public LogEntry latestEntry;

    public LogEntryCounter(LogEntry latestEntry) {
        this.count = 1;
        this.latestEntry = latestEntry;
    }

    /**
     * replace the latest entry with a new one
     *
     * @param entry the new entry that just came in
     */
    public void add(LogEntry entry) {
        latestEntry = entry;
        count++;
    }
}
