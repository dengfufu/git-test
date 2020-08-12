package com.zjft.usp.gateway.filter;

import cn.hutool.core.collection.CollectionUtil;
import com.zjft.usp.common.constant.SecurityConstants;
import com.zjft.usp.common.model.LoginAppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * 将认证用户的相关信息放入header中, 后端服务可以直接读取使用
 *
 * @author CK
 * @date 2019-08-06 23:14
 */
@Slf4j
@Component
public class UserInfoHeaderFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            OAuth2Authentication oauth2Authentication = (OAuth2Authentication) authentication;
            String clientId = oauth2Authentication.getOAuth2Request().getClientId();

            // 有用户信息
            String userId = "";
            String authorities = "";
            if (oauth2Authentication.getUserAuthentication() != null) {
                LoginAppUser loginAppUser = (LoginAppUser) authentication.getPrincipal();
                userId = String.valueOf(loginAppUser.getUserId());
                authorities = CollectionUtil.join(authentication.getAuthorities(), ",");
            }

            request.mutate()
                    .header(SecurityConstants.USER_ID_HEADER, userId)
                    .header(SecurityConstants.ROLE_HEADER, authorities)
                    .header(SecurityConstants.CLIENT_ID_HEADER, clientId)
                    .build();
        }
        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
