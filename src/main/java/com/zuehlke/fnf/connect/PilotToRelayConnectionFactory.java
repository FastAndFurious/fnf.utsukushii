package com.zuehlke.fnf.connect;

import com.zuehlke.carrera.javapilot.services.PilotToRelayConnection;
import com.zuehlke.carrera.relayapi.messages.*;

import java.util.function.Consumer;

public interface PilotToRelayConnectionFactory {

    PilotToRelayConnection create(Consumer<RaceStartMessage> onRaceStart,
                                  Consumer<RaceStopMessage> onRaceStop,
                                  Consumer<SensorEvent> onSensor,
                                  Consumer<VelocityMessage> onVelocity,
                                  Consumer<PenaltyMessage> onPenalty,
                                  Consumer<RoundTimeMessage> roundPassed);

    String getDescription();
}
