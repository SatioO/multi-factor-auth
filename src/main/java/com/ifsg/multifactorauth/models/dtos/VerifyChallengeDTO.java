package com.ifsg.multifactorauth.models.dtos;

import com.ifsg.multifactorauth.models.enums.AuthMethod;
import lombok.Data;

import java.util.UUID;

@Data
public class VerifyChallengeDTO {
    private UUID sessionId;
    private AuthMethod authMethod;
    private String code;
    private String browserVersion;
    private String deviceVersion;
}
