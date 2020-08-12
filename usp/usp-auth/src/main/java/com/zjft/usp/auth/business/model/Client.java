package com.zjft.usp.auth.business.model;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author CK
 * @date 2019-09-19 16:24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("oauth_client_details")
public class Client extends Model<Client> {

    private static final long serialVersionUID = 0L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String clientId;

    private String resourceIds = "";

    @JsonIgnore
    private String clientSecret;

    private String scope = "all";

    private String authorizedGrantTypes = "authorization_code,password,refresh_token,client_credentials";

    private String webServerRedirectUri;

    private String authorities = "";

    private Integer accessTokenValidity = 18000;

    private Integer refreshTokenValidity = 28800;

    private String additionalInformation = "{}";

    private String autoapprove = "true";

    // 企业租户id
    private Long corpId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
