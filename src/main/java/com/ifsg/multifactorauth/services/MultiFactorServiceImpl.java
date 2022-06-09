package com.ifsg.multifactorauth.services;

import com.ifsg.multifactorauth.adapters.multi_factor.MultiFactorAdapter;
import com.ifsg.multifactorauth.entities.MultiFactorEntity;
import com.ifsg.multifactorauth.exceptions.ResourceNotFoundException;
import com.ifsg.multifactorauth.models.dtos.*;
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
    public VerifyChallengeResponseDTO verifyChallenge(VerifyChallengeBodyDTO body) {
        return this.multiFactorRepository.findById(body.getChallengeId()).map(entity -> {
            // NOTE: Throw error if the user tries to validate already approved session
            if (entity.getAuthStatus() == AuthStatus.SUCCESS) {
                return VerifyChallengeResponseDTO
                        .builder()
                        .authStatus(AuthStatus.ERROR)
                        .authReasonCode(AuthReasonCode.CHALLENGE_VERIFIED)
                        .build();
            }

            // NOTE: Throw error if the user tries to validate expired session
            if (entity.getAuthStatus() == AuthStatus.EXPIRED) {
                return VerifyChallengeResponseDTO
                        .builder()
                        .authStatus(AuthStatus.ERROR)
                        .authReasonCode(AuthReasonCode.CHALLENGE_EXPIRED)
                        .build();
            }

            // NOTE: Throw error if the user is blocked from verification after multiple failure attempts
            if (entity.getAuthStatus() == AuthStatus.ERROR) {
                return VerifyChallengeResponseDTO
                        .builder()
                        .authStatus(AuthStatus.ERROR)
                        .authReasonCode(AuthReasonCode.VERIFICATION_BLOCKED)
                        .build();
            }

            // NOTE: Throw error if the requested auth method is different from verification auth method
            if(entity.getAuthMethod() != body.getAuthMethod()) {
                return VerifyChallengeResponseDTO
                        .builder()
                        .authStatus(AuthStatus.FAIL)
                        .authReasonCode(AuthReasonCode.AUTH_CODE_MISMATCH)
                        .build();
            }

            // NOTE: Connect to respective adapter to perform validation logic such as compare OTP
            VerifyChallengeAdapterDTO result = this.multiFactorAdapter
                    .getAdapter(body.getAuthMethod())
                    .verifyChallenge(entity, body);

            this.multiFactorRepository.save(
                    entity.toBuilder()
                            .attempts(entity.getAttempts() + 1)
                            .authStatus(result.getAuthStatus())
                            .authReasonCode(result.getAuthReasonCode())
                            .build());

            return VerifyChallengeResponseDTO
                    .builder()
                    .authStatus(result.getAuthStatus())
                    .authReasonCode(result.getAuthReasonCode())
                    .build();
        }).orElse(VerifyChallengeResponseDTO.builder().authStatus(AuthStatus.ERROR).authReasonCode(AuthReasonCode.CHALLENGE_NOT_FOUND).build());
    }

    @Override
    public MultiFactorEntity initializeChallenge(Map<String, String> headers, InitializeChallengeBodyDTO body, Authentication auth) {
        CreateChallengeAdapterDTO session = this.multiFactorAdapter
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
                        .authStatus(session.getStatus())
                        .createdTime(session.getCreatedTime())
                        .updatedTime(session.getCreatedTime())
                        .expiryTime(session.getExpiryTime())
                        .browserVersion(body.getBrowserVersion())
                        .appVersion(body.getDeviceVersion())
                        .build()
        );
    }
}
