package com.zjft.usp.anyfix.baseinfo.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字段类型枚举类
 *
 * @author cxd
 * @date 2019/1/3 9:22
 * @Version 1.0
 **/
public enum FormTypeEnum {
    WORK_ADD(10, "建立工单"),
    WORK_HANDLE(20, "受理工单"),
    WORK_RECALL_ASSIGN(30, "撤回派单"),
    WORK_CLAIM(40, "认领工单"),
    WORK_REFUSE_ASSIGN(50, "拒绝工单"),
    WORK_RETURN_ASSIGN(60, "退回派单"),
    WORK_BOOKING(70, "预约工单"),
    WORK_SIGN(80, "现场签到"),
    WORK_FINISH(90, "完成工单 "),
    DEVICE_INFO(100, "设备档案");
    /**
     * 类型编码
     **/
    private int code;
    /**
     * 类型名称
     **/
    private String name;

    FormTypeEnum(int code, String name) {
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
     * @author zgpi
     * @date 2019/10/11 10:15 上午
     **/
    public static String getNameByCode(Integer code) {
        for (FormTypeEnum typeEnum : FormTypeEnum.values()) {
            if (code == typeEnum.getCode()) {
                return typeEnum.getName();
            }
        }
        return null;
    }

    /**
     * 获取所有状态
     *
     * @return
     */
    public static List<Integer> listFormType() {
        return Arrays.stream(FormTypeEnum.values())
                .map(nodeTypeEnum -> nodeTypeEnum.getCode()).collect(Collectors.toList());
    }
}
