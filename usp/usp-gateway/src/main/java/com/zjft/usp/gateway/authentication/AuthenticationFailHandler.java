package com.zjft.usp.gateway.authentication;

import com.zjft.usp.gateway.util.ServerResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


/**
 * @author CK
 * @date 2019-08-10 22:34
 */
@Component
public class AuthenticationFailHandler implements ServerAuthenticationFailureHandler {

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
        return ServerResponseUtil.serverResponseWriter(response,
                exception.getMessage(),
                HttpStatus.UNAUTHORIZED);
    }
}
