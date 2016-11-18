package com.zuehlke.fnf.utsukushii;

import com.zuehlke.fnf.utsukushii.constantpower.ConstantPowerProperties;
import com.zuehlke.fnf.utsukushii.model.TrackDetectionProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Simulator Settings
 */
@Data
@ConfigurationProperties(prefix="javapilot") // loaded from /resources/application.yml
public class UtsukushiiProperties {
    private String relayUrl;
    private String rabbitUrl;
    private String name;
    private String accessCode;
    private ConstantPowerProperties constantPowerProperties;
    private TrackDetectionProperties trackDetectionProperties;

}