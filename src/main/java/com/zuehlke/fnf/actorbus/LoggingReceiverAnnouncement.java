package com.zuehlke.fnf.actorbus;

import akka.actor.ActorRef;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * wraps the loggingReceiver actor in a type-safe manner,
 */
@Data
@AllArgsConstructor
class LoggingReceiverAnnouncement {

    private ActorRef logger;
}
