package com.ifsg.multifactorauth.models.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserBodyDTO {
    private String externalId;
    private String firstName;
    private String lastName;
    private String primaryEmail;
    private String secondaryEmail;
    private String phoneCountryCode;
    private String phoneNumber;
}
