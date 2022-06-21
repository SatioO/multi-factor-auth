package com.ifsg.multifactorauth.models.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAdapterResponse {
    private Boolean status;
    private String message;
}
