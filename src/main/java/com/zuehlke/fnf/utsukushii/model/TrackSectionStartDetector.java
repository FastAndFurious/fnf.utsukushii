package com.zuehlke.fnf.utsukushii.model;

import com.zuehlke.fnf.util.LinRegFilter;

import java.util.Optional;

/**
 * Detects a section start from a continuous stream of gyro z values
 * Concept: a linreg filter provides current smoothed value and gradient. Those two values are then fed into a trigger
 * that decides whether the current values indicate entry into a new track section
 *
 * WARNING: NOT THREAD-SAFE! For use within an actor only!
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


    Optional<TrackSectionStart> putAndDetect(int t, int gyrz ) {

        filter.put(t, gyrz);
        return filter.current().map((v)->trigger.putAndDetect(v.getValue(), v.getGradient(), t)).orElse(Optional.empty());
    }
}
