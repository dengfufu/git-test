package com.zjft.usp.anyfix.work.remind.enums;

/**
 * 工单预警类型枚举类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-04-23 10:55
 **/
public enum WorkRemindTypeEnum {
    TO_HANDLE_EXPIRE(10, "分配超时"),
    TO_ASSIGN_EXPIRE(20, "派单超时"),
    TO_CLAIM_EXPIRE(30, "接单超时"),
    TO_SIGN_EXPIRE(40, "签到超时"),
    IN_SERVICE_EXPIRE(50, "服务超时");


    private Integer code;
    private String name;

    WorkRemindTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
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
     * 根据code获取name
     *
     * @param code
     * @return
     * @author Qiugm
     * @date 2020-04-23
     */
    public static String getNameByCode(Integer code) {
        for (WorkRemindTypeEnum workRemindTypeEnum : WorkRemindTypeEnum.values()) {
            if (workRemindTypeEnum.getCode() == code) {
                return workRemindTypeEnum.getName();
            }
        }
        return null;
    }

}
