package com.ifsg.multifactorauth.models.dtos;

import com.ifsg.multifactorauth.models.enums.DeviceType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignTokenBodyDTO {
    private String externalId;
    private DeviceType deviceType;
    private String deviceId;
}
