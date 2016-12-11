package com.zuehlke.fnf.utsukushii.model;

import com.zuehlke.fnf.util.ValueAndGradient;

import java.util.Optional;

/**
 * recognizes curves by subsequent exceeding of the given threshold.
 * WARNING: NOT THREAD-SAFE. Use only within an actor context
 */
class ThresholdTrigger {


    private final int upperStraightBoundary;
    private final int lowerStraightBoundary;
    private final int triggerSize;
    private final double gradientThreshold;
    private final double kickFactor;
    private int above;
    private int below;
    private int middle;
    private TrackSectionType currentSection = null;

    ThresholdTrigger(int upperStraightBoundary, int lowerStraightBoundary, int triggerSize,
                            double gradientThreshold, double kickFactor  ) {
        this.upperStraightBoundary = upperStraightBoundary;
        this.lowerStraightBoundary = lowerStraightBoundary;
        this.triggerSize = triggerSize;
        this.gradientThreshold = gradientThreshold;
        this.kickFactor = kickFactor;
    }


    Optional<TrackSectionStart> putAndDetect ( double value, double gradient, long timestamp ) {

        if ( currentSection != TrackSectionType.RIGHT_CURVE ) {
            if ( value > upperStraightBoundary ) {
                above++;
            }
            if ( value > kickFactor * upperStraightBoundary || above == triggerSize ) {
                currentSection = TrackSectionType.RIGHT_CURVE;
                below = 0;
                middle = 0;
                return Optional.of(new TrackSectionStart(currentSection, new ValueAndGradient(value, gradient), timestamp));
            }
        }

        if ( currentSection != TrackSectionType.LEFT_CURVE ) {
            if ( value < lowerStraightBoundary ) {
                below++;
            }
            if ( value < kickFactor * lowerStraightBoundary || below == triggerSize ) {
                currentSection = TrackSectionType.LEFT_CURVE;
                above = 0;
                middle = 0;
                return Optional.of(new TrackSectionStart(currentSection, new ValueAndGradient(value, gradient), timestamp));
            }
        }

        if ( currentSection != TrackSectionType.STRAIGHT ) {
            if ( value <= upperStraightBoundary && value >= lowerStraightBoundary) {
                /*
                 * taking into account that a large gradient may indicate an L-R or R-L change, during which the
                 * lower gyro-z absolute values do not actually indicate a straight.
                 */
                if ( Math.abs(gradient) < gradientThreshold ) {
                    middle++;
                }
            }
            if ( middle == triggerSize ) {
                currentSection = TrackSectionType.STRAIGHT;
                below = 0;
                above = 0;
                return Optional.of(new TrackSectionStart(currentSection, new ValueAndGradient(value, gradient), timestamp));
            }
        }
        return Optional.empty();
    }

}
