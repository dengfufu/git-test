package com.zjft.usp.device.baseinfo.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 设备大类filter
 *
 * @author canlei
 * @version 1.0
 * @date 2019-11-08 10:08
 **/
@ApiModel(value = "设备大类filter")
@Getter
@Setter
public class DeviceLargeClassFilter extends Page {

    @ApiModelProperty(value = "大类编号")
    private Long id;

    @ApiModelProperty(value = "企业编号")
    private Long corp;

    @ApiModelProperty(value = "用于查询委托上的企业编号")
    private Long corpIdForDemander;


    @ApiModelProperty(value = "大类名称")
    private String name;

    @ApiModelProperty(value = "是否可用")
    private String enabled;

    @ApiModelProperty(value = "模糊查询")
    private String matchFilter;
}
