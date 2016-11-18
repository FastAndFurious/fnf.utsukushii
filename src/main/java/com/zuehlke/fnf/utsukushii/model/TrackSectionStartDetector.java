package com.zuehlke.fnf.utsukushii.model;

import com.zuehlke.fnf.util.LinRegFilter;

import java.util.Optional;

/**
 * Detects a section start from a continuous stream of gyro z values
 * WARNING NOT THREAD-SAFE!
 */
public class TrackSectionStartDetector {

    private final LinRegFilter filter;
    private final ThresholdTrigger trigger;


    TrackSectionStartDetector(int primaryFilterSize, int upperStraightBoundary, int lowerStraightBoundary,
                                     int triggerSize, double gradientThreshold, double kickFactor) {
        this.filter = new LinRegFilter(primaryFilterSize);
        this.trigger = new ThresholdTrigger(upperStraightBoundary, lowerStraightBoundary, triggerSize, gradientThreshold, kickFactor);
    }

    public TrackSectionStartDetector(TrackDetectionProperties props) {
        this(props.getPrimaryFilterSize(), props.getUpperStraightBoundary(), props.getLowerStraightBoundary(),
                props.getTriggerSize(), props.getGradientThreshold(), props.getKickFactor());
    }


    Optional<TrackSectionType> putAndDetect(int t, int gyrz ) {

        filter.put(t, gyrz);
        return filter.current().map((v)->trigger.putAndDetect(v.getValue(), v.getGradient())).orElse(Optional.empty());
    }
}
