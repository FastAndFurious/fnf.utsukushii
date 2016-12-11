package com.zuehlke.fnf.utsukushii.bootstrap;

import akka.actor.Props;
import com.zuehlke.carrera.javapilot.services.PilotToRelayConnection;
import com.zuehlke.carrera.relayapi.messages.PowerControl;
import com.zuehlke.fnf.actorbus.ActorBusActor;
import com.zuehlke.fnf.actorbus.Subscriptions;

class PowerPublisherActor extends ActorBusActor {

    private final PilotToRelayConnection connection;

    static Subscriptions subscriptions = Subscriptions
            .forClass(PowerControl.class);

    static Props props ( PilotToRelayConnection connection ) {
        return Props.create ( PowerPublisherActor.class, ()->new PowerPublisherActor(connection));
    }

    private PowerPublisherActor (PilotToRelayConnection connection ) {
        super ( "PowerPublisherActor");
        this.connection = connection;
    }

    @Override
    protected void onReceive2(Object message) throws Exception {
        if ( connection == null ) return;
        if ( message instanceof PowerControl ) {
            connection.send((PowerControl) message);
        }

    }
}
