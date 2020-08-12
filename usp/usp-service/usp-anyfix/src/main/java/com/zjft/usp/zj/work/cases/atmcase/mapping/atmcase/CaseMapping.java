package com.zjft.usp.zj.work.cases.atmcase.mapping.atmcase;

import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.ApiModel;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: JFZOU
 * @Date: 2020-03-19 19:55
 * @Version 1.0
 */

@ApiModel(value = "ATM机CASE添加、修改、关闭页面新平台与旧平台属性的映射，" +
        "新旧平台属性都相同的不需要配置映射," +
        "由于旧平台有些变量命名本身可能在添加、修改、关闭都不统一，如果有，建议分开类定义")
public abstract class CaseMapping {

    /**
     * 相同的属性名不需要在此处定义，可定义在DTO中
     * @return
     */
    public static Map<String,String> getNewAndOldPropertyMap(){
        Map<String,String> map = new HashMap<>();
        map.put("deviceCodes","atmcode");
        map.put("serial","code");
        map.put("customName","bankname");
        map.put("serviceBranch", "ocode");
        map.put("serviceBranchName","bureauName");
        map.put("warrantyName","bxdate");
        map.put("serviceRequest","bxdesc");
        map.put("modelId", "type1");
        map.put("modelName", "tname");
        map.put("brandName","cs");
        map.put("faultCode","faultcode");
        map.put("engineers", "gcspeople");
        map.put("engineerNames","gcsname");
        map.put("faultTime","gzcxdatetime");
        map.put("traceRequired","iftrace");
        map.put("inspectionRequired","ifxj");
        map.put("planGoTime","ycdatetime");
        map.put("planArrivalTime","yddatetime");
        map.put("bookTime","yydatetime");
        map.put("goTime","scdatetime");
        map.put("signTime","sddatetime");
        map.put("startTime","repairDateTime");
        map.put("endTime","swdatetime");
        map.put("finishTime","closedatetime");
        map.put("traffic", "trafficid");
        map.put("trafficNote","trafficnote");
        map.put("workCode","ycaseid");
        map.put("workTypeName","ycasetype");
        map.put("workSubType", "whTypeId");
        map.put("workTypeAllName", "yjcaseTypeName");
        map.put("workStatusName","ycasestatus");
        map.put("deviceBranchName","wdname");
        map.put("sysCreateTime","opdatetime");
        return map;
    }

    public static Map<String,String> getOldAndNewPropertyMap(){
        Map<String,String> map = getNewAndOldPropertyMap();
        Map<String,String> oldAndNewMap = MapUtil.reverse(map);
        return oldAndNewMap;
    }

}
