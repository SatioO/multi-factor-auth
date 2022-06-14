package com.ifsg.multifactorauth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rsa-policy")
@Data
public class RSAPolicyConfig {
    private String baseUrl;
    private String profile;
    private String type;
    private String username;
    private String password;
}
