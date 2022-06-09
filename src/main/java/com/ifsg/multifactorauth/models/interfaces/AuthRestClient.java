package com.ifsg.multifactorauth.models.interfaces;

public interface AuthRestClient {
    String getSession();
    String verifySession();
}
