package com.ifsg.multifactorauth.models.dtos;

import com.ifsg.multifactorauth.models.enums.AuthMethod;
import com.ifsg.multifactorauth.models.enums.AuthStatus;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class CreateChallengeAdapterDTO {
    private UUID sessionId;
    private AuthMethod authMethod;
    private String code;
    private AuthStatus status;
    private Date expiryTime;
    private Date createdTime;
}
