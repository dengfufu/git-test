package com.zjft.usp.zj.work.cases.atmcase.mapping.atmcase;

import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.ApiModel;

import java.util.HashMap;
import java.util.Map;

/**
 * 派工单新平台与旧平台属性映射
 * @author zgpi
 * @date 2020-4-7 9:57
 **/
@ApiModel("派工单新平台与旧平台属性映射")
public abstract class WorkOrderMapping {

    /**
     * 相同的属性名不需要在此处定义，可定义在DTO中
     * @return
     */
    public static Map<String,String> getNewAndOldPropertyMap(){
        Map<String,String> map = new HashMap<>();
        map.put("workCode","ycaseId");
        map.put("workType","ycasetype");
        map.put("serviceBranch","ocode");
        map.put("serviceBranchName","bureauName");
        map.put("customId","usercode");
        map.put("customName","bankname");
        map.put("deviceBranchName","wdname");
        map.put("modelId","type1");
        map.put("modelName","deviceTypeName");
        map.put("serial","code");
        map.put("deviceCode","atmcode");
        map.put("serviceRequest","bxdesc");
        map.put("ifBook","isOrder");
        map.put("bookTime","yydatetime");
        map.put("faultTime","gzcxdatetime");
        map.put("reportTime","gzbxdatetime");
        return map;
    }

    public static Map<String,String> getOldAndNewPropertyMap(){
        Map<String,String> map = getNewAndOldPropertyMap();
        Map<String,String> oldAndNewMap = MapUtil.reverse(map);
        return oldAndNewMap;
    }
}
