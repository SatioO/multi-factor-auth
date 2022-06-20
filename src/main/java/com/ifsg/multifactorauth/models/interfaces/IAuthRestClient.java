package com.ifsg.multifactorauth.models.interfaces;

public interface IAuthRestClient {
    String getSession();
    String verifySession();
}
