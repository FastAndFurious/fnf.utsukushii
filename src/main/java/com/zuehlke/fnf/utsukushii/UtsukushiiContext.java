package com.zuehlke.fnf.utsukushii;

import com.zuehlke.fnf.actorbus.ActorBusConfiguration;
import com.zuehlke.fnf.connect.RabbitConfig;
import com.zuehlke.fnf.utsukushii.bootstrap.UtsukushiiBootStrapper;
import com.zuehlke.fnf.utsukushii.temporary.CounterHandler;
import com.zuehlke.fnf.utsukushii.web.WebSocketConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@Import({ActorBusConfiguration.class,
        RabbitConfig.class, WebSocketConfig.class })
@ComponentScan(basePackageClasses = { UtsukushiiBootStrapper.class})
public class UtsukushiiContext {
}
