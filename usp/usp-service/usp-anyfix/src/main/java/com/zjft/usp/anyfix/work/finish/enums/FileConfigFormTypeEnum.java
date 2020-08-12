package com.zjft.usp.anyfix.work.finish.enums;

/**
 * 工单费用录入状态枚举类
 *
 * @author zgpi
 * @date 2020/5/15 09:14
 */
public enum FileConfigFormTypeEnum {

    FINISH(1, "服务完成");


    /**
     * 状态编码
     **/
    private Integer code;
    /**
     * 状态名称
     **/
    private String name;

    FileConfigFormTypeEnum(Integer code, String name) {
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
     * @date 2020/5/15 09:16
     **/
    public static String getNameByCode(Integer code) {
        for (FileConfigFormTypeEnum statusEnum : FileConfigFormTypeEnum.values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum.getName();
            }
        }
        return null;
    }
}
