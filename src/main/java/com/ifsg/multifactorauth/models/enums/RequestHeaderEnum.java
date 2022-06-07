package com.ifsg.multifactorauth.models.enums;

public enum RequestHeaderEnum {
    GLOBALUUID("globaluuid"),
    REQUESTUUID("requestuuid"),
    CHANNEL("channel");

    public final String value;

    private RequestHeaderEnum(String value) {
        this.value = value;
    }
}
