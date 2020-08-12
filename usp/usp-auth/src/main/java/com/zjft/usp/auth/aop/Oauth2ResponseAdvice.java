package com.zjft.usp.auth.aop;

import com.zjft.usp.common.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * response body 格式改为Result
 *
 * @author CK
 * @date 2019-08-26 14:54
 */
@Slf4j
@ControllerAdvice(assignableTypes = {TokenEndpoint.class})
public class Oauth2ResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof Exception) {
            Result result = Result.failed(((Exception) body).getMessage());
            return result;
        }
        Result result = Result.succeed(body);
        return result;
    }
}
