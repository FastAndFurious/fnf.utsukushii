package com.zuehlke.fnf.utsukushii.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    private final String commonName;
    private WebSocketSession session;

    public WebSocketHandler ( String commonName ) {
        this.commonName = commonName;
    }

    private String prefix () {

        return "Handler '" + commonName + "': ";
    }

    @Override
    public void handleTextMessage (WebSocketSession session, TextMessage message ) {
            log.warn(prefix() + "Not expecting any messages here (for the time being)");
            log.warn("Got: " + message.getPayload());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        this.session = session;
        log.debug (prefix() + "Session established.");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.debug (prefix() + "Session closed. Reason: " + status.getReason());
    }

    public boolean isConnected () {
        return ( session != null ) && session.isOpen();
    }

    boolean send (Object object ) {
        if ( session == null ) {
            //log.trace ( "Can't send message. No session established yet");
            return false;
        }
        if ( !session.isOpen()) {
            log.trace ( prefix() + "Cannot send message. Session was closed.");
            session = null;
            return false;
        }
        try {
            String payload = new ObjectMapper().writeValueAsString(object);
            session.sendMessage(new TextMessage(payload));
            return true;
        } catch (Exception e) {
            log.error(prefix() + "Failed to send message. Exception was: " + e.getMessage());
            session = null;
            return false;
        }
    }
}
