package com.zuehlke.fnf.utsukushii;

import com.zuehlke.fnf.actorbus.ActorBusConfiguration;
import com.zuehlke.fnf.connect.RabbitConfig;
import com.zuehlke.fnf.utsukushii.bootstrap.UtsukushiiBootStrapper;
import com.zuehlke.fnf.utsukushii.web.WebSocketConfig;
import org.springframework.boot.autoconfigure.web.ErrorViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;

@Configuration
@EnableScheduling
@Import({ActorBusConfiguration.class,
        RabbitConfig.class, WebSocketConfig.class })
@ComponentScan(basePackageClasses = { UtsukushiiBootStrapper.class})
public class UtsukushiiContext {
    @Bean
    ErrorViewResolver supportPathBasedLocationStrategyWithoutHashes() {
        return (request, status, model) -> status == HttpStatus.NOT_FOUND
                ? new ModelAndView("index.html", Collections.emptyMap(), HttpStatus.OK) : null;
    }}
