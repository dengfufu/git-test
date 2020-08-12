package com.zjft.usp.zj.work.baseinfo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 机器型号DTO类
 *
 * @author zgpi
 * @date 2020/3/24 20:23
 */
@ApiModel(value = "机器型号DTO类")
@Data
public class DeviceModelDto implements Serializable {

    private static final long serialVersionUID = -2403186952431365014L;

    @ApiModelProperty(value = "型号ID")
    private String modelId;

    @ApiModelProperty(value = "型号名称")
    private String modelName;
}
