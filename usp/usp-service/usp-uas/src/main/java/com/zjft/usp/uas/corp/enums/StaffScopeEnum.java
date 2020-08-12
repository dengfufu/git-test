package com.zjft.usp.uas.corp.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 人员规模
 * @author canlei
 * @date 2019-08-29
 */
public enum StaffScopeEnum {

    UNDER_FIFTY(1, "1~50人"),
    FIFTY_TO_HUNDRED(2, "51~100人"),
    ONE_TO_TWO_HUNDRED(3, "101~200人"),
    TWO_TO_FIVE_HUNDRED(4, "201~500人"),
    FIVE_HUNDRED_TO_THOUSAND(5, "501~1000人"),
    ABOVE_THOUSAND(6, ">1000人");

    private final int code;
    private final String name;

    StaffScopeEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public static final Map<Integer, String> map = new HashMap<>();

    static {
        for (StaffScopeEnum s : EnumSet.allOf(StaffScopeEnum.class)) {
            map.put(s.getCode(), s.getName());
        }
    }

    public int getCode(){
        return code;
    }

    public String getName(){
        return name;
    }

}
