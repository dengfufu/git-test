package com.zjft.usp.zj.work.cases.atmcase.mapping.atmcase;

import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: JFZOU
 * @Date: 2020-03-19 19:55
 * @Version 1.0
 */

@ApiModel(value = "ATM机CASE签到页面新平台与旧平台属性的映射，新旧平台属性都相同的不需要配置映射")
@Data
public abstract class CaseSignMapping {

    /**
     * 相同的属性名不需要在此处定义，可定义在DTO中
     * @return
     */
    public static Map<String,String> getNewAndOldPropertyMap(){
        Map<String,String> map = new HashMap<>();
        map.put("workCode","ycaseid");
        map.put("workTypeName","ycasetype");
        map.put("deviceBranchName","wdname");
        map.put("customName","bankname");
        map.put("brandName","cs");
        map.put("serviceRequest","bxdesc");
        map.put("bookTimeBegin","yydatetime");
        map.put("goTime","scdatetime");
        map.put("sysCreateTime", "opdatetime");
        map.put("serviceBranchName","bureauName");
        map.put("engineerNames","gcsname");
        return map;
    }

    public static Map<String,String> getOldAndNewPropertyMap(){
        Map<String,String> map = getNewAndOldPropertyMap();
        Map<String,String> oldAndNewMap = MapUtil.reverse(map);
        return oldAndNewMap;
    }

}
