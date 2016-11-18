package com.zuehlke.fnf.utsukushii;

import com.zuehlke.fnf.actorbus.ActorBusConfiguration;
import com.zuehlke.fnf.connect.RabbitConfig;
import com.zuehlke.fnf.utsukushii.bootstrap.UtsukushiiBootStrapper;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ActorBusConfiguration.class, RabbitConfig.class})
@ComponentScan(basePackageClasses = UtsukushiiBootStrapper.class)
public class UtsukushiiContext {



}
