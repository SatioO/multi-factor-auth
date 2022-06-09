package com.ifsg.multifactorauth.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDTO<T> implements java.io.Serializable {
    private T data;
    private MetadataDTO meta;
    private T error;
    private String statusCode;
}


