package com.zjft.usp.zj.work.workorder.mapping;

import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.ApiModel;

import java.util.HashMap;
import java.util.Map;

/**
 * 新老平台派工单属性名映射类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-18 10:14
 **/
@ApiModel(value = "老平台派工单属性与新平台属性的映射，新旧平台属性都相同的不需要配置映射")
public class WorkOrderMapping {
    /**
     * 相同的属性名不需要在此处定义，可定义在DTO中
     * @return
     */
    public static Map<String, String> getNewAndOldPropertyMap() {
        Map<String, String> map = new HashMap<>();
        map.put("workOrderId", "id");
        return map;
    }

    public static Map<String, String> getOldAndNewPropertyMap() {
        Map<String, String> map = getNewAndOldPropertyMap();
        Map<String, String> oldAndNewMap = MapUtil.reverse(map);
        return oldAndNewMap;
    }
}
