package com.ifsg.multifactorauth.models.dtos;

import com.ifsg.multifactorauth.models.enums.AuthMethod;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateSessionDTO {
    @NotNull
    private AuthMethod authMethod;

    private String browserVersion;

    private String deviceVersion;
}
