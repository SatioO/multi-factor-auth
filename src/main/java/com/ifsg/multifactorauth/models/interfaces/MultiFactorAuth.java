package com.ifsg.multifactorauth.models.interfaces;

import com.ifsg.multifactorauth.entities.AuthSessionEntity;
import com.ifsg.multifactorauth.models.dtos.CreateSessionDTO;
import com.ifsg.multifactorauth.models.dtos.SessionDTO;
import com.ifsg.multifactorauth.models.dtos.ValidateSessionDTO;

public interface MultiFactorAuth {
    SessionDTO createSession(CreateSessionDTO data);

    Boolean validateSession(AuthSessionEntity entity, ValidateSessionDTO body);
}
