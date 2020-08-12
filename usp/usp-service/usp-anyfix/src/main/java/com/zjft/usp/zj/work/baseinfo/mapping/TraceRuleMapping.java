package com.zjft.usp.zj.work.baseinfo.mapping;

import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.ApiModel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author canlei
 * @version 1.0
 * @date 2020-03-26 15:54
 **/
@ApiModel("跟单规则新平台与旧平台属性的映射")
public class TraceRuleMapping {

    /**
     * 相同的属性名不需要在此处定义，可定义在DTO中
     *
     * @return
     */
    public static Map<String, String> getNewAndOldPropertyMap() {
        Map<String, String> map = new HashMap<>();
        map.put("workTypeName", "caseType");
        map.put("traceRequired", "isTrace");
        map.put("ruleContent", "faultContent");
        map.put("ruleId", "traceRuleid");
        return map;
    }

    public static Map<String, String> getOldAndNewPropertyMap() {
        Map<String, String> map = getNewAndOldPropertyMap();
        Map<String, String> oldAndNewMap = MapUtil.reverse(map);
        return oldAndNewMap;
    }

}
