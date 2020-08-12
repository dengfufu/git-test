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

@ApiModel(value = "ATM机CASE明细页面新平台与旧平台属性的映射，" +
        "新旧平台属性都相同的不需要配置映射," +
        "由于旧平台有些变量命名本身可能在添加、修改、关闭都不统一，因此分开类定义")
public abstract class CaseDetailMapping {

    /**
     * 相同的属性名不需要在此处定义，可定义在DTO中
     * @return
     */
    public static Map<String,String> getNewAndOldPropertyMap(){
        Map<String,String> map = new HashMap<>();
        map.put("workTypeName","caseType");
        map.put("workTypeAllName","ycasetype");
        map.put("customId","usercode");
        map.put("customName","bankname");
        map.put("warrantyName","bxdate");
        map.put("workCode","ycaseid");
        map.put("serviceRequest","bxdesc");
        map.put("deviceBranchName","wdname");
        map.put("serial","code");
        map.put("serialNoWarranty", "devicecode");
        map.put("workStatusName","ycasestatus");
        map.put("brandName","cs");
        map.put("dealResult","dealresult");
        map.put("workSubType", "whtypeid");
        map.put("provinceId","provinceid");
        map.put("createByApp","ismobile");
        map.put("modelId", "type1");
        map.put("modelName","devicetypename");
        map.put("deviceCode","atmcode");
        map.put("serviceBranch", "ocode");
        map.put("serviceBranchName","bureauname");
        map.put("escortName","escortname");
        map.put("escortPhone","escortphone");
        map.put("traffic", "trafficid");
        map.put("trafficName","trafficname");
        map.put("trafficNote","trafficnote");
        map.put("faultTime","gzcxdatetime");
        map.put("reportTime","gzbxdatetime");
        map.put("sysCreateTime","opdatetime");
        map.put("bookTime","yydatetime");
        map.put("caseBelongCreateTime","ycdatetime");
        map.put("planArriveTime","yddatetime");
        map.put("planEndTime","ywdatetime");
        map.put("goTime","scdatetime");
        map.put("signTime","sddatetime");
        map.put("startTime","repairdatetime");
        map.put("endTime","swdatetime");
        map.put("finishDescription","closedesc");
        map.put("finishTime","closedatetime");
        map.put("faultCode","faultcode");
        map.put("engineers", "gcspeople");
        map.put("engineerNames","gcsname");
        map.put("creatorName","oppeoplename");
        map.put("contactName","bxpeople");
        map.put("contactPhone","lxtel");
        map.put("traceRequired","iftrace");
        map.put("inspectionRequired","ifxj");
        map.put("hasICReader","hasicreader");
        map.put("hasCaseResistVersion","hasCaseRegistVesion");
        map.put("moneyCircleStatus","zbhl_ktname");
        map.put("softUpdate","hasupdate");
        map.put("softUpdateName", "hasupdatename");
        map.put("softVersion","version1");
        map.put("softVersionName","version1name");
        map.put("spSoftVersion","spversion");
        map.put("spSoftVersionName","spversionname");
        map.put("bvSoftVersion","version2");
        map.put("bvSoftVersionName","version2name");
        map.put("otherSoftVersion","version3");
        map.put("otherSoftVersionName","version3name");
        map.put("monitorStatus","monitorstate");
        map.put("monitorStatusName","monitorstatename");
        map.put("monitorTime","setmonitime");
        map.put("monitorName","monitorname");
        map.put("cancelMonitor","cancelmonitor");
        map.put("cancelMonitorName","cancelmonitorname");
        map.put("cancelMonitorTime","cancelmonitime");
        return map;
    }

    public static Map<String,String> getOldAndNewPropertyMap(){
        Map<String,String> map = getNewAndOldPropertyMap();
        Map<String,String> oldAndNewMap = MapUtil.reverse(map);
        return oldAndNewMap;
    }

}
