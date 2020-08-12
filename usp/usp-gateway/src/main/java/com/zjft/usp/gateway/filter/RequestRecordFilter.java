package com.zjft.usp.gateway.filter;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.zjft.usp.common.model.LoginAppUser;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.gateway.util.IPUtil;
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

import java.net.URI;

/**
 * 公共请求参数过滤器
 *
 * @author zgpi
 * @version 1.0
 * @date 2019-08-15 13:39
 **/
@Slf4j
@Component
public class RequestRecordFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        URI requestUri = request.getURI();
        //只记录 http 请求(包含 https)
        String schema = requestUri.getScheme();

        log.info("请求url:{}, 请求方法:{}, 请求地址:{}",
                request.getURI(), request.getMethod(), IPUtil.getIpAddr(request));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            OAuth2Authentication oauth2Authentication = (OAuth2Authentication) authentication;
            String clientId = oauth2Authentication.getOAuth2Request().getClientId();

            log.info("当前 clientId:{}", clientId);

            if (oauth2Authentication.getUserAuthentication() != null) {
                LoginAppUser loginAppUser = (LoginAppUser) oauth2Authentication.getPrincipal();
                Long userId = loginAppUser.getUserId();
                log.info("用户 userId:{}", userId);
            }
        }

        if ((!"http".equalsIgnoreCase(schema) && !"https".equalsIgnoreCase(schema))) {
            return chain.filter(exchange);
        }

        String commonParamJson = request.getHeaders().getFirst("Common-Params");
        JSONObject jsonObject = new JSONObject(commonParamJson);
        String txId = KeyUtil.getIdStr();
        jsonObject.put("txId", txId);
        String json = JSON.toJSONString(jsonObject);
        log.info("公共参数---commonParams:{},txId:{}", commonParamJson, txId);
        request.mutate()
                .header("x-common-param-header", json)
                .build();
        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return -2;
    }

}
