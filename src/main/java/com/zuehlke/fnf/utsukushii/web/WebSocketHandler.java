package com.zuehlke.fnf.utsukushii.web;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        log.debug ("Session established.");
    }


    void send (Object object ) {
        if ( session == null ) {
            log.trace ( "Can't send log report. No session established yet");
            return;
        }
        if ( !session.isOpen()) {
            log.trace ( "Cannot send log report. Session was closed.");
            session = null;
            return;
        }
        try {
            String payload = new ObjectMapper().writeValueAsString(object);
            session.sendMessage(new TextMessage(payload));
        } catch (Exception e) {
            log.error("Failed to send message. Exception was: " + e.getMessage());
            session = null;
        }
    }
}
