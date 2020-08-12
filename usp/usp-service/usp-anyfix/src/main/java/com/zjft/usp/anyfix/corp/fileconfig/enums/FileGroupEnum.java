package com.zjft.usp.anyfix.corp.fileconfig.enums;

/**
 * 是否可用枚举类
 *
 * @author zgpi
 * @date 2019/10/16 3:10 下午
 **/
public enum FileGroupEnum {

    DEVICE_PHOTO("1", "设备照片");

    /**
     * 代码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    FileGroupEnum(String code, String name) {
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
     * @param code
     * @return
     */
    public static String getNameByCode(String code) {
        for (FileGroupEnum fileGroupEnum : FileGroupEnum.values()) {
            if (fileGroupEnum.getCode().equals(code)) {
                return fileGroupEnum.getName();
            }
        }
        return null;
    }
}
