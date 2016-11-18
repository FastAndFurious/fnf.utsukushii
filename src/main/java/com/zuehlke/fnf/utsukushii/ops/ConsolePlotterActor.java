package com.zuehlke.fnf.utsukushii.ops;

import akka.actor.Props;
import com.zuehlke.carrera.relayapi.messages.SensorEvent;
import com.zuehlke.carrera.relayapi.messages.VelocityMessage;
import com.zuehlke.fnf.actorbus.ActorBusActor;
import com.zuehlke.fnf.actorbus.Subscriptions;
import org.apache.commons.lang.StringUtils;

import java.io.PrintStream;

public class ConsolePlotterActor extends ActorBusActor {

    private PrintStream out = System.out;

    private ConsolePlotterActor() {
        super("ConsolePlotterActor");
    }

    public static Props props () {
        return Props.create ( ConsolePlotterActor.class, ConsolePlotterActor::new);
    }

    public static Subscriptions subscriptions = Subscriptions
            .forClass(VelocityMessage.class)
            .andForClass(SensorEvent.class);


    @Override
    protected void onReceive2(Object message) throws Exception {

        if ( message instanceof SensorEvent ) {
            handleSensorEvent ( (SensorEvent) message );

        } else if ( message instanceof VelocityMessage ) {
            handleVelocityMessage ( (VelocityMessage) message );
        }
    }

    private void handleVelocityMessage(VelocityMessage velocityMessage) {
        out.println (String.format("Velocity : %3.0f", velocityMessage.getVelocity()));
    }

    private void handleSensorEvent(SensorEvent event) {
        plotGyroZ ( event );
    }

    private void plotGyroZ ( SensorEvent event ) {
        consolePlot(event.getT(), event.getG()[2]);
    }

    private void consolePlot(int t, int val) {
        String prefix = String.format("%07d: ", t);
        int scale = 120 * (val - (-10000) ) / 20000;
        out.println(prefix + StringUtils.repeat(" ", scale) + val);
    }


}
