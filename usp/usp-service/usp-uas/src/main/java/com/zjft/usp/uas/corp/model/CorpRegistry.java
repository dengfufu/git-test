package com.zjft.usp.uas.corp.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author canlei
 * @date 2019-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uas_corp_reg")
@ApiModel("企业注册表")
public class CorpRegistry extends Model<CorpRegistry> {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "corpid")
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

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("LOGO文件编号")
    private Long logoImg;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
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

    @ApiModelProperty("是否认证(0=未认证，1=已认证)")
    private Integer verify;

    @ApiModelProperty("企业编号")
    private String corpCode;
}