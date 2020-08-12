package com.zjft.usp.feign.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 设备网点
 *
 * @author zgpi
 * @since 2019-09-24
 */
@ApiModel("设备网点")
@Getter
@Setter
public class DeviceBranchDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备网点编号")
    private Long branchId;

    @ApiModelProperty(value = "服务商企业编号")
    private Long customCorp;

    @ApiModelProperty(value = "上级编号")
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


}