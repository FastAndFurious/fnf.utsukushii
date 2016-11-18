package com.zuehlke.fnf.utsukushii.model;

import java.util.Optional;

/**
 * recognizes curves by subsequent exceeding of the given threshold.
 * WARNING: NOT THREAD-SAFE
 */
public class ThresholdTrigger {


    private final int upperStraightBoundary;
    private final int lowerStraightBoundary;
    private final int triggerSize;
    private final double gradientThreshold;
    private final double kickFactor;
    private int above;
    private int below;
    private int middle;
    private TrackSectionType currentSection = null;

    public ThresholdTrigger(int upperStraightBoundary, int lowerStraightBoundary, int triggerSize,
                            double gradientThreshold, double kickFactor  ) {
        this.upperStraightBoundary = upperStraightBoundary;
        this.lowerStraightBoundary = lowerStraightBoundary;
        this.triggerSize = triggerSize;
        this.gradientThreshold = gradientThreshold;
        this.kickFactor = kickFactor;
    }


    public Optional<TrackSectionType> putAndDetect ( double value, double gradient ) {

        if ( currentSection != TrackSectionType.RIGHT_CURVE ) {
            if ( value > upperStraightBoundary ) {
                above++;
            }
            if ( value > kickFactor * upperStraightBoundary || above == triggerSize ) {
                currentSection = TrackSectionType.RIGHT_CURVE;
                below = 0;
                middle = 0;
                return Optional.of(currentSection);
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
                return Optional.of(currentSection);
            }
        }

        if ( currentSection != TrackSectionType.STRAIGHT ) {
            if ( value <= upperStraightBoundary && value >= lowerStraightBoundary) {
                /**
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
                return Optional.of(currentSection);
            }
        }
        return Optional.empty();
    }


    public Optional<TrackSectionType> putAndDetect2 ( double value, double gradient ) {

        if ( value > upperStraightBoundary ) {
            above++;
        } else if ( value < lowerStraightBoundary ) {
            below++;
        } else {
            /**
             * taking into account that a large gradient may indicate an L-R or R-L combination, where the lower gyro-z
             * values during the change do not actually indicate a straight.
             */
            if ( Math.abs(gradient) < gradientThreshold ) {
                middle++;
            }
        }

        if ( above == triggerSize ) {
            below = 0;
            middle = 0;
            currentSection = TrackSectionType.RIGHT_CURVE;
            return Optional.of(currentSection);
        }

        if ( below == triggerSize ) {
            above = 0;
            middle = 0;
            currentSection = TrackSectionType.LEFT_CURVE;
            return Optional.of(currentSection);
        }

        if ( middle == triggerSize ) {
            above = 0;
            below = 0;
            /** special corner case: if after the entry into the straight the gradient goes beyond the threshold,
             * a double section detection may occur */
            if ( currentSection == TrackSectionType.STRAIGHT ) {
                return Optional.empty();
            }
            currentSection = TrackSectionType.STRAIGHT;
            return Optional.of(currentSection);
        }

        return Optional.empty();
    }

    public Optional<TrackSectionType> current () {
        return Optional.ofNullable(currentSection);
    }
}
