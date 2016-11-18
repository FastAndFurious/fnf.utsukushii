package com.zuehlke.fnf.actorbus;

import akka.actor.Props;
import akka.actor.UntypedActor;

public class DispatcherActor extends UntypedActor {

    private SubscriberIndex subscriberIndex = new SubscriberIndex();

    public static Props props() {
        return Props.create ( DispatcherActor.class, ()->new DispatcherActor());
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if ( message instanceof Subscriptions) {
            subscriberIndex.addSubscriptions((Subscriptions)message);
        } else {
            String name = getSender().path().name();
            MessageEvent event = new MessageEvent(message.getClass(), name);
            subscriberIndex.getSubscribersFor(event).forEach((s)->s.tell(message, getSender()));
        }

    }
}
