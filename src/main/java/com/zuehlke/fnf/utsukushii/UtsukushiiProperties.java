package com.zuehlke.fnf.utsukushii;

import com.zuehlke.fnf.actorbus.logging.LoggingReceiverProperties;
import com.zuehlke.fnf.utsukushii.constantpower.ConstantPowerProperties;
import com.zuehlke.fnf.utsukushii.model.TrackDetectionProperties;
import com.zuehlke.fnf.utsukushii.model.TrackModelActorProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Simulator Settings
 */
@Data
@ConfigurationProperties(prefix="utsukushii") // loaded from /resources/application.yml
public class UtsukushiiProperties {
    private String relayUrl;
    private String rabbitUrl;
    private String name;
    private String accessCode;
    private ConstantPowerProperties constantPowerProperties;
    private TrackDetectionProperties trackDetectionProperties;
    private TrackModelActorProperties trackModelActorProperties;
    private LoggingReceiverProperties loggingReceiverProperties;
}