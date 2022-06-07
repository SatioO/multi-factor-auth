package com.ifsg.multifactorauth.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessDTO<T> implements java.io.Serializable {
    private T data;
    private Metadata meta;
    private T error;
    private String statusCode;
}