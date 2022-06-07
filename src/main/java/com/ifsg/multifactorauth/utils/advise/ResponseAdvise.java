package com.ifsg.multifactorauth.utils.advise;

import com.ifsg.multifactorauth.models.dtos.ErrorDTO;
import com.ifsg.multifactorauth.models.dtos.Metadata;
import com.ifsg.multifactorauth.models.dtos.SuccessDTO;
import com.ifsg.multifactorauth.models.enums.RequestHeaderEnum;
import com.ifsg.multifactorauth.models.interfaces.IgnoreResponseBinding;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ResponseAdvise implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (methodParameter.getContainingClass().isAnnotationPresent(RestController.class)) {

            if (!methodParameter.getMethod().isAnnotationPresent(IgnoreResponseBinding.class)) {
                if ((!(o instanceof ErrorDTO<?>)) && (!(o instanceof SuccessDTO))) {
                    HttpHeaders headers = serverHttpRequest.getHeaders();

                    Metadata metadata = new Metadata(
                            headers.getFirst(RequestHeaderEnum.GLOBALUUID.value),
                            headers.getFirst(RequestHeaderEnum.REQUESTUUID.value),
                            headers.getFirst(RequestHeaderEnum.CHANNEL.value)
                    );

                    SuccessDTO<Object> responseBody = new SuccessDTO<>(o, metadata, null, HttpStatus.OK.name());
                    return responseBody;
                }
            }
        }
        return o;
    }


}
