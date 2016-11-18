package com.zuehlke.fnf.actorbus;

class MessageEvent {

    private final Class<?> messageClass;
    private final String sender;

    public MessageEvent(Class<?> messageClass, String sender ) {
        this.messageClass = messageClass;
        this.sender = sender;
    }

    public Class<?> getMessageClass() {
        return messageClass;
    }

    public String getSender() {
        return sender;
    }

}
