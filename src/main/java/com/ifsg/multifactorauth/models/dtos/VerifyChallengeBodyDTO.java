package com.ifsg.multifactorauth.models.dtos;

import com.ifsg.multifactorauth.models.enums.AuthMethod;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class VerifyChallengeBodyDTO {
    private UUID challengeId;
    private AuthMethod authMethod;
    private String answer;
    private String browserVersion;
    private String deviceVersion;
}
