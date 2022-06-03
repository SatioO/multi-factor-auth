package com.ifsg.multifactorauth.services;

import com.ifsg.multifactorauth.adapters.multi_factor.MultiFactorAdapter;
import com.ifsg.multifactorauth.entities.MultiFactorEntity;
import com.ifsg.multifactorauth.models.dtos.ChallengeDTO;
import com.ifsg.multifactorauth.models.dtos.ChallengeResponse;
import com.ifsg.multifactorauth.models.dtos.InitializeChallengeDTO;
import com.ifsg.multifactorauth.models.dtos.VerifyChallengeDTO;
import com.ifsg.multifactorauth.models.enums.AuthReasonCode;
import com.ifsg.multifactorauth.models.enums.AuthStatus;
import com.ifsg.multifactorauth.repositories.MultiFactorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class MultiFactorServiceImpl implements MultiFactorService {
    @Autowired
    private MultiFactorAdapter multiFactorAdapter;

    @Autowired
    private MultiFactorRepository multiFactorRepository;

    @Override
    public MultiFactorEntity getChallengeStatus(UUID sessionId) {
        return this.multiFactorRepository
                .findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session Not Found."));
    }

    @Override
    public ChallengeResponse verifyChallenge(VerifyChallengeDTO body) {
        return this.multiFactorRepository.findById(body.getSessionId()).map(entity -> {
            // NOTE: Throw error if the user tries to validate already approved session
            if (entity.getStatus() == AuthStatus.SUCCESS) {
                return ChallengeResponse
                        .builder()
                        .authStatus(AuthStatus.ERROR)
                        .authReasonCode(AuthReasonCode.CHALLENGE_VERIFIED)
                        .build();
            }

            // NOTE: Throw error if the user tries to validate expired session
            if (entity.getStatus() == AuthStatus.EXPIRED) {
                return ChallengeResponse
                        .builder()
                        .authStatus(AuthStatus.ERROR)
                        .authReasonCode(AuthReasonCode.CHALLENGE_EXPIRED)
                        .build();
            }

            // NOTE: Throw error if the user is blocked from verification after multiple failure attempts
            if (entity.getStatus() == AuthStatus.ERROR) {
                return ChallengeResponse
                        .builder()
                        .authStatus(AuthStatus.ERROR)
                        .authReasonCode(AuthReasonCode.VERIFICATION_BLOCKED)
                        .build();
            }

            // NOTE: Connect to respective adapter to perform validation logic such as compare OTP
            boolean result = this.multiFactorAdapter
                    .getAdapter(body.getAuthMethod())
                    .validateSession(entity, body);

            int attempts = entity.getAttempts() + 1;

            if(result) {
                this.multiFactorRepository.save(
                        entity.toBuilder()
                                .attempts(attempts)
                                .status(AuthStatus.SUCCESS)
                                .build());

                return ChallengeResponse
                        .builder()
                        .authStatus(AuthStatus.SUCCESS)
                        .authReasonCode(AuthReasonCode.CHALLENGE_VERIFIED)
                        .build();
            } else {
                if (attempts > 3) {
                    // NOTE: if entered OTP doesn't match, mark it with failure in DB but if it crosses set limit, then mark that as blocked
                    this.multiFactorRepository.save(
                            entity.toBuilder()
                                    .attempts(attempts)
                                    .status(AuthStatus.ERROR)
                                    .build());

                    return ChallengeResponse
                            .builder()
                            .authStatus(AuthStatus.ERROR)
                            .authReasonCode(AuthReasonCode.VERIFICATION_BLOCKED)
                            .build();
                } else {
                    // NOTE: if entered OTP doesn't match, mark it with failure in DB but if it crosses set limit, then mark that as blocked
                    this.multiFactorRepository.save(
                            entity.toBuilder()
                                    .attempts(attempts)
                                    .status(AuthStatus.FAIL)
                                    .build());

                    return ChallengeResponse
                            .builder()
                            .authStatus(AuthStatus.FAIL)
                            .authReasonCode(AuthReasonCode.CHALLENGE_FAILED)
                            .build();
                }
            }
        }).orElse(ChallengeResponse.builder().authStatus(AuthStatus.ERROR).authReasonCode(AuthReasonCode.CHALLENGE_NOT_FOUND).build());
    }

    @Override
    public MultiFactorEntity initializeChallenge(InitializeChallengeDTO body) {
        ChallengeDTO session = this.multiFactorAdapter
                .getAdapter(body.getAuthMethod())
                .createSession(body);

        return this.multiFactorRepository.save(
                MultiFactorEntity
                        .builder()
                        .code(session.getCode())
                        .customerId("DUMMY_CUST_1")
                        .channel("DUMMY_CHAN_WEB")
                        .ipAddress("1.2.3.4")
                        .authMethod(session.getAuthMethod())
                        .reasonCode(AuthReasonCode.VERIFICATION_REQUIRED)
                        .attempts(0)
                        .status(session.getStatus())
                        .createdTime(session.getCreatedTime())
                        .updatedTime(session.getCreatedTime())
                        .expiryTime(session.getExpiryTime())
                        .browserVersion(body.getBrowserVersion())
                        .appVersion(body.getDeviceVersion())
                        .build()
        );
    }
}
