package com.zuehlke.fnf.utsukushii.strategy;

import akka.actor.Props;
import com.zuehlke.carrera.relayapi.messages.PowerControl;
import com.zuehlke.fnf.actorbus.ActorBusActor;
import com.zuehlke.fnf.actorbus.Subscriptions;
import com.zuehlke.fnf.utsukushii.UtsukushiiProperties;

public class StrategyGatewayActor extends ActorBusActor {

    private RecommendationStrategy currentStrategy = RecommendationStrategy.CONSTANT_POWER;
    private UtsukushiiProperties properties;

    public static Subscriptions subscriptions = Subscriptions
            .forClass(PowerRecommendation.class)
            .andForClass(RecommendationStrategy.class);

    public static Props props (UtsukushiiProperties properties) {
        return Props.create ( StrategyGatewayActor.class, ()->new StrategyGatewayActor(properties));
    }

    private StrategyGatewayActor(UtsukushiiProperties properties) {
        super("StrategyGatewayActor");
        this.properties = properties;
    }

    @Override
    protected void onReceive2(Object message) throws Exception {

        if ( message instanceof RecommendationStrategy ) {
            currentStrategy = (RecommendationStrategy) message;

        } else if ( message instanceof PowerRecommendation ) {
            handlePowerRecommendation ((PowerRecommendation) message);
        }
    }


    private void handlePowerRecommendation ( PowerRecommendation recommendation ) {
        if ( recommendation.getStrategy() == currentStrategy ) {
            publish(new PowerControl(recommendation.getPower(), properties.getName(), properties.getAccessCode(),
                    System.currentTimeMillis()));
        }
    }
}
