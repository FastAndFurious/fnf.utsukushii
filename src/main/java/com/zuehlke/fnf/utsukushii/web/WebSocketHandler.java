package com.zuehlke.fnf.utsukushii.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuehlke.fnf.actorbus.logging.LogReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    private WebSocketSession session;

    @Override
    public void handleTextMessage (WebSocketSession session, TextMessage message ) {
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("At last: {}", message.getPayload());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        this.session = session;
        log.info ("Session established.");
    }


    public void send (LogReport logReport ) {
        if ( session == null ) {
            log.debug ( "Can't send log report. No session established yet");
            return;
        }
        try {
            String payload = new ObjectMapper().writeValueAsString(logReport);
            session.sendMessage(new TextMessage(payload));
        } catch (Exception e) {
            log.error("Failed to send log report. Exception was: " + e.getMessage());
        }
    }
}
