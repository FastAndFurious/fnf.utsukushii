package com.zuehlke.fnf.actorbus.logging;

import com.zuehlke.fnf.util.DisplayableProperties;
import lombok.Data;

@Data
public class LoggingReceiverProperties extends DisplayableProperties {
    private int publishEveryAfter; // in ms

    public LoggingReceiverProperties() {
        super("Logging");
    }
}
