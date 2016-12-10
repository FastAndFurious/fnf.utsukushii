package com.zuehlke.fnf.utsukushii.replay;

import com.zuehlke.fnf.util.DisplayableProperties;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReplayProperties extends DisplayableProperties {

    private float frequency;

    public ReplayProperties() {
        super("Replay");
    }
}
