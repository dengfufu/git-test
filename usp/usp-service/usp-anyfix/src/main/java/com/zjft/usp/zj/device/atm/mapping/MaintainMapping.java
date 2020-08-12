package com.zjft.usp.zj.device.atm.mapping;

import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.ApiModel;

import java.util.HashMap;
import java.util.Map;

/**
 * 维护记录映射类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-28 16:11
 **/
@ApiModel(value = "维护记录新平台与旧平台属性的映射")
public abstract class MaintainMapping {
    /**
     * 相同的属性名不需要在此处定义，可定义在DTO中
     *
     * @return
     */
    public static Map<String, String> getNewAndOldPropertyMap() {
        Map<String, String> map = new HashMap<>();
        map.put("recordId", "maintainrecordid");
        map.put("faultDate", "occurdate");
        map.put("warrantyStatus", "guaranteestatus");
        map.put("reportDatetime", "repairsdatetime");
        map.put("localeDateTime", "localedatetime");
        map.put("repairDateTime", "repairdatetime");
        map.put("faultType", "faulttype");
        map.put("faultModeName", "fautlmodename");
        map.put("faultCode", "faultcode");
        map.put("faultDesc", "faultdesc");
        map.put("softVersion", "softversion");
        map.put("spSoftVersion", "spversion");
        map.put("bvSoftVersion", "bvversion");
        map.put("otherSoftVersion", "otherversion");
        map.put("moneyCirculate", "moneyflow");
        map.put("processStep", "dealstep");
        map.put("processResult", "dealresult");
        map.put("engineerName", "engineer");
        map.put("engineerSignDate", "engineerdate");
        map.put("userComment", "useridea");
        map.put("userSign", "usersign");
        map.put("userSignDate", "userdate");
        map.put("userTel", "userphone");
        map.put("partNum", "partnum");
        return map;
    }

    public static Map<String, String> getOldAndNewPropertyMap() {
        Map<String, String> map = getNewAndOldPropertyMap();
        Map<String, String> oldAndNewMap = MapUtil.reverse(map);
        return oldAndNewMap;
    }
}
