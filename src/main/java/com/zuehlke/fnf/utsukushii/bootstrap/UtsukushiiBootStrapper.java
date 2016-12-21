package com.zuehlke.fnf.utsukushii.bootstrap;

import com.zuehlke.carrera.javapilot.services.PilotToRelayConnection;
import com.zuehlke.fnf.actorbus.ActorBus;
import com.zuehlke.fnf.connect.PilotToRelayConnectionFactory;
import com.zuehlke.fnf.mongo.DocumentStore;
import com.zuehlke.fnf.utsukushii.UtsukushiiProperties;
import com.zuehlke.fnf.utsukushii.constantpower.ConstantPowerActor;
import com.zuehlke.fnf.utsukushii.model.TrackModelCreatorActor;
import com.zuehlke.fnf.utsukushii.model.TrackSectionDetectorActor;
import com.zuehlke.fnf.utsukushii.model.TrackSectionStartDetector;
import com.zuehlke.fnf.utsukushii.ops.ConsolePlotterActor;
import com.zuehlke.fnf.utsukushii.replay.ReplayActor;
import com.zuehlke.fnf.utsukushii.strategy.StrategyGatewayActor;
import com.zuehlke.fnf.utsukushii.web.WebSocketPublisherActor;
import com.zuehlke.fnf.utsukushii.web.WebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class UtsukushiiBootStrapper {

    private final ActorBus bus;
    private final PilotToRelayConnectionFactory connectionFactory;
    private final WebSocketHandler replayStatusHandler;
    private final WebSocketHandler logReportHandler;
    private final WebSocketHandler usageStatsHandler;
    private PilotToRelayConnection connection;
    private UtsukushiiProperties defaultProps;
    private DocumentStore<UtsukushiiProperties> paramStore;

    @Autowired
    public UtsukushiiBootStrapper(ActorBus bus, PilotToRelayConnectionFactory connectionFactory,
                                  UtsukushiiProperties props, DocumentStore<UtsukushiiProperties> paramStore,
                                  @Qualifier("logReportHandler") WebSocketHandler logReportHandler,
                                  @Qualifier("replayStatusHandler") WebSocketHandler replayStatusHandler,
                                  @Qualifier("usageStatsHandler") WebSocketHandler usageStatsHandler ) {
        this.defaultProps = props;
        this.bus = bus;
        this.paramStore = paramStore;
        this.connectionFactory = connectionFactory;
        this.replayStatusHandler = replayStatusHandler;
        this.logReportHandler = logReportHandler;
        this.usageStatsHandler = usageStatsHandler;
    }

    @PostConstruct
    private void bootstrap() {

        UtsukushiiProperties props = determineProperties ();

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

        bus.register("ConsolePlotterActor", ConsolePlotterActor.props(props),
                ConsolePlotterActor.subscriptions);

        TrackSectionStartDetector detector = new TrackSectionStartDetector(props.getTrackDetectionProperties());
        bus.register("TrackSectionDetectorActor", TrackSectionDetectorActor.props(detector),
                TrackSectionDetectorActor.subscriptions);

        bus.register("TrackModelActor", TrackModelCreatorActor.props(props.getTrackModelActorProperties()),
                TrackModelCreatorActor.subscriptions);

        bus.register("WebsocketPublisherActor",
                WebSocketPublisherActor.props(logReportHandler, replayStatusHandler, usageStatsHandler),
                WebSocketPublisherActor.subscriptions);

        bus.register("ProbingActor", ProbingActor.props(),
                ProbingActor.subscriptions);
    }

    private UtsukushiiProperties determineProperties() {
        UtsukushiiProperties props = paramStore.retrieve(defaultProps.getId());
        if ( props == null ) {
            log.warn("No property set in DB. Storing default properties");
            paramStore.store(defaultProps);
            props = defaultProps;
        } else {
            log.info("Using properties from db {} at {}", defaultProps.getMongoDb(), defaultProps.getMongoUrl());
        }
        return props;
    }


    @Scheduled(fixedRate = 1000)
    public void announce() {

        connection.announce("<unused>");
    }


}
