package com.zjft.usp.gateway.authentication;

import com.zjft.usp.gateway.util.ServerResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 401未授权异常处理，转换为JSON
 *
 * @author zgpi
 * @date 2019/11/26 16:11
 */
@Slf4j
public class JsonAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        return ServerResponseUtil.responseWriter(exchange, HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }
}
