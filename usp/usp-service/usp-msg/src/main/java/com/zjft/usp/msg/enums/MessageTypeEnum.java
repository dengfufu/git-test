package com.zjft.usp.msg.enums;

/**
 * 消息类型枚举类
 *
 * @author zgpi
 * @date 2019/11/20 09:28
 **/
public enum MessageTypeEnum {

    MESSAGE_ORDINARY(1, "发送普通消息"),
    MESSAGE_TEMPLATE(2, "发送模板消息"),
    MESSAGE_NOTICE(3, "发送通知"),
    MESSAGE_BULLETIN(4, "发送公告");

    /**
     * 代码
     */
    private int code;

    /**
     * 名称
     */
    private String name;

    MessageTypeEnum(int code, String name) {
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

}
