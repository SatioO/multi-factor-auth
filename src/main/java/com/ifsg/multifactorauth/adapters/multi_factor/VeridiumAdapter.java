package com.ifsg.multifactorauth.adapters.multi_factor;

import com.ifsg.multifactorauth.entities.MultiFactorEntity;
import com.ifsg.multifactorauth.models.dtos.InitializeChallengeDTO;
import com.ifsg.multifactorauth.models.dtos.ChallengeDTO;
import com.ifsg.multifactorauth.models.dtos.VerifyChallengeDTO;
import com.ifsg.multifactorauth.models.enums.AuthStatus;
import com.ifsg.multifactorauth.models.interfaces.MultiFactorAuth;
import org.springframework.stereotype.Service;


@Service
public class VeridiumAdapter implements MultiFactorAuth  {
    @Override
    public ChallengeDTO createSession(InitializeChallengeDTO data) {
        return ChallengeDTO.builder()
                .code("123456")
                .authMethod(data.getAuthMethod())
                .status(AuthStatus.CHALLENGE)
                .build();
    }

    @Override
    public Boolean verifyChallenge(MultiFactorEntity entity, VerifyChallengeDTO body) {
        return null;
    }
}
