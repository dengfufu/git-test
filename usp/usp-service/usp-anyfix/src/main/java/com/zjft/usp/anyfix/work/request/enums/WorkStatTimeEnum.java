package com.zjft.usp.anyfix.work.request.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 时间选择枚举类
 *
 * @author zrlin
 * @version 1.0
 * @date 2019/11/01 16:55
 **/
public enum WorkStatTimeEnum {

    THIS_WEEK(10,"本周"),
    THIS_MONTH(20, "本月"),
    LAST_MONTH(30, "上月"),
    THIS_QUARTER(40, "本季度"),
    LAST_THREE_MONTH(50, "最近三个月"),
    THIS_YEAR(60, "本年"),
    LAST_ONE_YEAR(70, "本季度");

    /**
     * 状态编码
     **/
    private Integer code;
    /**
     * 状态名称
     **/
    private String name;

    WorkStatTimeEnum(Integer code, String name) {
        this.name = name;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(Integer code) {
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
     * @author zgpi
     * @date 2019/10/11 10:15 上午
     **/
    public static String getNameByCode(Integer code) {
        for (WorkStatTimeEnum statusEnum : WorkStatTimeEnum.values()) {
            if (statusEnum.getCode() == code) {
                return statusEnum.getName();
            }
        }
        return null;
    }

    /**
     * 运行中的状态列表，不包含待评价
     * @author zgpi
     * @date 2019/10/15 10:20 上午
     * @param 
     * @return
     **/
    public static List<Integer> getTimeStatList() {
        List<Integer> list = new ArrayList<>();
        list.add(WorkStatTimeEnum.THIS_WEEK.getCode());
        list.add(WorkStatTimeEnum.THIS_MONTH.getCode());
        list.add(WorkStatTimeEnum.LAST_MONTH.getCode());
        list.add(WorkStatTimeEnum.THIS_QUARTER.getCode());
        list.add(WorkStatTimeEnum.LAST_THREE_MONTH.getCode());
        list.add(WorkStatTimeEnum.THIS_YEAR.getCode());
        list.add(WorkStatTimeEnum.LAST_ONE_YEAR.getCode());
        return list;
    }
}
