package com.zjft.usp.zj.work.cases.atmcase.mapping.atmcase;

import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.ApiModel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author canlei
 * @version 1.0
 * @date 2020-03-26 19:47
 **/
@ApiModel(value = "ATM机取消页面新平台与旧平台属性的映射")
public abstract class CaseCancelMapping {

    /**
     * 相同的属性名不需要在此处定义，可定义在DTO中
     * @return
     */
    public static Map<String,String> getNewAndOldPropertyMap(){
        Map<String,String> map = new HashMap<>();
        map.put("workCode","ycaseid");
        map.put("cancelReason","closedesc");
        map.put("escortName","escortname");
        map.put("escortPhone","escortphone");
        map.put("cancelTime","canceltime");
        map.put("modTime","modtime");
        return map;
    }

    public static Map<String,String> getOldAndNewPropertyMap(){
        Map<String,String> map = getNewAndOldPropertyMap();
        Map<String,String> oldAndNewMap = MapUtil.reverse(map);
        return oldAndNewMap;
    }

}
