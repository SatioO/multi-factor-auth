package com.ifsg.multifactorauth.adapters.multi_factor;

import com.ifsg.multifactorauth.entities.MultiFactorEntity;
import com.ifsg.multifactorauth.models.dtos.CreateChallengeAdapterDTO;
import com.ifsg.multifactorauth.models.dtos.InitializeChallengeBodyDTO;
import com.ifsg.multifactorauth.models.dtos.VerifyChallengeAdapterDTO;
import com.ifsg.multifactorauth.models.dtos.VerifyChallengeBodyDTO;
import com.ifsg.multifactorauth.models.enums.AuthReasonCode;
import com.ifsg.multifactorauth.models.enums.AuthStatus;
import com.ifsg.multifactorauth.models.interfaces.MultiFactorAuthAdapter;
import com.ifsg.multifactorauth.rest.veridium.VeridiumRestClient;
import org.springframework.stereotype.Service;


@Service
public class VeridiumAdapter implements MultiFactorAuthAdapter {
    private final VeridiumRestClient restClient;

    public VeridiumAdapter(VeridiumRestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public CreateChallengeAdapterDTO createSession(InitializeChallengeBodyDTO data) {
        return CreateChallengeAdapterDTO.builder()
                .code("123456")
                .authMethod(data.getAuthMethod())
                .status(AuthStatus.CHALLENGE)
                .build();
    }

    @Override
    public VerifyChallengeAdapterDTO verifyChallenge(MultiFactorEntity entity, VerifyChallengeBodyDTO body) {
        return VerifyChallengeAdapterDTO.builder()
                .authStatus(AuthStatus.FAIL)
                .authReasonCode(AuthReasonCode.CHALLENGE_FAILED)
                .build();
    }
}
