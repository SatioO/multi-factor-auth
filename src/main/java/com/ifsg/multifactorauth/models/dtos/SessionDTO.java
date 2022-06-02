package com.ifsg.multifactorauth.models.dtos;

import com.ifsg.multifactorauth.models.enums.AuthMethod;
import com.ifsg.multifactorauth.models.enums.AuthStatus;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class SessionDTO {
    private UUID sessionId;
    private AuthMethod method;
    private String code;
    private AuthStatus status;
    private Date expiryTime;
    private Date createdTime;
}
