package com.zjft.usp.push.model;


import lombok.Getter;
import lombok.Setter;

/**
 * @Author : zrLin
 * @Date : 2019年9月10日
 * @Desc : 用于单推信息
 */
@Getter
@Setter
public class SingleMessage extends Message {

    /** 对于推送的api，既是别名 */
    private String userId;

}
