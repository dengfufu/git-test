package com.zjft.usp.zj.device.atm.mapping;

import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.ApiModel;

import java.util.HashMap;
import java.util.Map;

/**
 * 维护PM记录映射类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-28 16:11
 **/
@ApiModel(value = "维护PM记录新平台与旧平台属性的映射")
public abstract class MaintainPmMapping {
    /**
     * 相同的属性名不需要在此处定义，可定义在DTO中
     *
     * @return
     */
    public static Map<String, String> getNewAndOldPropertyMap() {
        Map<String, String> map = new HashMap<>();
        map.put("recordId", "recordid");
        map.put("faultDesc", "faultdesc");
        return map;
    }

    public static Map<String, String> getOldAndNewPropertyMap() {
        Map<String, String> map = getNewAndOldPropertyMap();
        Map<String, String> oldAndNewMap = MapUtil.reverse(map);
        return oldAndNewMap;
    }
}
