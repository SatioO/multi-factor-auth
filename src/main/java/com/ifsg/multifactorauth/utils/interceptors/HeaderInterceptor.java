package com.ifsg.multifactorauth.utils.interceptors;

import com.ifsg.multifactorauth.exceptions.InvalidInputException;
import com.ifsg.multifactorauth.models.enums.RequestHeaderEnum;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
public class HeaderInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object object) throws Exception {
        try {
            UUID.fromString(request.getHeader(RequestHeaderEnum.GLOBALUUID.value));
        } catch (Exception e) {
            throw new InvalidInputException("Invalid GLOBALUUID");
        }

        try {
            UUID.fromString(request.getHeader(RequestHeaderEnum.REQUESTUUID.value));
        } catch (Exception e) {
            throw new InvalidInputException("Invalid REQUESTUUID");
        }

        if(!StringUtils.hasText(request.getHeader(RequestHeaderEnum.CHANNEL.value))) {
            throw new InvalidInputException("CHANNEL is required");
        }

        return true;
    }
}
