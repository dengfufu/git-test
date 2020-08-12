package com.zjft.usp.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CK
 * @date 2019-08-01 09:14
 */
public class ResponseUtil {

    private ResponseUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 通过流写到前端
     *
     * @param objectMapper 对象序列化
     * @param response
     * @param msg          返回信息
     * @param httpStatus   返回状态码
     * @throws IOException
     */
    public static void responseWriter(ObjectMapper objectMapper, HttpServletResponse response, String msg, int httpStatus) throws IOException {
        Map<String, String> rsp = new HashMap<>(2);
        response.setStatus(httpStatus);
        rsp.put("code", String.valueOf(httpStatus));
        rsp.put("msg", msg);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try (
                Writer writer = response.getWriter()
        ) {
            writer.write(objectMapper.writeValueAsString(rsp));
            writer.flush();
        }
    }
}
