package com.ifsg.multifactorauth.controllers;

import com.ifsg.multifactorauth.entities.AuthSessionEntity;
import com.ifsg.multifactorauth.models.dtos.CreateSessionDTO;
import com.ifsg.multifactorauth.models.dtos.ValidateSessionDTO;
import com.ifsg.multifactorauth.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
public class SessionController {
    @Autowired
    private SessionService sessionService;

    @GetMapping("v1/session")
    public AuthSessionEntity getSessionById(UUID sessionId) {
        return this.sessionService.getSessionById(sessionId);
    }

    @PostMapping("v1/session/validate")
    public Boolean validateSession(@Valid @RequestBody ValidateSessionDTO body) {
        return this.sessionService.validateSession(body);
    }

    @PostMapping("v1/session")
    public AuthSessionEntity createSession(@Valid @RequestBody CreateSessionDTO body) {
        return this.sessionService.createSession(body);
    }
}
