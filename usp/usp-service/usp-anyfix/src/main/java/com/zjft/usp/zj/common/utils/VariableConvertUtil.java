package com.zjft.usp.zj.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.zjft.usp.common.utils.JsonUtil;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 新老平台变量转换工具类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-17 10:59
 **/
public class VariableConvertUtil {
    /**
     * 转换成新平台使用的对象
     * @author JFZOU
     * @date 2020-03-19
     * @param jsonData 已经取得的指定的JSON字符串
     * @param oldAndNewPropertyMap 老平台与新平台属性名称映射
     * @param clazz 新平台的对象类型
     * @param <T>
     * @return
     */
    public static <T> T convertToNewEntity(String jsonData, Map<String, String> oldAndNewPropertyMap, Class<T> clazz) {
        Map<String, String> dataMap = JsonUtil.parseMap(jsonData);
        for (String oldKey : oldAndNewPropertyMap.keySet()) {
            MapUtil.renameKey(dataMap, oldKey, oldAndNewPropertyMap.get(oldKey));
        }
        return BeanUtil.toBean(dataMap, clazz);
    }

    /**
     * 转换成老平台使用的请求参数
     * @author JFZOU
     * @date 2020-03-19
     * @param srcEntity 源对象
     * @param newAndOldPropertyMap 新旧平台的映射
     * @return
     */
    public static MultiValueMap<String, Object> convertToOldEntity(Object srcEntity,Map<String, String> newAndOldPropertyMap){
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        Map<String, Object> srcEntityMap = BeanUtil.beanToMap(srcEntity);
        for (String newKey : srcEntityMap.keySet()) {
            String oldKey = newAndOldPropertyMap.get(newKey);
            if (StrUtil.isEmpty(oldKey)) {
                /**如果不存在映射，保留原来的newKey命名*/
                oldKey = newKey;
            }
            paramMap.add(oldKey, srcEntityMap.get(newKey));
        }

        return paramMap;
    }

    /**
     * 转换成新平台使用的对象
     * @author JFZOU
     * @date 2020-03-30
     * @param srcEntity 源对象
     * @return
     */
    public static MultiValueMap<String, Object> convertToOldEntity(Object srcEntity){
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        Map<String, Object> srcEntityMap = BeanUtil.beanToMap(srcEntity);
        for (String newKey : srcEntityMap.keySet()){
            paramMap.add(newKey,srcEntityMap.get(newKey));
        }
        return paramMap;
    }

    /**
     * 转换成新平台使用的对象列表
     *
     * @author Qiugm
     * @date 2020-03-30
     * @param jsonArrayData 已经取得的指定的JSONArray字符串
     * @param oldAndNewPropertyMap  老平台与新平台属性名称映射
     * @param <T> 新平台的对象类型
     * @return
     */
    public static <T> List<T> convertToNewEntityList(String jsonArrayData,
                                                     Map<String, String> oldAndNewPropertyMap, Class<T> clazz) {
        List<T> entityList = new ArrayList<>();
        if (StrUtil.isNotEmpty(jsonArrayData)) {
            List<Object> objList = JsonUtil.parseArray(jsonArrayData, Object.class);
            if (objList != null && !objList.isEmpty()) {
                for (Object obj : objList) {
                    T entity = convertToNewEntity(JsonUtil.toJson(obj), oldAndNewPropertyMap, clazz);
                    if (entity != null) {
                        entityList.add(entity);
                    }
                }
            }
        }
        return entityList;
    }

}
