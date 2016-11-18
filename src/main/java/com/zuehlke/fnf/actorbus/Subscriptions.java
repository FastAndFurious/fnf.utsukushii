package com.zuehlke.fnf.actorbus;

import akka.actor.ActorRef;

import java.util.ArrayList;
import java.util.List;

public class Subscriptions {

    private List<MessageMatcher> patterns = new ArrayList<>();
    private ActorRef subscriber;

    public static Subscriptions none() {
        return new Subscriptions();    }

    public static Subscriptions forClass(Class<?> messageClass) {
        Subscriptions forClass = new Subscriptions();
        forClass.patterns.add(new SingleClassMatcher( messageClass ));
        return forClass;
    }

    public Subscriptions andForClass ( Class<?> otherClass ) {
        patterns.add(new SingleClassMatcher(otherClass));
        return this;
    }

    public List<MessageMatcher> getPatterns() {
        return patterns;
    }

    public void setSubscriber(ActorRef actor) {
        this.subscriber = actor;
    }

    public ActorRef getSubscriber() {
        return subscriber;
    }
}

