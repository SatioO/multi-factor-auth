package com.ifsg.multifactorauth.models.dtos;

import com.ifsg.multifactorauth.models.enums.AuthReasonCode;
import com.ifsg.multifactorauth.models.enums.AuthStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerifyChallengeAdapterDTO {
    private AuthStatus authStatus;
    private AuthReasonCode authReasonCode;
}
