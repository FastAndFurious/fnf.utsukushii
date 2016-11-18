package com.zuehlke.fnf.actorbus.logging;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

/**
 * adds a couple of useful features to an untyped actor
 */
@Slf4j
abstract public class AbstractLoggingActor extends UntypedActor {

    protected ActorRef logActor;

    /**
     * @param code    the error code
     * @param message a meaningful message
     * @param origin  any hint about where the problem may have originated
     */
    private void log(Class<?> source, MessageCode code, Severity severity, String message, String origin) {
        LogEntry entry = new LogEntry(
            source, code, severity, message, origin);

        logActor.tell(entry, getSelf());
        log.debug("Got message: {}", entry);
    }


    protected void info(MessageCode code, String description, String origin) {
        log(getClass(), code, Severity.INFO, description, origin);
    }

    protected void warn(MessageCode code, String description, String origin) {
        log(getClass(), code, Severity.WARN, description, origin);
    }

    protected void debug(MessageCode code, String description, String origin) {
        log(getClass(), code, Severity.DEBUG, description, origin);
    }

    protected void error(MessageCode code, String description, String origin) {
        log(getClass(), code, Severity.ERROR, description, origin);
    }

    protected void fatal(MessageCode code, String description, String origin) {
        log(getClass(), code, Severity.FATAL, description, origin);
    }

}
