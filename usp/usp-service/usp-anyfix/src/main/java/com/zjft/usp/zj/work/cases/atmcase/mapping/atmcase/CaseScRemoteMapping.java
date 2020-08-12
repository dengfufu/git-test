package com.zjft.usp.zj.work.cases.atmcase.mapping.atmcase;

import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.ApiModel;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-04-01 15:33
 **/
@ApiModel("老平台SC应用ATM机CASE新平台与旧平台属性映射")
public abstract class CaseScRemoteMapping {

    /**
     * 相同的属性名不需要在此处定义，可定义在DTO中
     * @return
     */
    public static Map<String,String> getNewAndOldPropertyMap(){
        Map<String,String> map = new HashMap<>();
        map.put("deviceCodes","atmcode");
        map.put("serial","code");
        map.put("customName","bankName");
        map.put("serviceBranchName","bureauName");
        map.put("warrantyName","bxDate");
        map.put("serviceRequest","bxDesc");
        map.put("modelId", "type1");
        map.put("modelName", "deviceTypeName");
        map.put("brandName","cs");
        map.put("faultCode","faultcode");
        map.put("engineers", "gcsPeople");
        map.put("engineerNames","gcsName");
        map.put("faultTime","gzcxDateTime");
        map.put("traceRequired","ifTrace");
        map.put("inspectionRequired","ifxj");
        map.put("reportTime","gzbxDateTime");
        map.put("sysCreateTime","opDateTime");
        map.put("planGoTime","ycDateTime");
        map.put("planArrivalTime","ydDateTime");
        map.put("bookTime","yyDateTime");
        map.put("goTime","scDateTime");
        map.put("signTime","sdDateTime");
        map.put("startTime","repairDateTime");
        map.put("endTime","swDateTime");
        map.put("finishTime","closeDateTime");
        map.put("traffic", "trafficId");
        map.put("trafficNote","trafficNote");
        map.put("workCode","ycaseId");
        map.put("workTypeName","ycaseType");
        map.put("workSubType", "whTypeId");
        map.put("workTypeAllName", "yjcaseTypeName");
        map.put("workStatusName","ycaseStatus");
        map.put("deviceBranchName","wdName");
        return map;
    }

    public static Map<String,String> getOldAndNewPropertyMap(){
        Map<String,String> map = getNewAndOldPropertyMap();
        Map<String,String> oldAndNewMap = MapUtil.reverse(map);
        return oldAndNewMap;
    }

}
