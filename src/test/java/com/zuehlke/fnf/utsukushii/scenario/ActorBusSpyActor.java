package com.zuehlke.fnf.utsukushii.scenario;

import akka.actor.Props;
import akka.actor.UntypedActor;
import com.zuehlke.carrera.relayapi.messages.RaceStartMessage;
import com.zuehlke.carrera.relayapi.messages.RaceStopMessage;
import com.zuehlke.carrera.relayapi.messages.SensorEvent;
import com.zuehlke.fnf.actorbus.Subscriptions;
import com.zuehlke.fnf.actorbus.UsageReport;
import com.zuehlke.fnf.actorbus.logging.LogReport;
import com.zuehlke.fnf.utsukushii.replay.RaceData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class ActorBusSpyActor extends UntypedActor {
    static Subscriptions subscriptions = Subscriptions
            .forClass(RaceStartMessage.class)
            .andForClass(RaceData.class)
            .andForClass(SensorEvent.class)
            .andForClass(RaceStopMessage.class)
            .andForClass(UsageReport.class)
            .andForClass(LogReport.class);

    static Props props() {
        return Props.create(ActorBusSpyActor.class, ActorBusSpyActor::new);
    }

    private List<Object> messages = new ArrayList<>();

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof Class<?>) {

            List<Object> messagesOfClass = messages.stream()
                    .filter((m) -> m.getClass() == message)
                    .collect(Collectors.toList());

            getSender().tell(messagesOfClass, getSelf());

        } else {
            messages.add(message);
        }

    }
}
