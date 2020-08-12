package com.zjft.usp.gateway.authentication;

import cn.hutool.core.util.StrUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 获取token
 *
 * @author CK
 * @date 2019-08-10 22:15
 */
public class AuthenticationConverter implements ServerAuthenticationConverter {

    private static final String BEARER = "bearer ";

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        String token = extractToken(exchange.getRequest());
        if (token != null) {
            return Mono.just(new PreAuthenticatedAuthenticationToken(token, ""));
        }
        return Mono.empty();
    }

    private String extractToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isBlank(token) || !token.toLowerCase().startsWith(BEARER)) {
            return null;
        }
        return token.substring(BEARER.length());
    }
}
