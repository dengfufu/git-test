package com.zjft.usp.anyfix.work.auto.enums;

/**
 * 派单模式枚举类
 *
 * @author zgpi
 * @date 2019/10/12 11:05 上午
 **/
public enum AssignModeEnum {

    MANUAL(AssignModeEnum.MANUAL_CODE, "手动"),
    AUTO(AssignModeEnum.AUTO_CODE, "自动"),
    DUTY_SERVICE(AssignModeEnum.DUTY_SERVICE_CODE, "派给设备负责工程师"),
    TEAM_SERVICE(AssignModeEnum.TEAM_SERVICE_CODE,"派给小组"),
    DISTANCE_SERVICE(AssignModeEnum.DISTANCE_SERVICE_CODE, "距离优先"),
    NRANCHALL_SERVICE(AssignModeEnum.NRANCHALL_SERVICE_CODE, "派给网点所有人");

    public static final int MANUAL_CODE = 10;
    public static final int DUTY_SERVICE_CODE = 20;
    public static final int TEAM_SERVICE_CODE = 30;
    public static final int DISTANCE_SERVICE_CODE = 40;
    public static final int NRANCHALL_SERVICE_CODE = 50;
    public static final int AUTO_CODE = 60;
    /**
     * 服务方式代码
     */
    private int code;

    /**
     * 服务方式名称
     */
    private String name;

    AssignModeEnum(int code, String name) {
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
     * @author zgpi
     * @date 2019/10/11 10:15 上午
     * @param code
     * @return
     **/
    public static String getNameByCode(Integer code) {
        for (AssignModeEnum assignModeEnum : AssignModeEnum.values()) {
            if (code == assignModeEnum.getCode()) {
                return assignModeEnum.getName();
            }
        }
        return null;
    }
}
