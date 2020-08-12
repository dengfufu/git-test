package com.zjft.usp.msg.model;

import lombok.Data;

import java.util.Map;

/**
 * @Author : zrLin
 * @Date : 2019年9月10日
 * @Desc : 用于模板消息
 */
@Data
public class TemplateMessage {

    private Integer appId;
    private String tplName;
    private String title;
    private String userIds;
    private String webUrl;
    private String appUrl;
    private String wechatUrl;
    private Map<String, Object> dataMap;
}
