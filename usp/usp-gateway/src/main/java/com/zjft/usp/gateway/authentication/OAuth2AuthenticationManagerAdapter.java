package com.zjft.usp.gateway.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author CK
 * @date 2019-08-09 16:22
 */
@Component
@Slf4j
public class OAuth2AuthenticationManagerAdapter implements ReactiveAuthenticationManager {

    @Autowired
    private OAuth2AuthenticationManager oAuth2AuthenticationManager;

    @Override
    public Mono<Authentication> authenticate(Authentication token) {
        return Mono.just(token).publishOn(Schedulers.elastic()).flatMap(t -> {
            try {
                Authentication authResult = this.oAuth2AuthenticationManager.authenticate(t);
                SecurityContextHolder.getContext().setAuthentication(authResult);
                return Mono.just(authResult);
            } catch (Exception e) {
                e.printStackTrace();
                return Mono.error(new BadCredentialsException("Invalid or expired access token presented"));
            }
        }).filter(Authentication::isAuthenticated);
    }

}
