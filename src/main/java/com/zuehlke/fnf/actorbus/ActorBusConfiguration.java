package com.zuehlke.fnf.actorbus;

import akka.actor.ActorSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActorBusConfiguration {

    private static final String ACTOR_SYSTEM_NAME = "ACTOR_BUS_SYSTEM";

    @Bean
    public ActorSystem actorSystem () {
        return ActorSystem.create ( ACTOR_SYSTEM_NAME );
    }

    @Bean
    public ActorBus actorBus ( ActorSystem actorSystem ) {
        return new ActorBus( actorSystem );
    }
}
