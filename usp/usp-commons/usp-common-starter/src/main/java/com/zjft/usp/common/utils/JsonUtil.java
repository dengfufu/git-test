package com.zjft.usp.common.utils;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * Json工具类
 *
 * @Author zgpi
 * @Date 2019-07-24 14:42
 * @Version 1.0
 **/
public class JsonUtil {
    static {
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
        ParserConfig.getGlobalInstance().setAsmEnable(false);
    }

    /**
     * 把JSON对象再包装一层
     *
     * @param key
     * @param objectJson
     * @return
     */
    public static String wrapJson(String key, String objectJson) {
        return "{\"" + key + "\":" + objectJson + "}";
    }

    /**
     * 将对象转换成JSON字符串
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        if (object == null) {
            return null;
        }
        return JSON.toJSONString(object);
    }

    /**
     * 将多个对象转换成JSON字符串 多个参数KEY和VALUE交替传入
     *
     * @param keysAndValues
     * @return
     */
    public static String toJsonString(Object... keysAndValues) {
        int len = keysAndValues.length;
        if (len == 1) {
            return JSON.toJSONString(keysAndValues[0]);
        }
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < len / 2; i++) {
            String key = keysAndValues[i * 2].toString();
            Object value = keysAndValues[i * 2 + 1];
            jsonObject.put(key, value);
        }
        return jsonObject.toString();
    }

    /**
     * 将JSON数组转换成对象列表
     *
     * @param jsonArray
     * @param classObject
     * @return
     */
    public static <T> List<T> parseArray(String jsonArray, Class<T> classObject) {
        return JSONObject.parseArray(jsonArray, classObject);
    }

    /**
     * 将JSON字符串转换成对象
     *
     * @param json
     * @param classObject
     * @return
     */
    public static <T> T parseObject(String json, Class<T> classObject) {
        return JSONObject.parseObject(json, classObject);
    }

    /**
     * 将JSON字符串转换成对象，保留顺序
     *
     * @param json
     * @param classObject
     * @return
     */
    public static <T> T parseObjectSort(String json, Class<T> classObject) {
        return JSONObject.parseObject(json, classObject, Feature.OrderedField);
    }

    /**
     * 获得指定的JSON属性值
     *
     * @param json
     * @param key
     * @return
     */
    public static Object parseValue(String json, String key) {
        @SuppressWarnings("unchecked")
        Map<String, Object> map = JSONObject.parseObject(json, Map.class);
        if (map == null || map.size() == 0) {
            return null;
        }
        Object t = map.get(key);
        return t;
    }

    /**
     * 获得指定的JSON属性整数值
     *
     * @param json
     * @param key
     * @return
     */
    public static String parseString(String json, String key) {
        Object val = parseValue(json, key);
        if (val == null) {
            return "";
        }
        if (val instanceof String) {
            return (String) val;
        }
        return "";
    }

    /**
     * 获得指定的JSON属性整数值
     *
     * @param json
     * @param key
     * @return
     */
    public static int parseInt(String json, String key) {
        Object val = parseValue(json, key);
        if (val == null) {
            return 0;
        }
        if (val instanceof Integer) {
            return (Integer) val;
        }
        if (val instanceof String) {
            return Integer.parseInt((String) val, 10);
        }
        return 0;
    }

    /**
     * @return
     * @Description 解析Json字符串中的对象
     * @Author zgpi
     * @Date 2019-07-25 16:17
     * @Param
     **/
    public static <T> T parseJsonObject(String json, String objName, Class<T> classObject) {
        JSONObject jsonObject = JSON.parseObject(json);
        if (jsonObject != null) {
            return parseObject(String.valueOf(jsonObject.get(objName)), classObject);
        }
        return null;
    }

    /**
     * Json字符串转map
     *
     * @param json
     * @return
     * @author zgpi
     * @date 2019/11/20 14:44
     **/
    public static Map parseMap(String json) {
        return JSONObject.parseObject(json, Map.class);
    }

    /**
     * Json字符串转map，保留顺序
     *
     * @param json
     * @return
     * @author zgpi
     * @date 2020/3/27 13:54
     **/
    public static Map parseMapSort(String json) {
        return JSONObject.parseObject(json, Map.class, Feature.OrderedField);
    }

    private JsonUtil() {

    }

}
