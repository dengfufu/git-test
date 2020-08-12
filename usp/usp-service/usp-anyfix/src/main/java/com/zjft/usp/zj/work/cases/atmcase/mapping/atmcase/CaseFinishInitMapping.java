package com.zjft.usp.zj.work.cases.atmcase.mapping.atmcase;

import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.ApiModel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author canlei
 * @version 1.0
 * @date 2020-03-26 09:56
 **/
@ApiModel(value = "ATM机CASE关闭页面初始化新平台与旧平台属性的映射，新旧平台属性都相同的不需要配置映射")
public abstract class CaseFinishInitMapping {

    /**
     * 相同的属性名不需要在此处定义，可定义在DTO中
     * @return
     */
    public static Map<String,String> getNewAndOldPropertyMap(){
        Map<String,String> map = new HashMap<>();
        map.put("workCode","ycaseid");
        map.put("workTypeName","ycasetype");
        map.put("workSubType", "whtypeid");
        map.put("workTypeAllName", "yjcaseTypeName");
        map.put("deviceBranchName", "wdname");
        map.put("modelId", "type1");
        map.put("modelName", "tname");
        map.put("serviceRequest","bxdesc");
        map.put("traffic", "trafficid");
        map.put("trafficNote", "trafficnote");
        map.put("workStatusName", "ycasestatus");
        map.put("customId", "usercode");
        map.put("customName","bankname");
        map.put("serviceBranch", "ocode");
        map.put("yoCodeDepot", "yocodedepot");
        map.put("serviceBranchName","bureauName");
        map.put("deviceCode", "atmcode");
        map.put("serial", "code");
        map.put("brandName","cs");
        map.put("warrantyName","bxdate");
        map.put("softVersion", "version1");
        map.put("spSoftVersion", "spversion");
        map.put("bvSoftVersion", "version2");
        map.put("otherSoftVersion", "version3");
        map.put("faultTime","gzcxdatetime");
        map.put("reportTime","gzbxdatetime");
        map.put("faultCode","faultcode");
        map.put("faultModuleId","fmoduleid");
        map.put("planGoTime","ycdatetime");
        map.put("planArriveTime","yddatetime");
        map.put("goTime","scdatetime");
        map.put("signTime","sddatetime");
        map.put("finishDescription", "closedesc");
        map.put("planEndTime","ywdatetime");
        map.put("engineers", "gcspeople");
        map.put("engineerNames", "gcsname");
        map.put("startTime", "repairDateTime");
        map.put("endTime", "swdatetime");
        map.put("traceRequired","iftrace");
        map.put("traceRule", "caseTraceRule");
        map.put("otherTraceRule", "caseTraceRuleOther");
        map.put("inspectionRequired","ifxj");
        map.put("mobileFinish", "isMoClose");
        map.put("isPartReplace", "ispartreplace");
        map.put("dealResultId", "dealresultid");
        map.put("dealResultName", "dealresultname");
        map.put("newDeviceCode", "newdevid");
        map.put("manMade", "manmaded");
        map.put("dealWay", "dealway");
        map.put("needIcbcImage", "needicbcimage");
        map.put("newModelId", "newType1");
        map.put("newSerial", "newCode");
        map.put("unRelatedNote", "unrelatedDesc");
        map.put("finishNote", "closedesc");
        return map;
    }

    public static Map<String,String> getOldAndNewPropertyMap(){
        Map<String,String> map = getNewAndOldPropertyMap();
        Map<String,String> oldAndNewMap = MapUtil.reverse(map);
        return oldAndNewMap;
    }

}
