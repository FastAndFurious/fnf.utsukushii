package com.zuehlke.fnf.utsukushii.scenario;

import com.zuehlke.carrera.javapilot.services.PilotToRelayConnection;
import com.zuehlke.carrera.relayapi.messages.*;
import com.zuehlke.fnf.actorbus.ActorBus;
import com.zuehlke.fnf.actorbus.ActorBusConfiguration;
import com.zuehlke.fnf.actorbus.logging.LoggingReceiverProperties;
import com.zuehlke.fnf.connect.PilotToRelayConnectionFactory;
import com.zuehlke.fnf.mongo.DocumentStore;
import com.zuehlke.fnf.utsukushii.UtsukushiiProperties;
import com.zuehlke.fnf.utsukushii.bootstrap.UtsukushiiBootStrapper;
import com.zuehlke.fnf.utsukushii.constantpower.ConstantPowerProperties;
import com.zuehlke.fnf.utsukushii.model.TrackDetectionProperties;
import com.zuehlke.fnf.utsukushii.model.TrackModelActorProperties;
import com.zuehlke.fnf.utsukushii.replay.ReplayProperties;
import com.zuehlke.fnf.utsukushii.replay.ReplayRestResource;
import com.zuehlke.fnf.utsukushii.web.WebSocketConfig;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Configuration
@Import({ActorBusConfiguration.class, WebSocketConfig.class})
@ComponentScan(basePackageClasses = {UtsukushiiBootStrapper.class})
public class ScenarioTestContext {

    @Bean
    public ReplayRestResource replayRestResource (ActorBus bus ) throws Exception {
        return new ReplayRestResource( bus);
    }


    @Bean
    public UtsukushiiProperties properties () {
        UtsukushiiProperties properties = new UtsukushiiProperties();
        properties.setLoggingReceiverProperties(new LoggingReceiverProperties());
        properties.setConstantPowerProperties(new ConstantPowerProperties());
        properties.setReplayProperties(new ReplayProperties());
        properties.setTrackDetectionProperties(new TrackDetectionProperties());
        properties.setTrackModelActorProperties(new TrackModelActorProperties());
        properties.setPlotSensor(false);
        properties.getLoggingReceiverProperties().setPublishEveryAfter(500);
        return properties;
    }

    @Bean @Primary
    public DocumentStore<UtsukushiiProperties> testPropertyStore () {
        return new DocumentStore<UtsukushiiProperties>() {

            UtsukushiiProperties props;
            @Override
            public void store(UtsukushiiProperties props) {
                this.props = props;
            }

            @Override
            public List<UtsukushiiProperties> retrieve(Query query) {
                return Collections.singletonList(props);
            }

            @Override
            public UtsukushiiProperties retrieve(String id) {
                return props;
            }

            @Override
            public void clearAll() {
                throw new UnsupportedOperationException("Not supported here.");
            }

            @Override
            public void delete(Query query) {
                throw new UnsupportedOperationException("Not supported here.");
            }
        };

    }
    @Bean @Primary
    public PilotToRelayConnectionFactory connectionFactory () {
        return new PilotToRelayConnectionFactory () {
            @Override
            public PilotToRelayConnection create(
                    Consumer<RaceStartMessage> onRaceStart,
                    Consumer<RaceStopMessage> onRaceStop,
                    Consumer<SensorEvent> onSensor,
                    Consumer<VelocityMessage> onVelocity,
                    Consumer<PenaltyMessage> onPenalty,
                    Consumer<RoundTimeMessage> roundPassed) {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }
        };
    }
}
