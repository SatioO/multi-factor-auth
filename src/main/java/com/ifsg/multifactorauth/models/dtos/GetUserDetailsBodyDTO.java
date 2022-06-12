package com.ifsg.multifactorauth.models.dtos;

import com.ifsg.multifactorauth.models.enums.AuthMethod;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUserDetailsBodyDTO {
    private AuthMethod authMethod;
    private String externalId;
}
