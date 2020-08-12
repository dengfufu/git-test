package com.zjft.usp.pay.business.service.feign.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author CK
 * @date 2020-06-24
 */
@Setter
@Getter
public class CorpRegistry {

    @ApiModelProperty("企业编号")
    private Long corpId;

    @ApiModelProperty("交易编号")
    private Long txId;

    @ApiModelProperty("企业名称")
    private String corpName;

    @ApiModelProperty("企业简称")
    private String shortName;

    @ApiModelProperty("省编号")
    private String province;

    @ApiModelProperty("市编号")
    private String city;

    @ApiModelProperty("区县编号")
    private String district;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("联系方式")
    private String telephone;

    @ApiModelProperty("所属行业编号")
    private String industry;

    @ApiModelProperty("经营业务")
    private String business;

    @ApiModelProperty("人员规模")
    private Integer staffQty;

    @ApiModelProperty("官方网站")
    private String website;

    @ApiModelProperty("管理密码")
    private String passwd;

    @ApiModelProperty("LOGO文件编号")
    private Long logoImg;

    @ApiModelProperty("注册用户编号")
    private Long regUserId;

    @ApiModelProperty("注册时间")
    private Date regTime;

    @ApiModelProperty("所属用户编号")
    private Long ownerUserId;

    @ApiModelProperty("支付宝账号")
    private String aliPayLogonId;

    @Deprecated
    @ApiModelProperty("应用编号")
    private Integer appId;

    @ApiModelProperty("客户端应用编号")
    private String clientId;

    @ApiModelProperty("企业编号")
    private String corpCode;
}