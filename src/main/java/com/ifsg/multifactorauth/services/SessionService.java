package com.ifsg.multifactorauth.services;

import com.ifsg.multifactorauth.entities.AuthSessionEntity;
import com.ifsg.multifactorauth.models.dtos.CreateSessionDTO;
import com.ifsg.multifactorauth.models.dtos.ValidateSessionDTO;

import java.util.UUID;

public interface SessionService {
    AuthSessionEntity createSession(CreateSessionDTO body);

    Boolean validateSession(ValidateSessionDTO body);

    AuthSessionEntity getSessionById(UUID sessionId);
}
