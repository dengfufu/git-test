package com.zjft.usp.common.utils;

import cn.hutool.core.util.StrUtil;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * RestTemplate工具类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-02-22 18:41
 **/
public class RestTemplateUtil {
    public static RestTemplate restTemplate;
    /**
     * 连接超时时间默认5s
     */
    private static int CONNECT_TIMEOUT = 5 * 1000;
    /**
     * 读写超时时间默认30s
     */
    private static int READ_TIMEOUT = 30 * 1000;

    static {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        //连接超时时间
        requestFactory.setConnectTimeout(CONNECT_TIMEOUT);
        //读写超时时间
        requestFactory.setReadTimeout(READ_TIMEOUT);
        if (restTemplate == null) {
            restTemplate = new RestTemplate(requestFactory);
        }
    }

    /**
     * post请求
     * @param url 请求路径
     * @param paramData body数据
     * @param token JWT所需的Token，不需要的可去掉
     * @return
     */
    public static String post(String url, String paramData, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Content-Encoding", "UTF-8");
        headers.add("Content-Type", "application/json; charset=UTF-8");
        if (token != null) {
            headers.add("Authorization", token);
        }
        HttpEntity<String> requestEntity = new HttpEntity<>(paramData, headers);
        return restTemplate.postForObject(url, requestEntity, String.class);
    }


    public static String postJson(String url, String paramData, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");
        headers.add("Content-Encoding", "UTF-8");
        if (token != null) {
            headers.add("Authorization", token);
        }
        HttpEntity<String> requestEntity = new HttpEntity<>(paramData, headers);
        return restTemplate.postForObject(url, requestEntity, String.class);
    }

    /**
     * get请求
     * @param url 请求路径
     * @param token JWT所需的Token，不需要的可去掉
     * @return
     */
    public static String get(String url, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Content-Encoding", "UTF-8");
        headers.add("Content-Type", "application/json; charset=UTF-8");
        if (token != null) {
            headers.add("Authorization", token);
        }
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
                String.class);
        return response.getBody();
    }

    /**
     * post请求（表单提交方式）
     * @param url 请求路径
     * @param paramMap 请求参数
     * @param sessionId Session ID
     * @return
     */
    public static ResponseEntity postForm(String url, MultiValueMap<String, Object> paramMap, String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        if (StrUtil.isNotEmpty(sessionId)) {
            List<String> cookies = new ArrayList<>();
            //在 header中存入cookies
            cookies.add(sessionId);
            headers.put(HttpHeaders.COOKIE, cookies);
        }
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(paramMap, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        return response;
    }

    /**
     * 获取文件流
     *
     * @param url       请求路径
     * @param paramMap  请求参数
     * @param sessionId Session ID
     * @return
     */
    public static InputStream downloadFile(String url, MultiValueMap<String, Object> paramMap, String sessionId) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        if (StrUtil.isNotEmpty(sessionId)) {
            List<String> cookies = new ArrayList<>();
            //在 header中存入cookies
            cookies.add(sessionId);
            headers.put(HttpHeaders.COOKIE, cookies);
        }
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(paramMap, headers);
        ResponseEntity<Resource> entity = restTemplate.postForEntity(url, request, Resource.class);
        return entity.getBody().getInputStream();
    }

}
