package com.mobi.clearsafe.net;

import java.lang.reflect.Field;

import okhttp3.HttpUrl;

public class HttpUrlHelper {
    private static final Field hostField;
    private static final Field schemeField;

    static {
        hostField = getField("host");
        schemeField = getField("scheme");
    }

    private static Field getField(String fieldStr) {
        Field field = null;
        try {
            field = HttpUrl.class.getDeclaredField(fieldStr);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return field;
    }

    private HttpUrl httpUrl;

    public HttpUrlHelper(HttpUrl httpUrl) {
        this.httpUrl = httpUrl;
    }

    public HttpUrl getHttpUrl() {
        return httpUrl;
    }

    public void setHost(String host) {
        try {
            hostField.set(httpUrl, host);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setSchemeField(String scheme) {
        try {
            schemeField.set(httpUrl, scheme);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
