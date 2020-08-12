package com.zjft.usp.anyfix.corp.config.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemConfig {

    public final static Map<Integer, String> WORK_FINISH_CONFIG__FORM_MAP = new HashMap<>();
    public final static Map<Integer, String> WORK_ADD_CONFIG_FORM_MAP = new HashMap<>();
    public final static Map<Integer, String> WORK_EDIT_CONFIG_FORM_MAP = new HashMap<>();

    static {
        WORK_FINISH_CONFIG__FORM_MAP.put(1, "brand");
        WORK_FINISH_CONFIG__FORM_MAP.put(2, "modelName");
        // 此处为校验项(一般为是否必填项)，如果为是否允许新增则不需要校验
//        WORK_FINISH_CONFIG__FORM_MAP.put(13, "specificationName");
//        WORK_FINISH_CONFIG__FORM_MAP.put(14, "modelName");
        WORK_ADD_CONFIG_FORM_MAP.put(4,"checkWorkCode");
        WORK_EDIT_CONFIG_FORM_MAP.put(5,"checkWorkCode");
    }

    public static List<Integer> getItemIdList(Map<Integer, String> map) {
        List<Integer> idList = new ArrayList<>();
        map.entrySet().stream().forEach( e-> idList.add(e.getKey()));
        return idList;
    }


}
