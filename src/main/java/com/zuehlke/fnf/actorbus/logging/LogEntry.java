package com.zuehlke.fnf.actorbus.logging;


/**
 * Represents a log entry.
 */
public class LogEntry {

    private Class<?> source;
    private MessageCode code;
    private Severity severity;
    private String description;
    private String origin;
    private long timeStamp;

    /**
     * Empty constructor for serialisation
     */
    protected LogEntry() {
    }


    /**
     * Creates a new LogEntry
     *
     * @param code        the message code
     * @param description Summary of the problem
     * @param origin      The cause / issues of the problem
     */
    public LogEntry(Class<?> source, MessageCode code, Severity severity, String description, String origin) {
        this(source, code, severity, description, origin, System.currentTimeMillis());
    }

    /**
     * Creates a new LogEntry
     *
     * @param source      the
     * @param code        The type / area of the entry
     * @param severity    the severity of this log entry
     * @param description Summary of the problem
     * @param origin      The cause / location of the problem
     * @param timeStamp   Timestamp when the problem occurred
     */
    public LogEntry(Class<?> source, MessageCode code, Severity severity, String description, String origin, long
            timeStamp) {
        this.source = source;
        this.code = code;
        this.description = description;
        this.origin = origin;
        this.timeStamp = timeStamp;
        this.severity = severity;
    }

    public MessageCode getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getOrigin() {
        return origin;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public Class<?> getSource() {
        return source;
    }

    public Severity getSeverity() {
        return severity;
    }

    /**
     * this classifying string helps to accumulate the number of messages
     * belonging to the same classification.
     */
    public String classifyingDescription() {
        return "LogEntry { " +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", origin='" + origin + "'}";
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", source='" + source.getSimpleName() + '\'' +
                ", origin='" + origin + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }


}
