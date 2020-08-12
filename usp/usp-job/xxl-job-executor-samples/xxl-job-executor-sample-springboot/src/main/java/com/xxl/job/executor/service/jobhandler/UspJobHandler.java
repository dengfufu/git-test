package com.xxl.job.executor.service.jobhandler;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.util.DateUtil;
import com.zjft.usp.common.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * description: TestJobHandler
 * date: 2020/1/16 19:51
 * version: 1.0
 * @author cxd
 */
@Component
public class UspJobHandler extends IJobHandler {

    @Value("${usp.url}")
    private String apiUrl;

    @Override
    @XxlJob("testJobHandler")
    public ReturnT<String> execute(String param) throws Exception {
        XxlJobLogger.log("===my TestJobHandler===");

        for(int i=0; i<5; i++){
            XxlJobLogger.log("=== : " + i);
        }
        return SUCCESS;
    }
    
    /***
     * 定时删除临时文件
     * @date 2020/1/20
     * @param param
     * @return com.xxl.job.core.biz.model.ReturnT<java.lang.String>
     */
//    @XxlJob("fileJobHandler")
//    public ReturnT<String> fileJobHandler(String param) throws Exception {
//        XxlJobLogger.log("===my fileJobHandler===");
//
//        // 设置删除临时文件的接口地址
//        String Url = apiUrl + "/api/file/deleteFileTemporary";
//        String Type = "POST";
//        String Data = null;
//        this.httpJob(Url,Type,Data);
//
//        return SUCCESS;
//    }

    /***
     * 定时创建位置表
     * @date 2020/1/20
     * @param param
     * @return com.xxl.job.core.biz.model.ReturnT<java.lang.String>
     */
//    @XxlJob("posJobHandler")
//    public ReturnT<String> posJobHandler(String param) throws Exception {
//        XxlJobLogger.log("===my posJobHandler===");
//
//        // 设置定时创建位置表的接口地址
//        String Url = apiUrl +  "/api/pos/position/createPos";
//        String Type = "POST";
//        Map<String,String> params = new HashMap<>();
//        params.put("startDateTime","2020-2-5");
//        params.put("days","5");
//        ObjectMapper json = new ObjectMapper();
//        String  Data = json.writeValueAsString(params);
//        this.httpJob(Url,Type,Data);
//
//        return SUCCESS;
//    }

    /***
     * 定时创建邮件发送记录表
     * @date 2020/1/20
     * @param param
     * @return com.xxl.job.core.biz.model.ReturnT<java.lang.String>
     */
//    @XxlJob("smsJobHandler")
//    public ReturnT<String> smsJobHandler(String param) throws Exception {
//        XxlJobLogger.log("===my smsJobHandler===");
//
//        // 设置定时创建邮件发送记录表的接口地址
//        String Url = apiUrl + "/api/msg/sms/createSms";
//        String Type = "POST";
//        Map<String,String> params = new HashMap<>();
//        params.put("startDateTime","2020-2-5");
//        params.put("days","5");
//        ObjectMapper json = new ObjectMapper();
//        String  Data = json.writeValueAsString(params);
//        this.httpJob(Url,Type,Data);
//
//        return SUCCESS;
//    }

    /***
     * 定时创建短信发送记录表
     * @date 2020/1/20
     * @param param
     * @return com.xxl.job.core.biz.model.ReturnT<java.lang.String>
     */
//    @XxlJob("maiJobHandler")
//    public ReturnT<String> maiJobHandler(String param) throws Exception {
//        XxlJobLogger.log("===my maiJobHandler===");
//
//        // 设置定时创建短信发送记录表的接口地址
//        String Url = apiUrl + "/api/msg/sms/createMai";
//        String Type = "POST";
//        Map<String,String> params = new HashMap<>();
//        params.put("startDateTime","2020-2-5");
//        params.put("days","5");
//        ObjectMapper json = new ObjectMapper();
//        String  Data = json.writeValueAsString(params);
//        this.httpJob(Url,Type,Data);
//
//        return SUCCESS;
//    }

    /***
     * 定时创建公告表
     * @date 2020/1/20
     * @param param
     * @return com.xxl.job.core.biz.model.ReturnT<java.lang.String>
     */
//    @XxlJob("bulletinJobHandler")
//    public ReturnT<String> bulletinJobHandler(String param) throws Exception {
//        XxlJobLogger.log("===my bulletinJobHandler===");
//        // 作废
//        return SUCCESS;
//    }

    /***
     * 定时创建群发通知表
     * @date 2020/1/20
     * @param param
     * @return com.xxl.job.core.biz.model.ReturnT<java.lang.String>
     */
//    @XxlJob("noticeJobHandler")
//    public ReturnT<String> noticeJobHandler(String param) throws Exception {
//        XxlJobLogger.log("===my noticeJobHandler===");
//        // 作废
//        return SUCCESS;
//    }

    /***
     * 定时创建单点消息记录表
     * @date 2020/1/20
     * @param param
     * @return com.xxl.job.core.biz.model.ReturnT<java.lang.String>
     */
//    @XxlJob("pushMsgJobHandler")
//    public ReturnT<String> pushMsgJobHandler(String param) throws Exception {
//        XxlJobLogger.log("===my pushMsgJobHandler===");
//
//        // 设置定时创建单点消息记录表的接口地址
//        String Url = apiUrl + "/api/msg/createPushMsg";
//        String Type = "POST";
//        Map<String,String> params = new HashMap<>();
//        params.put("startDateTime","2020-2-5");
//        params.put("days","5");
//        ObjectMapper json = new ObjectMapper();
//        String  Data = json.writeValueAsString(params);
//        this.httpJob(Url,Type,Data);
//
//        return SUCCESS;
//    }

    /**
     * usp定时作业
     *
     * @param params
     * @return
     * @throws Exception
     */
    @XxlJob("uspJobHandler")
    public ReturnT uspJobHandler(String params) throws Exception {
        XxlJobLogger.log("=== my createWorkFeeVerify jobHandler ===");
        String type = "POST";
        ObjectMapper json = new ObjectMapper();
        try {
            if (StrUtil.isEmpty(params)) {
                XxlJobLogger.log(new Exception("未获取到接口地址"));
                return FAIL;
            }
            String url = apiUrl + params + "?_allow_anonymous=true";
            this.httpJob(url, type, "");
            return SUCCESS;
        } catch (Exception e) {
            XxlJobLogger.log(e);
            return FAIL;
        }
    }


    /***
     * 跨平台Http任务
     * @date 2020/1/20
     * @param url    http接口地址
     * @return com.xxl.job.core.biz.model.ReturnT<java.lang.String>
     */
    public ReturnT<String> httpJob(String url,String type,String data) throws Exception {

        // request
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        try {
            // connection
            URL realUrl = new URL(url);
            connection = (HttpURLConnection) realUrl.openConnection();

            // connection setting
            connection.setRequestMethod(type);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setReadTimeout(5 * 1000);
            connection.setConnectTimeout(3 * 1000);
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept-Charset", "application/json;charset=UTF-8");

            // do connection
            connection.connect();

            // 当为POST请求时，传入参数
            String postType = "POST";
            data = StrUtil.trimToEmpty(data);
            if(postType.equals(type)){
                DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                dos.write(data.getBytes());
                dos.flush();
                dos.close();
            }

            //Map<String, List<String>> map = connection.getHeaderFields();

            // valid StatusCode
            int statusCode = connection.getResponseCode();
            if (statusCode != 200) {
                throw new RuntimeException("Http Request StatusCode(" + statusCode + ") Invalid.");
            }

            // result
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            String responseMsg = result.toString();

            XxlJobLogger.log(responseMsg);
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            XxlJobLogger.log(e);
            return ReturnT.FAIL;
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Exception e2) {
                XxlJobLogger.log(e2);
            }
        }

    }
}
