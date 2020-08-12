package com.zjft.usp.wms.business.consign.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.wms.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 发货基本信息共用表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("consign_m")
@ApiModel(value = "ConsignMain对象", description = "发货基本信息共用表")
public class ConsignMain implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "发货单号ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "可显示的发货单号")
    private String consignCode;

    @ApiModelProperty(value = "企业ID")
    private Long corpId;

    @ApiModelProperty(value = "发货日期")
    private String consignDate;

    @ApiModelProperty(value = "发货人ID")
    private Long consignBy;

    @ApiModelProperty(value = "发货人联系手机号")
    private String consignMobile;

    @ApiModelProperty(value = "发货描述")
    private String description;

    @ApiModelProperty(value = "快递公司ID")
    private Long expressCorpId;

    @ApiModelProperty(value = "快递单号ID")
    private String expressNo;

    @ApiModelProperty(value = "快递类型")
    private Integer expressType;

    @ApiModelProperty(value = "公司收货人ID")
    private Long receiverId;

    @ApiModelProperty(value = "收货人显示名称(出库时，收货方可能不是本公司人员，无法使用ID，此时可使用名称)")
    private String receiveName;

    @ApiModelProperty(value = "收货人联系手机号")
    private String receiverMobile;

    @ApiModelProperty(value = "收货地址")
    private String receiveDistrict;

    @ApiModelProperty(value = "收货详细地址")
    private String receiveAddress;

    @ApiModelProperty(value = "总箱数")
    private Integer totalBoxNum;

    @ApiModelProperty(value = "运输方式自定义列表")
    private Long transportTypeId;

    @ApiModelProperty(value = "托运方式自定义列表")
    private Long consignTypeId;

    @ApiModelProperty(value = "是否提交(Y=提交;N=保存)")
    private String isSubmit;

    @ApiModelProperty(value = "创建人")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date createTime;


}
