package com.zuehlke.fnf.util;

abstract public class DisplayableProperties {

    private String title;

    protected DisplayableProperties(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
