package com.ifsg.multifactorauth.config;

import lombok.Data;

@Data
public class OTPSoftTokenConfig {
    private int tokenLength;
    private int maxLength;
}
