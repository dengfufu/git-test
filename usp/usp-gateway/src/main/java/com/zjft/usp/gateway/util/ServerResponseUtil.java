package com.zjft.usp.gateway.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zjft.usp.common.model.Result;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;


/**
 * webflux
 *
 * @author CK
 * @date 2019-08-10 22:41
 */
public class ServerResponseUtil {

    private static final String ALLOWED_HEADERS = "x-requested-with, authorization, Content-Type, Authorization, " +
            "credential, X-XSRF-TOKEN, Common-Params";
    private static final String ALLOWED_METHODS = "GET, PUT, POST, DELETE, OPTIONS";
    private static final String ALLOWED_ORIGIN = "*";
    private static final String MAX_AGE = "3600";

    private ServerResponseUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 通过流写到前端
     *
     * @param response
     * @param msg
     * @param httpStatus
     * @return
     */
    public static Mono<Void> serverResponseWriter(ServerHttpResponse response,
                                                  String msg,
                                                  HttpStatus httpStatus) {
        response.setStatusCode(httpStatus);
        HttpHeaders headers = response.getHeaders();

        // 需要跨域设置，否则前端无法获取正确的异常信息
        headers.add("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
        headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
        headers.add("Access-Control-Max-Age", MAX_AGE);
        headers.add("Access-Control-Allow-Headers",ALLOWED_HEADERS);

        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        Result result = new Result(null, httpStatus.value(), msg);
        String r;
        try {
            ObjectMapper mapper = new ObjectMapper();
            r = mapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            r = e.getMessage();
            e.printStackTrace();
        }
        DataBuffer dataBuffer = response.bufferFactory().wrap(r.getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }

    /**
     * webflux的response返回json对象
     */
    public static Mono<Void> responseWriter(ServerWebExchange exchange, int httpStatus, String msg) {
        Result result = Result.failedWith(null, httpStatus, msg);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.valueOf(result.getCode()));

        HttpHeaders headers = response.getHeaders();

        // 需要跨域设置，否则前端无法获取正确的异常信息
        headers.add("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
        headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
        headers.add("Access-Control-Max-Age", MAX_AGE);
        headers.add("Access-Control-Allow-Headers",ALLOWED_HEADERS);

        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        DataBufferFactory dataBufferFactory = response.bufferFactory();
        DataBuffer buffer = dataBufferFactory.wrap(JSONObject.toJSONString(result).getBytes(Charset.defaultCharset()));
        return response.writeWith(Mono.just(buffer)).doOnError((error) -> {
            DataBufferUtils.release(buffer);
        });
    }
}
