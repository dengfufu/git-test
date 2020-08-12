package com.zjft.usp.anyfix.corp.branch.model;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 设备网点表
 * </p>
 *
 * @author zgpi
 * @since 2019-09-24
 */
@ApiModel(value="DeviceBranch对象", description="设备网点表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("device_branch")
public class DeviceBranch implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备网点编号")
    @TableId("branch_id")
    private Long branchId;

    @ApiModelProperty(value = "客户编号")
    private Long customId;

    @ApiModelProperty(value = "客户企业编号")
    private Long customCorp;

    @ApiModelProperty(value = "上级网点编号")
    private Long upperBranchId;

    @ApiModelProperty(value = "网点名称")
    private String branchName;

    @ApiModelProperty(value = "省份代码")
    private String province;

    @ApiModelProperty(value = "城市代码")
    private String city;

    @ApiModelProperty(value = "区县代码")
    private String district;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "网点电话")
    private String branchPhone;

    @ApiModelProperty(value = "联系人编号")
    private Long contactId;

    @ApiModelProperty(value = "联系人姓名")
    private String contactName;

    @ApiModelProperty(value = "联系人电话")
    private String contactPhone;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "是否可用")
    private String enabled;

    @ApiModelProperty(value = "经度")
    private BigDecimal lon;

    @ApiModelProperty(value = "维度")
    private BigDecimal lat;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;

}
