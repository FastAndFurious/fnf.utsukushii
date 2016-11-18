package com.zuehlke.fnf.actorbus;

public interface MessageMatcher {

    public boolean matches(MessageEvent event);

    Class<?> getMessageClass();

    String getSender();
}
