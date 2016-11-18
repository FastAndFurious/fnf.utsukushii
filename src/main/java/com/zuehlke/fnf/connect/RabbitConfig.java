package com.zuehlke.fnf.connect;

import com.zuehlke.carrera.api.DirectExchangePilotApiImpl;
import com.zuehlke.carrera.api.PilotApi;
import com.zuehlke.carrera.api.channel.PilotToRelayChannelNames;
import com.zuehlke.carrera.api.channel.RoutingKeyNames;
import com.zuehlke.carrera.api.client.rabbit.RabbitClient;
import com.zuehlke.carrera.api.seralize.JacksonSerializer;
import com.zuehlke.fnf.utsukushii.UtsukushiiProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    UtsukushiiProperties properties () {
        return new UtsukushiiProperties();
    }

    @Bean
    PilotToRelayConnectionFactory connectionFactory(PilotApi pilotApi, UtsukushiiProperties properties) {
        return new RabbitPilotToRelayConnectionFactory(pilotApi, properties);
    }

    @Bean
    PilotApi pilotApi(UtsukushiiProperties properties) {
        return new DirectExchangePilotApiImpl(new RabbitClient(), new PilotToRelayChannelNames(properties.getName()),
                new RoutingKeyNames(properties.getName()), new JacksonSerializer());
    }
}
