package com.zuehlke.fnf.actorbus;

public class SingleClassMatcher implements MessageMatcher {

    private Class<?> clazz;

    public SingleClassMatcher ( Class<?> clazz ) {
        this.clazz = clazz;
    }

    @Override
    public boolean matches(MessageEvent event) {
        return clazz.isAssignableFrom(event.getMessageClass());
    }

    @Override
    public Class<?> getMessageClass() {
        return clazz;
    }

    @Override
    public String getSender() {
        return "";
    }
}
