package com.zuehlke.fnf.utsukushii;

import com.zuehlke.fnf.actorbus.logging.LoggingReceiverProperties;
import com.zuehlke.fnf.util.DisplayableProperties;
import com.zuehlke.fnf.utsukushii.constantpower.ConstantPowerProperties;
import com.zuehlke.fnf.utsukushii.model.TrackDetectionProperties;
import com.zuehlke.fnf.utsukushii.model.TrackModelActorProperties;
import com.zuehlke.fnf.utsukushii.replay.ReplayProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Simulator Settings
 */
@Getter @Setter
@ConfigurationProperties(prefix="utsukushii") // loaded from /resources/application.yml
public class UtsukushiiProperties extends DisplayableProperties {
    private String id;
    private String relayUrl;
    private String rabbitUrl;
    private String mongoUrl;
    private String mongoDb;
    private String name;
    private String accessCode;
    private ConstantPowerProperties constantPowerProperties;
    private TrackDetectionProperties trackDetectionProperties;
    private TrackModelActorProperties trackModelActorProperties;
    private LoggingReceiverProperties loggingReceiverProperties;
    private ReplayProperties replayProperties;

    public UtsukushiiProperties(){
        super("Basic Properties");
    }

}