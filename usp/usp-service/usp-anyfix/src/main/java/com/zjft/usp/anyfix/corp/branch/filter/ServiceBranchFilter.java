package com.zjft.usp.anyfix.corp.branch.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 服务网点filter
 *
 * @author user
 * @version 1.0
 * @date 2019-10-12 13:40
 **/
@Getter
@Setter
public class ServiceBranchFilter extends Page {

    @ApiModelProperty(value = "服务商企业编号")
    private Long serviceCorp;

    @ApiModelProperty(value = "网点编号")
    private Long branchId;

    @ApiModelProperty(value = "网点名称")
    private String branchName;

    @ApiModelProperty(value = "直接上级服务网点编号")
    private Long upperBranchId;

    @ApiModelProperty(value = "省编号")
    private String province;

    @ApiModelProperty(value = "市编号")
    private String city;

    @ApiModelProperty(value = "区县编号")
    private String district;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "网点类型")
    private Integer type;

    @ApiModelProperty(value = "是否可用")
    private String enabled;

    @ApiModelProperty(value = "区域编码")
    private String areaCode;

    @ApiModelProperty(value = "省名称")
    private String provinceName;

    @ApiModelProperty(value = "市名称")
    private String cityName;

    @ApiModelProperty(value = "县名称")
    private String districtName;

    @ApiModelProperty(value = "是否只查询第一级网点")
    private String ifFirstLevel;

    @ApiModelProperty(value = "移动端搜索栏")
    private String mobileFilter;

    @ApiModelProperty(value = "模糊查询")
    private String matchFilter;
}
