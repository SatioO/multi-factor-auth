package com.ifsg.multifactorauth.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Metadata {
    private String GLOBALUUID;
    private String REQUESTUUID;
    private String CHANNEL;
}
