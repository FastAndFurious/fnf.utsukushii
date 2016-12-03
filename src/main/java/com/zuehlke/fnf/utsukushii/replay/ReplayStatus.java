package com.zuehlke.fnf.utsukushii.replay;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReplayStatus {

    public static final ReplayStatus OFF = new ReplayStatus(Status.OFF, null, 0);

    public enum Status {
        OFF,
        PLAYING,
        SUSPENDED
    }

    private Status status;
    private String fileName;
    private long milliesSinceStart;
}
