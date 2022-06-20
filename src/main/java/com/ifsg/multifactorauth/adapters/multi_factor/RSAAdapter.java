package com.ifsg.multifactorauth.adapters.multi_factor;

import com.ifsg.multifactorauth.entities.MultiFactorEntity;
import com.ifsg.multifactorauth.models.dtos.CreateChallengeAdapterDTO;
import com.ifsg.multifactorauth.models.dtos.InitializeChallengeBodyDTO;
import com.ifsg.multifactorauth.models.dtos.VerifyChallengeAdapterDTO;
import com.ifsg.multifactorauth.models.dtos.VerifyChallengeBodyDTO;
import com.ifsg.multifactorauth.models.enums.AuthReasonCode;
import com.ifsg.multifactorauth.models.enums.AuthStatus;
import com.ifsg.multifactorauth.models.interfaces.IMultiFactorAuthAdapter;
import org.springframework.stereotype.Service;

@Service
public class RSAAdapter implements IMultiFactorAuthAdapter {
    @Override
    public CreateChallengeAdapterDTO createSession(InitializeChallengeBodyDTO data) {
        return null;
    }

    @Override
    public VerifyChallengeAdapterDTO verifyChallenge(MultiFactorEntity entity, VerifyChallengeBodyDTO body) {
        return VerifyChallengeAdapterDTO.builder()
                .authStatus(AuthStatus.FAIL)
                .authReasonCode(AuthReasonCode.CHALLENGE_FAILED)
                .build();
    }
}
