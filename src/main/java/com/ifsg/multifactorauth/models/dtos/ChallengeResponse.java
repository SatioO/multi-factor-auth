package com.ifsg.multifactorauth.models.dtos;


import com.ifsg.multifactorauth.models.enums.AuthReasonCode;
import com.ifsg.multifactorauth.models.enums.AuthStatus;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChallengeResponse {
    private AuthStatus authStatus;
    private AuthReasonCode authReasonCode;
}
