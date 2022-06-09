package com.ifsg.multifactorauth.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MetadataDTO {
    @JsonProperty("GLOBALUUID")
    private String GLOBALUUID;

    @JsonProperty("REQUESTUUID")
    private String REQUESTUUID;

    @JsonProperty("CHANNEL")
    private String CHANNEL;
}
