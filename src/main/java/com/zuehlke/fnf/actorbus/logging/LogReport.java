package com.zuehlke.fnf.actorbus.logging;

import java.util.Collection;

/**
 * encapsulates a collection of messages and their resp. occurrence counts
 */
public class LogReport {
    private Collection<LogEntryCounter> entries;

    public LogReport(Collection<LogEntryCounter> entries) {
        this.entries = entries;
    }

    public Collection<LogEntryCounter> getEntries() {
        return entries;
    }
}
