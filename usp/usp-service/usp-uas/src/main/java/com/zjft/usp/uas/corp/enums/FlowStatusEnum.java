package com.zjft.usp.uas.corp.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 申请状态枚举类
 * @author canlei
 * @date 2019-08-15
 * @version 1.0
 */
public enum FlowStatusEnum {

    APPLY((short)1, "申请中"),
    PASS((short)2, "已通过"),
    REFUSE((short)3, "已拒绝");

    private final short code;
    private final String name;
    private Map<Short, String> flowStatusMap;

    FlowStatusEnum(short code, String name){
        this.code = code;
        this.name = name;
    }

    public short getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Map<Short, String> getFlowStatusMap(){
        FlowStatusEnum[] flowStatusEnumList = FlowStatusEnum.values();
        Map<Short, String> map = new HashMap<>();
        for(FlowStatusEnum flowStatusEnum:flowStatusEnumList){
            map.put(flowStatusEnum.code, flowStatusEnum.name);
        }
        return map;
    }

}
