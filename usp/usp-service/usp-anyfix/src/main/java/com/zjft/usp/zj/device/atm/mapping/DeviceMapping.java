package com.zjft.usp.zj.device.atm.mapping;

import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.ApiModel;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备映射类
 *
 * @author zgpi
 * @date 2020/3/25 13:38
 */
@ApiModel(value = "设备新平台与旧平台属性的映射")
public abstract class DeviceMapping {

    /**
     * 相同的属性名不需要在此处定义，可定义在DTO中
     *
     * @return
     */
    public static Map<String, String> getNewAndOldPropertyMap() {
        Map<String, String> map = new HashMap<>();
        map.put("deviceModel", "devicetype");
        map.put("deviceModelName", "devicetypename");
        map.put("deviceCode", "atmcode");
        map.put("serial", "devicecode");
        map.put("warrantyStatus", "guaranteestatus");
        map.put("deviceBranch", "branchid");
        map.put("branchName", "branchname");
        map.put("bankName", "bankname");
        map.put("bankSubBranchName", "subbankname");
        map.put("cityName", "cityname");
        map.put("pmConcernStatus", "pmconcernstatus");
        map.put("branchAddress", "branchaddress");
        map.put("contactName", "contactperson");
        map.put("contactPhone", "contacttelephone");
        map.put("needTime", "needtime");
        map.put("photoConfirmed", "photoconfirmed");
        map.put("monitorBrand", "monitorbrand");
        map.put("installDate", "installdate");
        map.put("openDate", "opendate");
        map.put("origManuArrivalDate", "dqdate");
        map.put("warrantyExpireDate", "dqdate2");
        map.put("actualManuArrivalDate", "actualdqdate");
        map.put("purchaseYear", "purchaseyear");
        map.put("moneyCirculate", "moneyflow");
        return map;
    }

    public static Map<String, String> getOldAndNewPropertyMap() {
        Map<String, String> map = getNewAndOldPropertyMap();
        Map<String, String> oldAndNewMap = MapUtil.reverse(map);
        return oldAndNewMap;
    }
}
