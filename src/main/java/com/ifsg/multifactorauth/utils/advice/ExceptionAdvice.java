package com.ifsg.multifactorauth.utils.advice;

import com.ifsg.multifactorauth.exceptions.BusinessLogicException;
import com.ifsg.multifactorauth.exceptions.InvalidInputException;
import com.ifsg.multifactorauth.exceptions.ResourceNotFoundException;
import com.ifsg.multifactorauth.models.dtos.ErrorDTO;
import com.ifsg.multifactorauth.models.dtos.ErrorDetailsDTO;
import com.ifsg.multifactorauth.models.dtos.MetadataDTO;
import com.ifsg.multifactorauth.models.enums.RequestHeaderEnum;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(InvalidInputException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO<ErrorDetailsDTO> invalidInputException(InvalidInputException ex, WebRequest request) {
        ErrorDetailsDTO errorDetails = new ErrorDetailsDTO(null, ex.getMessage());

        MetadataDTO metadataDTO = new MetadataDTO(
                request.getHeader(RequestHeaderEnum.GLOBALUUID.value),
                request.getHeader(RequestHeaderEnum.REQUESTUUID.value),
                request.getHeader(RequestHeaderEnum.CHANNEL.value)
        );

        return new ErrorDTO<>(null, metadataDTO, errorDetails, HttpStatus.BAD_REQUEST.name());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO<ErrorDetailsDTO> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDetailsDTO errorDetails = new ErrorDetailsDTO(null, ex.getMessage());

        MetadataDTO metadataDTO = new MetadataDTO(
                request.getHeader(RequestHeaderEnum.GLOBALUUID.value),
                request.getHeader(RequestHeaderEnum.REQUESTUUID.value),
                request.getHeader(RequestHeaderEnum.CHANNEL.value)
        );

        return new ErrorDTO<>(null, metadataDTO, errorDetails, HttpStatus.NOT_FOUND.name());
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseBody
    public ErrorDTO<ErrorDetailsDTO> handleStatusException(ResponseStatusException ex, WebRequest request) {
        ErrorDetailsDTO errorDetails = new ErrorDetailsDTO(null, ex.getMessage());

        MetadataDTO metadataDTO = new MetadataDTO(
                request.getHeader(RequestHeaderEnum.GLOBALUUID.value),
                request.getHeader(RequestHeaderEnum.REQUESTUUID.value),
                request.getHeader(RequestHeaderEnum.CHANNEL.value)
        );

        return new ErrorDTO<>(null, metadataDTO, errorDetails, ex.getStatus().name());
    }

    @ExceptionHandler(BusinessLogicException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO<ErrorDetailsDTO> resourceNotFoundException(BusinessLogicException ex, WebRequest request) {
        ErrorDetailsDTO errorDetails = new ErrorDetailsDTO(null, ex.getMessage());

        MetadataDTO metadataDTO = new MetadataDTO(
                request.getHeader(RequestHeaderEnum.GLOBALUUID.value),
                request.getHeader(RequestHeaderEnum.REQUESTUUID.value),
                request.getHeader(RequestHeaderEnum.CHANNEL.value)
        );

        return new ErrorDTO<>(null, metadataDTO, errorDetails, HttpStatus.INTERNAL_SERVER_ERROR.name());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO<ErrorDetailsDTO> processAllError(Exception ex, WebRequest request) {
        ErrorDetailsDTO errorDetails = new ErrorDetailsDTO(null, ex.getMessage());

        MetadataDTO metadataDTO = new MetadataDTO(
                request.getHeader(RequestHeaderEnum.GLOBALUUID.value),
                request.getHeader(RequestHeaderEnum.REQUESTUUID.value),
                request.getHeader(RequestHeaderEnum.CHANNEL.value)
        );

        return new ErrorDTO<>(null, metadataDTO, errorDetails, HttpStatus.INTERNAL_SERVER_ERROR.name());
    }
}
