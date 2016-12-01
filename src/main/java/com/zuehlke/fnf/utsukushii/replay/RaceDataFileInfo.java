package com.zuehlke.fnf.utsukushii.replay;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RaceDataFileInfo {

    public enum Status {
        OK,
        CORRUPT
    }

    private String fileName;
    private String teamName;
    private String trackName;
    private String raceType;
    private Status status;
    private int numSensors;
    private int duration;
    private long startTime;
}
