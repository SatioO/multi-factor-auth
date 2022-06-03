package com.ifsg.multifactorauth.models.dtos;

import com.ifsg.multifactorauth.models.enums.AuthStatus;
import lombok.Data;

@Data
public class MultiFactorReplyDTO {
    private AuthStatus responseCode;
    private String reasonCode;
}
