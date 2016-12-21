package com.zuehlke.fnf.utsukushii.ops;

import akka.actor.Props;
import com.zuehlke.carrera.relayapi.messages.SensorEvent;
import com.zuehlke.carrera.relayapi.messages.VelocityMessage;
import com.zuehlke.fnf.actorbus.ActorBusActor;
import com.zuehlke.fnf.actorbus.Subscriptions;
import com.zuehlke.fnf.utsukushii.UtsukushiiProperties;
import com.zuehlke.fnf.utsukushii.model.TrackSectionStart;
import com.zuehlke.fnf.utsukushii.model.TrackSectionType;
import org.apache.commons.lang.StringUtils;

import java.io.PrintStream;

public class ConsolePlotterActor extends ActorBusActor {

    private PrintStream out = System.out;
    private UtsukushiiProperties properties;
    private long startTime;

    private ConsolePlotterActor(UtsukushiiProperties properties) {
        super("ConsolePlotterActor");
        this.properties = properties;
    }

    public static Props props (UtsukushiiProperties properties) {
        return Props.create ( ConsolePlotterActor.class, ()->new ConsolePlotterActor(properties));
    }

    public static Subscriptions subscriptions = Subscriptions
            .forClass(VelocityMessage.class)
            .andForClass(SensorEvent.class)
            .andForClass(TrackSectionStart.class);


    @Override
    protected void onReceive2(Object message) throws Exception {

        if ( message instanceof SensorEvent ) {
            handleSensorEvent ( (SensorEvent) message );

        } else if ( message instanceof VelocityMessage ) {
            handleVelocityMessage ( (VelocityMessage) message );

        } else if ( message instanceof TrackSectionStart) {
            handleTrackSectionType ( (TrackSectionStart) message );
        }
    }

    private void handleTrackSectionType ( TrackSectionStart sectionStart ) {
        out.println ( "New section: " + sectionStart.toString());
    }

    private void handleVelocityMessage(VelocityMessage velocityMessage) {
        if ( !properties.isPlotSensor()) return;
        out.println (String.format("At %d: Velocity : %3.0f", velocityMessage.getT(), velocityMessage.getVelocity()));
    }

    private void handleSensorEvent(SensorEvent event) {
        plotGyroZ ( event );
    }

    private void plotGyroZ ( SensorEvent event ) {

        if ( !properties.isPlotSensor()) return;
        int t;
        if ( event.getT() == 0 ) {
            if ( startTime == 0 ) startTime = event.getTimeStamp();
            t = (int)(event.getTimeStamp()-startTime);
        } else {
            t = event.getT();
        }
        consolePlot(t, event.getG()[2]);
    }

    private void consolePlot(int t, int val) {
        String prefix = String.format("%07d: ", t);
        int scale = 120 * (val - (-10000) ) / 20000;
        out.println(prefix + StringUtils.repeat(" ", scale) + val);
    }


}
