package com.zuehlke.fnf.actorbus;

import akka.actor.ActorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActorBusBootstrapper {

    private final ActorSystem actorSystem;

    @Autowired
    public ActorBusBootstrapper(ActorSystem system) {
        this.actorSystem = system;
    }
}
