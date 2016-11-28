package com.zuehlke.fnf.actorbus;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.zuehlke.fnf.actorbus.logging.LoggingReceiver;
import com.zuehlke.fnf.utsukushii.UtsukushiiProperties;

public class ActorBus {

    private final ActorSystem actorSystem;
    private final ActorRef dispatcherActor;
    private final ActorRef monitorActor;
    private final ActorRef loggerActor;

    ActorBus(ActorSystem actorSystem, UtsukushiiProperties properties ) {
        this.actorSystem = actorSystem;
        this.dispatcherActor = actorSystem.actorOf(DispatcherActor.props());
        this.monitorActor = actorSystem.actorOf(MonitorActor.props(dispatcherActor, 3000));
        this.loggerActor = actorSystem.actorOf(LoggingReceiver.props(properties.getLoggingReceiverProperties(), dispatcherActor));
    }

    public ActorRef register (String name, Props props, Subscriptions subscriptions ) {

        ActorRef actorRef = actorSystem.actorOf(props, name);
        subscriptions.setSubscriber( actorRef );
        dispatcherActor.tell(subscriptions, ActorRef.noSender());

        actorRef.tell(new DispatcherAnnouncement(dispatcherActor), ActorRef.noSender());
        actorRef.tell(new MonitorAnnouncement(monitorActor), ActorRef.noSender());
        actorRef.tell(new LoggingReceiverAnnouncement(loggerActor), ActorRef.noSender());

        return actorRef;
    }

    public void publish ( Object message ) {
        dispatcherActor.tell(message, ActorRef.noSender());
    }
}
