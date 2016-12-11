package com.zuehlke.fnf.connect;

import com.zuehlke.carrera.api.PilotApi;
import com.zuehlke.carrera.javapilot.services.PilotToRelayConnection;
import com.zuehlke.carrera.relayapi.messages.*;
import com.zuehlke.fnf.utsukushii.UtsukushiiProperties;

import java.util.function.Consumer;

public class RabbitPilotToRelayConnectionFactory implements PilotToRelayConnectionFactory {
    private final PilotApi pilotApi;
    private final UtsukushiiProperties settings;

    RabbitPilotToRelayConnectionFactory(PilotApi pilotApi, UtsukushiiProperties properties) {
        this.pilotApi = pilotApi;
        this.settings = properties;
    }

    @Override
    public PilotToRelayConnection create(Consumer<RaceStartMessage> onRaceStart,
                                         Consumer<RaceStopMessage> onRaceStop,
                                         Consumer<SensorEvent> onSensor,
                                         Consumer<VelocityMessage> onVelocity,
                                         Consumer<PenaltyMessage> onPenalty,
                                         Consumer<RoundTimeMessage> roundPassed) {
        PilotToRelayConnection connection = new PilotToRelayConnection() {
            @Override
            public void announce(String optionalUrl) {
                PilotLifeSign lifeSign = new PilotLifeSign(settings.getName(),
                        settings.getAccessCode(), optionalUrl, System.currentTimeMillis());
                pilotApi.announce(lifeSign);
            }

            @Override
            public void send(PowerControl powerControl) {
                pilotApi.powerControl(powerControl);
            }

            @Override
            public void ensureConnection() {
                pilotApi.connect(settings.getRabbitUrl());
            }
        };
        pilotApi.connect(settings.getRabbitUrl());
        pilotApi.onRaceStart(onRaceStart);
        pilotApi.onRaceStop(onRaceStop);
        pilotApi.onSensor(onSensor);
        pilotApi.onVelocity(onVelocity);
        pilotApi.onPenalty(onPenalty);
        pilotApi.onRoundPassed(roundPassed);
        return connection;
    }

    public String getDescription () {
        return "Rabbit AMQP on " + settings.getRabbitUrl();
    }
}
