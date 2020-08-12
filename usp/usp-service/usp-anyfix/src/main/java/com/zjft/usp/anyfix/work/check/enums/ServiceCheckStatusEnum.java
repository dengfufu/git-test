package com.zjft.usp.anyfix.work.check.enums;

/**
 * 服务审核状态枚举类
 *
 * @author zgpi
 * @date 2020/5/29 17:19
 */
public enum ServiceCheckStatusEnum {

    UN_CHECK(1, "待审核"),
    CHECK_PASS(2, "审核通过"),
    CHECK_REFUSE(3, "审核不通过");

    /**
     * 状态编码
     **/
    private Integer code;
    /**
     * 状态名称
     **/
    private String name;

    ServiceCheckStatusEnum(Integer code, String name) {
        this.name = name;
        this.code = code;
    }

    public Integer getCode() {
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
     * @date 2020/5/11 17:11
     **/
    public static String getNameByCode(Integer code) {
        for (ServiceCheckStatusEnum statusEnum : ServiceCheckStatusEnum.values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum.getName();
            }
        }
        return null;
    }
}
