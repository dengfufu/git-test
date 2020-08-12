package com.zjft.usp.device.device.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 客户企业与设备网点关联
 */
@Getter
@Setter
@NoArgsConstructor
public class CustomDeviceRelate implements Serializable {

    @ApiModelProperty(value = "客户企业名称")
    private String customCorpName;
    @ApiModelProperty(value = "客户编号")
    private Long customId;
    @ApiModelProperty(value = "客户企业编号")
    private Long customCorp;
    @ApiModelProperty(value = "设备网点编号")
    private Long branchId;
    @ApiModelProperty(value = "设备网点名称")
    private String branchName;

}
