package com.ifsg.multifactorauth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rsa-policy")
@Data
public class RSAPolicyConfig {
    private RSAConnectionConfig connection;
    private int codeLength;
    private int codeExpiry;
}

