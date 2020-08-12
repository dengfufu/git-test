package com.zjft.usp.gateway.config;

import com.zjft.usp.common.oauth2.properties.SecurityProperties;
import com.zjft.usp.gateway.authentication.JsonAccessDeniedHandler;
import com.zjft.usp.gateway.authentication.JsonAuthenticationEntryPoint;
import com.zjft.usp.gateway.authentication.PermissionAuthManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

import java.util.Arrays;


/**
 * @author CK
 * @date 2019-08-06 09:49
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
@EnableWebFluxSecurity
@Slf4j
public class WebFluxSecurityConfig {

    @Autowired
    private AuthenticationWebFilter authenticationWebFilter;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private PermissionAuthManager permissionAuthManager;

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity security) {
        JsonAuthenticationEntryPoint entryPoint = new JsonAuthenticationEntryPoint();
        return security
                .headers().frameOptions().disable()
                .and()
                .csrf().disable()
                .logout().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .exceptionHandling()
                .and()
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange()
                .matchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .pathMatchers(securityProperties.getIgnore().getUrls()).permitAll()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers("/api/app/version/check4update").permitAll()
                .anyExchange().access(permissionAuthManager)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new JsonAccessDeniedHandler())
                .authenticationEntryPoint(entryPoint)
                .and()
                .build();
    }

}

