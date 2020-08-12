package com.zjft.usp.auth.business.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author CK
 * @date 2019-09-19 16:24
 */
@Setter
@Getter
public class Token implements Serializable {

    private static final long serialVersionUID = 0L;

    /**
     * token的值
     */
    private String tokenValue;
    /**
     * 到期时间
     */
    private Date expiration;
    /**
     * 用户名
     */
    private String username;
    /**
     * 所属应用
     */
    private String clientId;
    /**
     * 授权类型
     */
    private String grantType;
}
