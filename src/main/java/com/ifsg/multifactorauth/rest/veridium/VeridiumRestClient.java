package com.ifsg.multifactorauth.rest.veridium;

import com.ifsg.multifactorauth.rest.veridium.models.VeridiumSession;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class VeridiumRestClient {
    private WebClient client = WebClient.create("https://veridiumdev.ifsgdev.io");

    public Flux<VeridiumSession> getSession() {
        return client.post().uri("/websec/rest/enterprise/AuthenticationRequest").retrieve().bodyToFlux(VeridiumSession.class);
    }
}
