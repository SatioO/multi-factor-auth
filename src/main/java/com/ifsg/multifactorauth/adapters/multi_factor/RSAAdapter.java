package com.ifsg.multifactorauth.adapters.multi_factor;

import com.ifsg.multifactorauth.entities.AuthSessionEntity;
import com.ifsg.multifactorauth.models.dtos.CreateSessionDTO;
import com.ifsg.multifactorauth.models.dtos.SessionDTO;
import com.ifsg.multifactorauth.models.dtos.ValidateSessionDTO;
import com.ifsg.multifactorauth.models.interfaces.MultiFactorAuth;
import org.springframework.stereotype.Service;

@Service
public class RSAAdapter implements MultiFactorAuth {
    @Override
    public SessionDTO createSession(CreateSessionDTO data) {
        return null;
    }

    @Override
    public Boolean validateSession(AuthSessionEntity entity, ValidateSessionDTO body) {
        return null;
    }
}
