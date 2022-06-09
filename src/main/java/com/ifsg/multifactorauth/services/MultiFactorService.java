package com.ifsg.multifactorauth.services;

import com.ifsg.multifactorauth.entities.MultiFactorEntity;
import com.ifsg.multifactorauth.models.dtos.VerifyChallengeResponseDTO;
import com.ifsg.multifactorauth.models.dtos.InitializeChallengeBodyDTO;
import com.ifsg.multifactorauth.models.dtos.VerifyChallengeBodyDTO;
import org.springframework.security.core.Authentication;

import java.util.Map;
import java.util.UUID;

public interface MultiFactorService {
    /** This is called by a subsequent session to be able to check or verify a previous authentication result.  */
    MultiFactorEntity getChallengeStatus(UUID sessionId);

    /** This request should be used when the client wants to provide authentication credentials (i.e., a password, an OTP, etc.). */
    VerifyChallengeResponseDTO verifyChallenge(VerifyChallengeBodyDTO body);

    /** This request should be used when the client wants to start an authentication attempt. */
    MultiFactorEntity initializeChallenge(Map<String, String> headers, InitializeChallengeBodyDTO sessionId, Authentication auth);
}
