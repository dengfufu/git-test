package com.zjft.usp.device.baseinfo.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 设备规格filter
 *
 * @author zgpi
 * @version 1.0
 * @date 2020-1-20
 **/
@ApiModel(value = "设备规格filter")
@Getter
@Setter
public class DeviceSpecificationFilter extends Page {

    @ApiModelProperty(value = "委托商企业编号")
    private Long corp;

    @ApiModelProperty(value = "设备小类ID")
    private Long smallClassId;

    @ApiModelProperty(value = "是否可用")
    private String enabled;

    @ApiModelProperty(value = "模糊查询")
    private String matchFilter;
}
