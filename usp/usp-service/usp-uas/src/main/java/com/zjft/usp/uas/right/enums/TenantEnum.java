package com.zjft.usp.uas.right.enums;

/**
 * 租户类型枚举类
 *
 * @author zgpi
 * @date 2020/6/17 16:44
 */
public enum TenantEnum {

    SERVICE_DEMANDER("service_demander", "委托商"),
    SERVICE_PROVIDER("service_provider", "服务商"),
    DEVICE_USER("device_user", "设备使用商"),
    CLOUD_MANAGER("cloud_manager", "平台管理");

    /**
     * 类型编码
     **/
    private String code;
    /**
     * 类型名称
     **/
    private String name;

    TenantEnum(String code, String name) {
        this.name = name;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
    public static String getNameByCode(String code) {
        for (TenantEnum tenantEnum : TenantEnum.values()) {
            if (tenantEnum.getCode().equalsIgnoreCase(code)) {
                return tenantEnum.getName();
            }
        }
        return null;
    }
}
