package com.ifsg.multifactorauth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class BusinessLogicException extends RuntimeException {
    public BusinessLogicException(String exception) {
        super(exception);
    }
}
