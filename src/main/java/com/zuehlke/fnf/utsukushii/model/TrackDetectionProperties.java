package com.zuehlke.fnf.utsukushii.model;

import lombok.Data;

@Data
public class TrackDetectionProperties {

    private int primaryFilterSize;
    private int upperStraightBoundary;
    private int lowerStraightBoundary;
    private int triggerSize;
    private double gradientThreshold;
    private double kickFactor;
}
