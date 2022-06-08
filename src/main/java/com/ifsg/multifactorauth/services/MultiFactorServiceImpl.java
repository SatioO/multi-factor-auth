package com.ifsg.multifactorauth.services;

import com.ifsg.multifactorauth.adapters.multi_factor.MultiFactorAdapter;
import com.ifsg.multifactorauth.entities.MultiFactorEntity;
import com.ifsg.multifactorauth.exceptions.ResourceNotFoundException;
import com.ifsg.multifactorauth.models.dtos.ChallengeDTO;
import com.ifsg.multifactorauth.models.dtos.ChallengeResponse;
import com.ifsg.multifactorauth.models.dtos.InitializeChallengeDTO;
import com.ifsg.multifactorauth.models.dtos.VerifyChallengeDTO;
import com.ifsg.multifactorauth.models.enums.AuthReasonCode;
import com.ifsg.multifactorauth.models.enums.AuthStatus;
import com.ifsg.multifactorauth.models.enums.RequestHeaderEnum;
import com.ifsg.multifactorauth.repositories.MultiFactorRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class MultiFactorServiceImpl implements MultiFactorService {
    private final MultiFactorAdapter multiFactorAdapter;
    private final MultiFactorRepository multiFactorRepository;

    public MultiFactorServiceImpl(MultiFactorAdapter multiFactorAdapter, MultiFactorRepository multiFactorRepository) {
        this.multiFactorRepository = multiFactorRepository;
        this.multiFactorAdapter = multiFactorAdapter;
    }

    @Override
    public MultiFactorEntity getChallengeStatus(UUID challengeId) {
        return this.multiFactorRepository
                .findById(challengeId)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge Not Found."));
    }

    @Override
    public ChallengeResponse verifyChallenge(VerifyChallengeDTO body) {
        return this.multiFactorRepository.findById(body.getChallengeId()).map(entity -> {
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
                                .authReasonCode(AuthReasonCode.CHALLENGE_VERIFIED)
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
                                    .authReasonCode(AuthReasonCode.VERIFICATION_BLOCKED)
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
                                    .authReasonCode(AuthReasonCode.CHALLENGE_FAILED)
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
    public MultiFactorEntity initializeChallenge(Map<String, String> headers, InitializeChallengeDTO body, Authentication auth) {
        ChallengeDTO session = this.multiFactorAdapter
                .getAdapter(body.getAuthMethod())
                .createSession(body);

        return this.multiFactorRepository.save(
                MultiFactorEntity
                        .builder()
                        .sessionId(UUID.fromString(headers.get(RequestHeaderEnum.GLOBALUUID.value)))
                        .code(session.getCode())
                        .customerId(auth.getName())
                        .channel(headers.get(RequestHeaderEnum.CHANNEL.value))
                        .ipAddress(null)
                        .authMethod(session.getAuthMethod())
                        .authReasonCode(AuthReasonCode.VERIFICATION_REQUIRED)
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
