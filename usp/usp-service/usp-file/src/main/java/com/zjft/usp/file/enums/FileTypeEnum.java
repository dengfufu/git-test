package com.zjft.usp.file.enums;

public enum FileTypeEnum {

    NONE(0, "未知类型"),
    IMAGE(1, "图片"),
    VIDEO(2, "视频"),
    AUDIO(3, "音频"),
    TEXT(4, "文本");

    private final int code;
    private final String name;

    FileTypeEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
