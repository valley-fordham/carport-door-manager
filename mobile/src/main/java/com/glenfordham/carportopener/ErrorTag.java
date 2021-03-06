package com.glenfordham.carportopener;

public enum ErrorTag {
    BACKGROUND("BackgroundService"),
    HOMESCREEN("HomeScreen"),
    THREAD_PROTECT("ThreadProtection");

    private String stringVal;

    ErrorTag(String stringValue) {
        this.stringVal = stringValue;
    }

    public String get() {
        return stringVal;
    }
}
