package com.zuehlke.fnf.actorbus;

import akka.actor.ActorRef;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class DispatcherAnnouncement {

    private ActorRef dispatcher;
}
