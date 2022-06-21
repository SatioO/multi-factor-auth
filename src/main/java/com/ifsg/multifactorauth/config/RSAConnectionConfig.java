package com.ifsg.multifactorauth.config;

import lombok.Data;

@Data
public class RSAConnectionConfig {
    private String baseUrl;
    private String profile;
    private String type;
    private String username;
    private String password;
}
