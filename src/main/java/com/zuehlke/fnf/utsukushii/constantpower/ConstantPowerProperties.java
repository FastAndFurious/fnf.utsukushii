package com.zuehlke.fnf.utsukushii.constantpower;

import com.zuehlke.fnf.util.DisplayableProperties;
import lombok.Data;

@Data
public class ConstantPowerProperties extends DisplayableProperties {

    private float frequencyInHertz;
    private String teamId;
    private String accessCode;

    public ConstantPowerProperties() {
        super("Constant Power Strategy");
    }
}
