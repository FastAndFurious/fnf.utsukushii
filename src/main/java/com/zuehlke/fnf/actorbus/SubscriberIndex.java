package com.zuehlke.fnf.actorbus;

import akka.actor.ActorRef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubscriberIndex {

    Map<Class<?>, Map<String, List<ActorRef>>> index = new HashMap<>();

    List<ActorRef> getSubscribersFor ( MessageEvent event ) {

        Map<String, List<ActorRef>> byClass = index.get(event.getMessageClass());
        List<ActorRef> res = new ArrayList<>();
        if ( byClass == null ) return res;

        if (byClass.get("") != null )
            res.addAll(byClass.get("")); // no matter what sender;
        if (byClass.get(event.getSender()) != null)
            res.addAll(byClass.get(event.getSender()));

        return res;
    }

    void addSubscriptions(Subscriptions subscriptions) {
        subscriptions.getPatterns().forEach((p)->{
            Class<?> clazz = p.getMessageClass();
            index.putIfAbsent(clazz, new HashMap<>());
            index.get(clazz).putIfAbsent(p.getSender(), new ArrayList<>());
            index.get(clazz).get(p.getSender()).add(subscriptions.getSubscriber());
        });
    }

}
