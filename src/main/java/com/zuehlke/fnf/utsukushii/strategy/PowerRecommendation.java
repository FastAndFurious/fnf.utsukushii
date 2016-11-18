package com.zuehlke.fnf.utsukushii.strategy;

/**
 *  A certain TopoFunction may recommend power for straights or curves
 */
public class PowerRecommendation {

    private final int power;
    private final RecommendationStrategy function;
    private final SituationalContext context;

    public PowerRecommendation(RecommendationStrategy function, int power, SituationalContext context ) {
        this.power = power;
        this.function = function;
        this.context = context;
    }

    public RecommendationStrategy getStrategy() {
        return function;
    }

    public int getPower() {
        return power;
    }

    public SituationalContext getContext() {
        return context;
    }
}
