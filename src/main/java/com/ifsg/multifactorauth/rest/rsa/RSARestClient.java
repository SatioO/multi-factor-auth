package com.ifsg.multifactorauth.rest.rsa;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class RSARestClient {
    private WebClient client = WebClient.create("http://10.1.7.214:8080");

    public void getAuthToken() {
        client.post().uri("/authn/auth");
    }
}
