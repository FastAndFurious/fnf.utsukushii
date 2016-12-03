package com.zuehlke.fnf.utsukushii.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private WebSocketHandler logReportHandler = new WebSocketHandler();
    private WebSocketHandler replayStatusHandler = new WebSocketHandler();

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {

        webSocketHandlerRegistry.addHandler (logReportHandler, "/ws/logreports");
        webSocketHandlerRegistry.addHandler (replayStatusHandler, "/ws/replaystatus");
    }

    @Bean
    public WebSocketHandler logReportHandler () {
        return logReportHandler;
    }

    @Bean
    public WebSocketHandler replayStatusHandler () { return replayStatusHandler; }
}