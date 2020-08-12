package com.zjft.usp.common.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 百度地图工具类
 *
 * @author Qiugm
 * @date 2019-08-15 14:13
 * @version 1.0.0
 **/
public class BaiduMapUtil {
    private static Logger log = LoggerFactory.getLogger(BaiduMapUtil.class);
    private static String ak = "gvyrDtzP8UZyqVFh2fscaRAP";

    /**
     * 反向地址解析（地址查询）
     *
     * @param x
     *            经度
     * @param y
     *            维度
     * @param coordType
     *            坐标类型，可选参数，默认为bd09经纬度坐标。允许的值为bd09ll、bd09mc、gcj02、wgs84。
     *            bd09ll表示百度经纬度坐标 ，bd09mc表示百度墨卡托坐标，gcj02表示经过国测局加密的坐标，
     *            wgs84表示gps获取的坐标。
     * @return
     */
    public Map<String, String> getPost(double x, double y, String coordType)
            throws IOException {
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("address", "");
            URL url = new URL(
                    "http://api.map.baidu.com/geocoder?callback=renderReverse&location="
                            + y + "," + x + "&output=json&coord_type="
                            + coordType);
            /*
             * 然后把连接设为输出模式。URLConnection通常作为输入来使用，比如下载一个Web页。
             * 通过把URLConnection设为输出，你可以把数据向你个Web页传送。下面是如何做：
             */
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            // 设置输入流采用字节流
            connection.setDoInput(true);
            // 设置输出流采用字节流
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            // POST请求不能使用缓存
            connection.setUseCaches(false);
            OutputStreamWriter out = new OutputStreamWriter(connection
                    .getOutputStream(), "utf-8");
            // remember to clean up
            out.write("&ak=" + ak);
            out.flush();
            out.close();
            // 一旦发送成功，用以下方法就可以得到服务器的回应：
            String res;
            InputStream l_urlStream;
            l_urlStream = connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    l_urlStream, "UTF-8"));
            StringBuilder sb = new StringBuilder("");
            while ((res = in.readLine()) != null) {
                sb.append(res.trim());
            }

            if (in != null) {
                in.close();
            }

            String str = sb.toString();

            if (str != null && str.length() > 0) {

                int addStart = str.indexOf("formatted_address\":");
                int addEnd = str.indexOf("\",\"business");
                if (addStart > 0 && addEnd > 0) {
                    String address = str.substring(addStart + 20, addEnd);

                    map.put("address", address);
                    return map;
                }
            }

            return new HashMap<String, String>();
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<String, String>();
        }

    }

    /**
     * 百度经纬度转GPS经纬度
     *
     * @author Qiugm
     * @date 2019-08-15
     * @param bd_x
     * @param bd_y
     * @return java.util.Map<java.lang.String, java.lang.Double>
     */
    public static Map<String, Double> getFromBaiduToGPS(double bd_x, double bd_y)
            throws Exception {
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("lon", bd_x);
        map.put("lat", bd_y);
        try {
            String result = getURLResult(bd_x, bd_y);
            Map<String, Double> mapStr = getXYMap(result);
            if (mapStr != null) {
                Double lon = 2 * bd_x - mapStr.get("X");
                Double lat = 2 * bd_y - mapStr.get("Y");

                // 如果转换后发现不是中国的经纬度，则不必处理。
                if (lon.doubleValue() < 140 && lon.doubleValue() > 60
                        && lat.doubleValue() > 2 && lat.doubleValue() < 60) {
                    map.put("lon", lon);
                    map.put("lat", lat);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    /**
     * 调用百度转GPS经纬度方法
     *
     * @author Qiugm
     * @date 2019-08-15
     * @param x
     * @param y
     * @return java.lang.String
     */
    private static String getURLResult(double x, double y) throws Exception {
        URL url = new URL("http://api.map.baidu.com/geoconv/v1/?coords=" + x
                + "," + y + "&from=1&to=5");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 设置输入流采用字节流
        connection.setDoInput(true);
        // 设置输出流采用字节流
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        // POST请求不能使用缓存
        connection.setUseCaches(false);
        OutputStreamWriter out = new OutputStreamWriter(connection
                .getOutputStream(), "utf-8");
        out.write("&ak=" + ak);
        out.flush();
        out.close();
        String res;
        InputStream l_urlStream;
        l_urlStream = connection.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                l_urlStream, "UTF-8"));
        StringBuilder sb = new StringBuilder("");
        while ((res = in.readLine()) != null) {
            sb.append(res.trim());
        }
        if (in != null) {
            in.close();
        }
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Double> getXYMap(String str) throws Exception {
        Map<String, Double> map = null;
        str = str.substring(str.indexOf("[") + 1, str.indexOf("]"));
        map = JsonUtil.parseObject(str, Map.class);
        return map;
    }

    /**
     * 地理编码
     *
     * @param address
     * @return
     */
    public static Map<String, Double> geocode(String address) {
        Map<String, Double> resultMap = new HashMap<String, Double>();
        try {
            String requestUrl = "http://api.map.baidu.com/geocoder/v2/?ak={ak}&address={address}&output=json";
            requestUrl = requestUrl.replace("{ak}", ak);
            requestUrl = requestUrl.replace("{address}", URLEncoder.encode(address, "UTF-8"));

            String result = HttpUtil.get(requestUrl);
            result = result.replace("showLocation&&showLocation(", "").replace(")", "");

            JSONObject jsonObj = JSONObject.parseObject(result);
            String status = jsonObj.getString("status");
            if (!StringUtils.isEmpty(status) && status.equalsIgnoreCase("0")) {
                JSONObject jsonResult = jsonObj.getJSONObject("result");
                JSONObject jsonLocation = jsonResult.getJSONObject("location");
                String lon = jsonLocation.getString("lng");
                String lat = jsonLocation.getString("lat");

                if (!StringUtils.isEmpty(lon) && !StringUtils.isEmpty(lat)) {
                    resultMap.put("lon", Double.valueOf(lon));
                    resultMap.put("lat", Double.valueOf(lat));
                }
            }
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return resultMap;
    }

    /**
     * 逆地理编码
     *
     * @param lng
     * @param lat
     * @param coordType
     *            坐标的类型，该接口目前支持的坐标类型包括：bd09ll（百度经纬度坐标）、
     *            gcj02ll（国测局经纬度坐标）、wgs84ll（ GPS经纬度）
     * @return
     */
    public static Map<String, String> geocode(String lng, String lat, String coordType) {
        Map<String, String> resultMap = new HashMap<String, String>(32);

        try {
            StringBuilder latLng = new StringBuilder();
            latLng.append(lat).append(",").append(lng);

            String requestUrl = "http://api.map.baidu.com/geocoder/v2/?ak={ak}&coordtype={coordtype}&location={location}&output=json&pois=0";
            requestUrl = requestUrl.replace("{ak}",
                    ak);
            requestUrl = requestUrl.replace("{coordtype}", coordType);
            requestUrl = requestUrl.replace("{location}", latLng);

            String result = HttpUtil.get(requestUrl);
            result = result.replace("renderReverse&&renderReverse(", "")
                    .replace(")", "");
            JSONObject jsonObj = JSONObject.parseObject(result);
            String status = jsonObj.getString("status");
            if (!StringUtils.isEmpty(status) && ("0").equals(status)) {
                JSONObject jsonResult = jsonObj.getJSONObject("result");
                String address = jsonResult.getString("formatted_address");
                resultMap.put("address", address);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return resultMap;
    }

    public static void main(String[] argv) throws IOException {
        String address = "广东省深圳市高新技术产业园区W1-A3（公司）  ";
        Map<String, Double> result = BaiduMapUtil.geocode(address);
        System.out.print("lon=" + result.get("lon"));
        System.out.print(" lat=" + result.get("lat"));
        Map<String, String> addResult = BaiduMapUtil.geocode("113.936", "22.528", "bd0911");
        System.out.println("address:" + addResult.get("address"));
    }

}
