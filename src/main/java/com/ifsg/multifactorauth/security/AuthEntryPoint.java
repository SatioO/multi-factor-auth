package com.ifsg.multifactorauth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifsg.multifactorauth.models.dtos.ErrorDTO;
import com.ifsg.multifactorauth.models.dtos.ErrorDetailsDTO;
import com.ifsg.multifactorauth.models.dtos.Metadata;
import com.ifsg.multifactorauth.models.enums.RequestHeaderEnum;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthEntryPoint implements AuthenticationEntryPoint {
    public @Override void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        ErrorDetailsDTO errorDetails = new ErrorDetailsDTO(null, "Authentication is required to access this resource");
        Metadata metadata = new Metadata(
                request.getHeader(RequestHeaderEnum.GLOBALUUID.value),
                request.getHeader(RequestHeaderEnum.REQUESTUUID.value),
                request.getHeader(RequestHeaderEnum.CHANNEL.value)
        );

        response.setHeader("content-type", "application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        String responseMsg = mapper.writeValueAsString(new ErrorDTO<>(null, metadata, errorDetails, HttpStatus.UNAUTHORIZED.name() ));
        response.getWriter().write(responseMsg);
    }
}
