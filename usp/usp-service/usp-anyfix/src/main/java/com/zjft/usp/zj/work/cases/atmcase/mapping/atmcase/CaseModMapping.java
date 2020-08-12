package com.zjft.usp.zj.work.cases.atmcase.mapping.atmcase;

import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.ApiModel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author canlei
 * @version 1.0
 * @date 2020-03-25 13:36
 **/
@ApiModel(value = "ATM机CASE修改新平台与旧平台属性映射")
public abstract class CaseModMapping {

    public static Map<String, String> getNewAndOldPropertyMap() {
        Map<String,String> map = new HashMap<>();
        map.put("workCode","ycaseId");
        map.put("createByApp","isMobile");
        map.put("inspectionRequired","ifxj");
        map.put("workTypeName","ycasetype");
        map.put("workSubType", "whTypeId");
        map.put("serviceBranch", "ocode");
        map.put("serviceBranchName","bureauname");
        map.put("customId","usercode");
        map.put("customName","bankname");
        map.put("deviceBranch", "branchid");
        map.put("deviceBranchName","wdname");
        map.put("warrantyName","bxdate");
        map.put("modelId", "type1");
        map.put("modelName","devicetypename");
        map.put("serial","code");
        map.put("deviceCode","atmcode");
        map.put("newDeviceCode", "newdevid");
        map.put("faultTime","gzcxdatetime");
        map.put("reportTime","gzbxdatetime");
        map.put("traffic", "trafficid");
        map.put("trafficName","trafficname");
        map.put("trafficNote","trafficnote");
        map.put("planGoTime","ycdatetime");
        map.put("planArriveTime","yddatetime");
        map.put("goTime","scdatetime");
        map.put("signTime","sddatetime");
        map.put("softUpdate","hasupdate");
        map.put("softVersion","version1");
        map.put("spSoftVersion","versionsp");
        map.put("bvSoftVersion","version2");
        map.put("otherSoftVersion","version3");
        map.put("engineers", "gcspeople");
        map.put("engineerNames", "gcsname");
        map.put("serviceRequest","bxdesc");
        map.put("modTime","modtime");
        return map;
    }

    public static Map<String,String> getOldAndNewPropertyMap(){
        Map<String,String> map = getNewAndOldPropertyMap();
        Map<String,String> oldAndNewMap = MapUtil.reverse(map);
        return oldAndNewMap;
    }

}
