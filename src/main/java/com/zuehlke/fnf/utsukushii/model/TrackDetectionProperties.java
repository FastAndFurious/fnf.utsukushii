package com.zuehlke.fnf.utsukushii.model;

import com.zuehlke.fnf.util.DisplayableProperties;
import lombok.Data;

@Data
public class TrackDetectionProperties extends DisplayableProperties {

    private int primaryFilterSize;
    private int upperStraightBoundary;
    private int lowerStraightBoundary;
    private int triggerSize;
    private double gradientThreshold;
    private double kickFactor;

    public TrackDetectionProperties() {
        super("Track Detection");
    }
}
