package com.zuehlke.fnf.actorbus.logging;

import akka.actor.Props;
import akka.actor.UntypedActor;

import java.util.HashMap;
import java.util.Map;

/**
 * This actor receives all {@link LogEntry} messages
 * and consolidates them.
 */
public class LoggingReceiver extends UntypedActor {


    private final Map<String, LogEntryCounter> classificationMap = new HashMap<>();

    public static Props props() {
        return Props.create(LoggingReceiver.class, LoggingReceiver::new);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof LogEntry) {
            handleLogEntry((LogEntry) message);
        } else if (message instanceof QueryLogReport) {
            handleQueryLogReport();
        } else {
            handleLogEntry(new LogEntry(LoggingReceiver.class, MessageCode.NOT_A_LOG_ENTRY, Severity.WARN,
                "Received a message of type " + message.getClass().getSimpleName(),
                "Actor: " + getSender().toString()));
        }
    }

    /**
     * Request to consolidate a create-report from the
     * collected create-entries.
     */
    private void handleQueryLogReport() {

        getSender().tell(new LogReport(classificationMap.values()), getSelf());
    }

    /**
     * adds the entry if it has a new classification, inc counter otherwise
     *
     * @param entry the new entry
     */
    private void handleLogEntry(LogEntry entry) {

        String classification = entry.classifyingDescription();
        LogEntryCounter counter = classificationMap.get(classification);

        if (counter == null) {
            counter = new LogEntryCounter(entry);
            classificationMap.put(classification, counter);
        } else {
            counter.add(entry);
        }
    }
}
