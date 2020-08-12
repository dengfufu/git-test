package com.zjft.usp.common.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author zphu
 * @Description
 * @date 2019/8/12 20:49
 * @Version 1.0
 **/
public class RealNameUtil {

    //商家分配的appcode
    private static final String app_code= "商家分配的appcode";
    //第三方接口地址
    private static final String app_url= "http://1.api.apistore.cn/idcard";


    /**
    *  调用第三方接口对身份证实名认证
    * @param realname
    * @param idCard
    * @Return java.lang.String 返回商家定义的结果代码
    * @Author zphu
    * @Date 2019/8/12 21:02
    */
    public static String getIdCardResult(String realname,String idCard) {

        // 返回结果定义
        String returnStr = null;
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        String param = "cardNo="+idCard+"&realName="+realname;
        try {
            url = new URL(app_url + "?" + param);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Authorization", "APPCODE " + app_code);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.connect();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            returnStr = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return returnStr;
    }
}
