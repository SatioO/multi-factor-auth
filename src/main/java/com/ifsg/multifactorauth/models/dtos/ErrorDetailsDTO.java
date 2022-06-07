package com.ifsg.multifactorauth.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorDetailsDTO {
    private List<StackTraceElement> stack;
    private String message;
}
