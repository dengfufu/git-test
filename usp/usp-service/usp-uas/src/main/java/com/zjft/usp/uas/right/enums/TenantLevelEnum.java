package com.zjft.usp.uas.right.enums;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 租户级别枚举类
 *
 * @author zrlin
 * @version 1.0
 * @date 2020/06/28 11:02
 */
public enum TenantLevelEnum {

    ORDINARY(1, "普通级别"),
    ADVANCED(2, "高级级别");


    /**
     * 类型编码
     **/
    private int code;
    /**
     * 类型名称
     **/
    private String name;

    TenantLevelEnum(int code, String name) {
        this.name = name;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 根据code获得name
     *
     * @param code
     * @return
     **/
    public static String getNameByCode(Integer code) {
        for (TenantLevelEnum levelEnum : TenantLevelEnum.values()) {
            if (code == levelEnum.getCode()) {
                return levelEnum.getName();
            }
        }
        return null;
    }

    /**
     * 获取所有级别
     *
     * @return
     */
    public static List<Integer> listApp() {
        return Arrays.stream(TenantLevelEnum.values())
                .map(appEnum -> appEnum.getCode()).collect(Collectors.toList());
    }

    /**
     * 编码与名称映射
     *
     * @param
     * @return
     **/
    public static Map<Integer, String> mapApp() {
        Map<Integer, String> map = new LinkedHashMap<>();
        List<Integer> list = listApp();
        for (int code : list) {
            map.put(code, getNameByCode(code));
        }
        return map;
    }
}
