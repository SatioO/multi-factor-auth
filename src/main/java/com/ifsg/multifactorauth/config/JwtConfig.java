package com.ifsg.multifactorauth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Validated
@Data
public class JwtConfig {
    @NotNull
    private String secret;
}
