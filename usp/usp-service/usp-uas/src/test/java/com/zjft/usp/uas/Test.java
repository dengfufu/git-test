package com.zjft.usp.uas;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: CK
 * @create: 2020-02-26 17:11
 */
public class Test {

    public static void main(String[] args) {
        String grant_type = "client_credentials";
        String client_id = "zjft-hr";
        String client_secret = "123456";
        String tokenUrl = "https://anyfixgw-dev.zijin.com/oauth/token";
        String deleteUrl = "https://anyfixgw-dev.zijin.com/api/uas/corp-user/change";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            // 1. 请求token
            HttpPost tokenHttpPost = new HttpPost(tokenUrl);
            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("grant_type", grant_type));
            pairs.add(new BasicNameValuePair("client_id", client_id));
            pairs.add(new BasicNameValuePair("client_secret", client_secret));
            tokenHttpPost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
            tokenHttpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            CloseableHttpResponse tokenResponse = httpClient.execute(tokenHttpPost);
            if (tokenResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = tokenResponse.getEntity();
                String result = EntityUtils.toString(entity);
                System.out.println("请求token:" + result);
                Map<String, Object> resultMap = new ObjectMapper().readValue(result, HashMap.class);
                String access_token = (String) ((HashMap) resultMap.get("data")).get("access_token");
                tokenResponse.close();

                // 2. 得到access_token, 进行删除企业用户操作
                String stringJson = "{\"test\":true,\"username\":\"陈凯\",\"mobile\":\"111111111\"}";
                HttpPost deleteHttpPost = new HttpPost(deleteUrl);
                deleteHttpPost.setHeader("Content-type", "application/json");
                deleteHttpPost.setHeader("Authorization", "Bearer " + access_token);
                deleteHttpPost.setEntity(new StringEntity(stringJson, "UTF-8"));
                CloseableHttpResponse deleteResponse = httpClient.execute(deleteHttpPost);
                if (deleteResponse.getStatusLine().getStatusCode() == 200) {
                    String deleteResult = EntityUtils.toString(deleteResponse.getEntity());
                    System.out.println("删除企业用户: " + deleteResult);
                }
                deleteResponse.close();
            }
            httpClient.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
