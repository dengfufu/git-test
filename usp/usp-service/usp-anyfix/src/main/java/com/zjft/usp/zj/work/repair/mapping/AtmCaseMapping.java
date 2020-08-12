package com.zjft.usp.zj.work.repair.mapping;

import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.ApiModel;

import java.util.HashMap;
import java.util.Map;


/**
 * 新旧平台CASE实体属性名转换
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-31 09:15
 **/

@ApiModel(value = "对应ATM系统YjcaseRemote实体类，用于新旧平台属性名映射转换。新旧平台属性都相同的不需要配置映射")
public abstract class AtmCaseMapping {

    /**
     * 相同的属性名不需要在此处定义，可定义在DTO中
     * @return
     */
    public static Map<String, String> getNewAndOldPropertyMap() {
        Map<String, String> map = new HashMap<>();
        map.put("deviceCodes", "atmcode");
        map.put("serials", "code");
        map.put("customName", "bankName");
        map.put("serviceBranchName", "bureauName");
        map.put("warrantyName", "bxdate");
        map.put("serviceRequest", "bxdesc");
        map.put("modelId", "type1");
        map.put("modelName", "atmTypeName");
        map.put("brandName", "cs");
        map.put("faultCode", "faultcode");
        map.put("engineers", "gcspeople");
        map.put("engineerNames", "gcsname");
        map.put("faultTime", "gzcxdatetime");
        map.put("traceRequired", "iftrace");
        map.put("inspectionRequired", "ifxj");
        map.put("planGoTime", "ycdatetime");
        map.put("planArrivalTime", "yddatetime");
        map.put("bookTime", "yydatetime");
        map.put("goTime", "scdatetime");
        map.put("signTime", "sddatetime");
        map.put("startTime", "repairDateTime");
        map.put("endTime", "swdatetime");
        map.put("finishTime", "closedatetime");
        map.put("traffic", "trafficid");
        map.put("trafficNote", "trafficnote");
        map.put("workCode", "ycaseid");
        map.put("workTypeName", "ycasetype");
        map.put("workSubType", "whTypeId");
        map.put("workStatusName", "ycasestatus");
        map.put("deviceBranchName", "wdname");
        return map;
    }

    public static Map<String, String> getOldAndNewPropertyMap() {
        Map<String, String> map = getNewAndOldPropertyMap();
        Map<String, String> oldAndNewMap = MapUtil.reverse(map);
        return oldAndNewMap;
    }

}
