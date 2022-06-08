package com.ifsg.multifactorauth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "otp-policy")
@Data
public class OTPPolicyConfig {
    private int codeLength;
    private Long codeExpiry;
    private int codeLimit;
    private String codeSecret;
}
