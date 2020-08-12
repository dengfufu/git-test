package com.zjft.usp.device.baseinfo.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 设备品牌filter
 *
 * @author canlei
 * @version 1.0
 * @date 2019-11-08 09:24
 **/
@ApiModel(value = "设备品牌filter")
@Getter
@Setter
public class DeviceBrandFilter extends Page {

    @ApiModelProperty(value = "设备品牌id")
    private Long smallClassId;

    @ApiModelProperty(value = "用该企业查询设备编号")
    private Long corpIdForDemander;

    @ApiModelProperty(value = "编号")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "供应商")
    private Long corp;

    @ApiModelProperty(value = "是否可用")
    private String enabled;

    @ApiModelProperty(value = "模糊查询")
    private String matchFilter;

}
