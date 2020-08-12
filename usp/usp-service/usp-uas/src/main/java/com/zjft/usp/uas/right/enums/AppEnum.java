package com.zjft.usp.uas.right.enums;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 应用枚举类
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/12/12 18:43
 */
public enum AppEnum {

    CLOUD(10001, "系统平台"),
    UAS(10002, "账户系统"),
    ANYFIX(10003, "工单系统"),
    WMS(10004, "物料系统"),
    DEVICE(10005, "设备系统"),
    DIP(10006, "数据系统"),
    PAY(10007, "支付系统");

    /**
     * 类型编码
     **/
    private int code;
    /**
     * 类型名称
     **/
    private String name;

    AppEnum(int code, String name) {
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
        for (AppEnum appEnum : AppEnum.values()) {
            if (code == appEnum.getCode()) {
                return appEnum.getName();
            }
        }
        return null;
    }

    /**
     * 获取所有应用
     *
     * @return
     */
    public static List<Integer> listApp() {
        return Arrays.stream(AppEnum.values())
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
