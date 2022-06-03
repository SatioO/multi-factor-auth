package com.ifsg.multifactorauth.adapters.multi_factor;

import com.ifsg.multifactorauth.entities.MultiFactorEntity;
import com.ifsg.multifactorauth.models.dtos.InitializeChallengeDTO;
import com.ifsg.multifactorauth.models.dtos.ChallengeDTO;
import com.ifsg.multifactorauth.models.dtos.VerifyChallengeDTO;
import com.ifsg.multifactorauth.models.interfaces.MultiFactorAuth;
import org.springframework.stereotype.Service;

@Service
public class RSAAdapter implements MultiFactorAuth {
    @Override
    public ChallengeDTO createSession(InitializeChallengeDTO data) {
        return null;
    }

    @Override
    public Boolean validateSession(MultiFactorEntity entity, VerifyChallengeDTO body) {
        return null;
    }
}
