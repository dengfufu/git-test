package com.zjft.usp.zj.work.cases.atmcase.mapping.atmcase;

import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.ApiModel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-30 09:32
 **/
@ApiModel("ATM机CASE建立新平台与旧平台属性映射")
public abstract class CaseAddMapping {

    /**
     * 相同的属性名不需要在此处定义，可定义在DTO中
     * @return
     */
    public static Map<String,String> getNewAndOldPropertyMap(){
        Map<String,String> map = new HashMap<>();
        map.put("createByApp","isMobile");
        map.put("workStatus","ycasestatus");
        map.put("currentTime","currentTime");
        map.put("inspectionRequired","ifxj");
        map.put("serviceBranch","ocode");
        map.put("customId","usercode");
        map.put("isNewBranch","isNewBranch");
        map.put("deviceBranchName","wdname");
        map.put("workType","ycasetype");
        map.put("workSubType", "whTypeId");
        map.put("warrantyName","bxdate");
        map.put("modelId", "type1");
        map.put("serials","code");
        map.put("deviceCodes","atmcode");
        map.put("ifBook","isOrder");
        map.put("bookTime","yydatetime");
        map.put("faultTime","gzcxdatetime");
        map.put("reportTime","gzbxdatetime");
        map.put("traffic", "trafficid");
        map.put("trafficNote","trafficnote");
        map.put("planGoTime","ycdatetime");
        map.put("planArriveTime","yddatetime");
        map.put("actualArriveTime","sddatetime");
        map.put("softUpdate","hasupdate");
        map.put("softVersion","version1");
        map.put("spSoftVersion","versionsp");
        map.put("bvSoftVersion","version2");
        map.put("otherSoftVersion","version3");
        map.put("engineers", "gcspeople");
        map.put("engineerNames","gcsname");
        map.put("serviceRequest","bxdesc");
        return map;
    }

    public static Map<String,String> getOldAndNewPropertyMap(){
        Map<String,String> map = getNewAndOldPropertyMap();
        Map<String,String> oldAndNewMap = MapUtil.reverse(map);
        return oldAndNewMap;
    }
}
