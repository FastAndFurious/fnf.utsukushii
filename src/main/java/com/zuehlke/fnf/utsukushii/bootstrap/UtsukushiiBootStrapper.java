package com.zuehlke.fnf.utsukushii.bootstrap;

import com.zuehlke.carrera.javapilot.services.PilotToRelayConnection;
import com.zuehlke.fnf.actorbus.ActorBus;
import com.zuehlke.fnf.connect.PilotToRelayConnectionFactory;
import com.zuehlke.fnf.utsukushii.UtsukushiiProperties;
import com.zuehlke.fnf.utsukushii.constantpower.ConstantPowerActor;
import com.zuehlke.fnf.utsukushii.model.TrackSectionDetectorActor;
import com.zuehlke.fnf.utsukushii.model.TrackSectionStartDetector;
import com.zuehlke.fnf.utsukushii.ops.ConsolePlotterActor;
import com.zuehlke.fnf.utsukushii.replay.ReplayActor;
import com.zuehlke.fnf.utsukushii.strategy.StrategyGatewayActor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class UtsukushiiBootStrapper {

    private final ActorBus bus;
    private final PilotToRelayConnectionFactory connectionFactory;
    private PilotToRelayConnection connection;
    private UtsukushiiProperties props;


    @Autowired
    public UtsukushiiBootStrapper(ActorBus bus, PilotToRelayConnectionFactory connectionFactory, UtsukushiiProperties props) {
        this.props = props;
        this.bus = bus;
        this.connectionFactory = connectionFactory;
    }

    @PostConstruct
    private void bootstrap() {

        connection = connectionFactory.create(
                bus::publish, bus::publish, bus::publish, bus::publish, bus::publish, bus::publish);

        bus.register("ConstantPowerActor", ConstantPowerActor.props(props.getConstantPowerProperties()),
                ConstantPowerActor.subscriptions);

        bus.register("StrategyGatewayActor", StrategyGatewayActor.props(props),
                StrategyGatewayActor.subscriptions);

        bus.register("PowerPublisherActor", PowerPublisherActor.props(connection),
                PowerPublisherActor.subscriptions);

        bus.register("ReplayActor", ReplayActor.props(),
                ReplayActor.subscriptions);

        bus.register("ConsolePlotterActor", ConsolePlotterActor.props(),
                ConsolePlotterActor.subscriptions);

        TrackSectionStartDetector detector = new TrackSectionStartDetector(props.getTrackDetectionProperties());
        bus.register("TrackSectionDetectorActor", TrackSectionDetectorActor.props(detector),
                TrackSectionDetectorActor.subscriptions);

    }


    @Scheduled(fixedRate = 1000)
    public void announce() {

        connection.announce("<unused>");
    }


}
