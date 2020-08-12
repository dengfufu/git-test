package com.zjft.usp.zj.device.atm.mapping;

import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.ApiModel;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备最近CASE映射类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-28 16:11
 **/
@ApiModel(value = "设备最近CASE新平台与旧平台属性的映射")
public abstract class RencentCaseMapping {

    /**
     * 相同的属性名不需要在此处定义，可定义在DTO中
     *
     * @return
     */
    public static Map<String, String> getNewAndOldPropertyMap() {
        Map<String, String> map = new HashMap<>();
        map.put("deviceCode", "atmcode");
        map.put("serial", "code");
        map.put("customName", "bankName");
        map.put("sysCreateTime", "opDateTime");
        map.put("serviceBranchName", "bureauName");
        map.put("modelName", "deviceTypeName");
        map.put("engineerNames", "gcsName");
        map.put("workCode", "ycaseId");
        map.put("workTypeName", "ycaseType");
        map.put("workStatusName", "ycaseStatus");
        map.put("deviceBranchName", "wdName");
        return map;
    }

    public static Map<String, String> getOldAndNewPropertyMap() {
        Map<String, String> map = getNewAndOldPropertyMap();
        Map<String, String> oldAndNewMap = MapUtil.reverse(map);
        return oldAndNewMap;
    }

}
