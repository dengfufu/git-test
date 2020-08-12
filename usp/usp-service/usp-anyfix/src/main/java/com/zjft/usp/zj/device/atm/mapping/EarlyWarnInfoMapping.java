package com.zjft.usp.zj.device.atm.mapping;

import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.ApiModel;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备预警信息映射类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-28 16:11
 **/
@ApiModel(value = "设备预警信息新平台与旧平台属性的映射")
public abstract class EarlyWarnInfoMapping {
    /**
     * 相同的属性名不需要在此处定义，可定义在DTO中
     *
     * @return
     */
    public static Map<String, String> getNewAndOldPropertyMap() {
        Map<String, String> map = new HashMap<>();
        map.put("hasPhoto", "hasphoto");
        map.put("lastPm", "lastpm");
        map.put("defaultBarcodeConfirm", "defaultbarcode");
        map.put("defaultBar2StatusConfirm", "defaultbar2status");
        map.put("softVersion", "softversion");
        map.put("softVersionNew", "softversionnew");
        map.put("spSoftVersion", "spversion");
        map.put("spSoftVersionNew", "spversionnew");
        map.put("bvSoftVersion", "bvversion");
        map.put("bvSoftVersionNew", "bvversionnew");
        map.put("otherSoftVersion", "otherversion");
        return map;
    }

    public static Map<String, String> getOldAndNewPropertyMap() {
        Map<String, String> map = getNewAndOldPropertyMap();
        Map<String, String> oldAndNewMap = MapUtil.reverse(map);
        return oldAndNewMap;
    }
}
