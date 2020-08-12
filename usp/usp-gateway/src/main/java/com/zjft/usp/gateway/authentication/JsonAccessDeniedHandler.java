package com.zjft.usp.gateway.authentication;

import com.alibaba.fastjson.JSONObject;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.gateway.util.ServerResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

/**
 * 403拒绝访问异常处理，转换为JSON
 *
 * @author zgpi
 * @date 2019/11/26 16:11
 */
@Slf4j
public class JsonAccessDeniedHandler implements ServerAccessDeniedHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException e) {
        return responseWriter(exchange, HttpStatus.FORBIDDEN.value(), e.getMessage());
    }

    /**
     * webflux的response返回json对象
     */
    private static Mono<Void> responseWriter(ServerWebExchange exchange, int httpStatus, String msg) {
        Result result = Result.failedWith(null, httpStatus, msg);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.valueOf(result.getCode()));

        HttpHeaders headers = response.getHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        DataBufferFactory dataBufferFactory = response.bufferFactory();
        DataBuffer buffer = dataBufferFactory.wrap(JSONObject.toJSONString(result).getBytes(Charset.defaultCharset()));
        return response.writeWith(Mono.just(buffer)).doOnError((error) -> {
            DataBufferUtils.release(buffer);
        });
    }
}
