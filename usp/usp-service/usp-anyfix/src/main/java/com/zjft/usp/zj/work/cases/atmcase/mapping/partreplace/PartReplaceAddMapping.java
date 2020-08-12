package com.zjft.usp.zj.work.cases.atmcase.mapping.partreplace;

import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.ApiModel;

import java.util.HashMap;
import java.util.Map;

/**
 * 备件更换新平台与旧平台属性映射
 * @author zgpi
 * @date 2020-4-4 19:36
 **/
@ApiModel("备件更换新平台与旧平台属性映射")
public abstract class PartReplaceAddMapping {

    /**
     * 相同的属性名不需要在此处定义，可定义在DTO中
     * @return
     */
    public static Map<String,String> getNewAndOldPropertyMap(){
        Map<String,String> map = new HashMap<>();
        map.put("workCode","ycaseId");
        map.put("workTypeAllName","yjcaseTypeName");
        map.put("serviceBranch","caseDepot");
        map.put("deviceModel","machineType");
        map.put("serial","machineCode");
        map.put("partCode","zcode");
        map.put("partName","zcodeName");
        map.put("replaceSource","useSource");
        map.put("newBarCode","newBarcode");
        map.put("oldBarCode","oldBarcode");
        map.put("upPartId","selectUpPartId");
        return map;
    }

    public static Map<String,String> getOldAndNewPropertyMap(){
        Map<String,String> map = getNewAndOldPropertyMap();
        Map<String,String> oldAndNewMap = MapUtil.reverse(map);
        return oldAndNewMap;
    }
}
