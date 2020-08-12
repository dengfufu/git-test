package com.zjft.usp.zj.work.baseinfo.mapping;

import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.ApiModel;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备网点映射类
 *
 * @author zgpi
 * @date 2020/3/24 17:40
 */
@ApiModel(value = "设备网点新平台与旧平台属性的映射")
public abstract class DeviceBranchMapping {

    /**
     * 相同的属性名不需要在此处定义，可定义在DTO中
     *
     * @return
     */
    public static Map<String, String> getNewAndOldPropertyMap() {
        Map<String, String> map = new HashMap<>();
        map.put("branchId", "branchid");
        map.put("branchName", "branchname");
        map.put("bankCode", "bankcode");
        map.put("bankName", "bankname");
        map.put("bankSubBranchName", "subbankname");
        return map;
    }

    public static Map<String, String> getOldAndNewPropertyMap() {
        Map<String, String> map = getNewAndOldPropertyMap();
        Map<String, String> oldAndNewMap = MapUtil.reverse(map);
        return oldAndNewMap;
    }
}
