package com.zuehlke.fnf.actorbus.logging;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.zuehlke.fnf.actorbus.ActorBusActor;
import com.zuehlke.fnf.actorbus.ScheduledTask;
import com.zuehlke.fnf.utsukushii.ScheduleNames;

import java.util.HashMap;
import java.util.Map;

/**
 * This actor receives all {@link LogEntry} messages
 * and consolidates them.
 */
public class LoggingReceiver extends ActorBusActor {

    private final Map<String, LogEntryCounter> classificationMap = new HashMap<>();

    private LoggingReceiver( LoggingReceiverProperties props, ActorRef dispatcher ) {
        super("LoggingReceiver");
        logActor = getSelf();
        this.dispatcher = dispatcher;
        scheduleRecurring(props.getPublishEveryAfter(), ScheduleNames.PUBLISH);
    }

    public static Props props(LoggingReceiverProperties properties, ActorRef dispatcher) {
        return Props.create(LoggingReceiver.class, ()->new LoggingReceiver(properties, dispatcher));
    }

    @Override
    public void onReceive2(Object message) throws Exception {
        if (message instanceof LogEntry) {
            handleLogEntry((LogEntry) message);

        } else if (message instanceof QueryLogReport) {
            handleQueryLogReport();

        } else if ( message instanceof ScheduledTask ) {
            publish(new LogReport(classificationMap.values()));

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
