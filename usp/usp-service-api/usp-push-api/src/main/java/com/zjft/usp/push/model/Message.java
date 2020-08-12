package com.zjft.usp.push.model;


import lombok.Data;

import java.util.Map;

/**
 * @Author : zrLin
 * @Date : 2019年9月10日
 * @Desc : 用于极光消息
 */
@Data
public class Message {

    /** 标题 */
    private String title;
    /** 标题内容 */
    private String content;
    /** 消息 */
    private Map<String,String> extraMap;
    /** 消息类型 */
    private int type;

}
