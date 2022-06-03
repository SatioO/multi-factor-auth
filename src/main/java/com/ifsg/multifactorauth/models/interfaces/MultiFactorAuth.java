package com.ifsg.multifactorauth.models.interfaces;

import com.ifsg.multifactorauth.entities.MultiFactorEntity;
import com.ifsg.multifactorauth.models.dtos.InitializeChallengeDTO;
import com.ifsg.multifactorauth.models.dtos.ChallengeDTO;
import com.ifsg.multifactorauth.models.dtos.VerifyChallengeDTO;

public interface MultiFactorAuth {
    ChallengeDTO createSession(InitializeChallengeDTO data);

    Boolean validateSession(MultiFactorEntity entity, VerifyChallengeDTO body);
}
