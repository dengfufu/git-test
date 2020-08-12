package com.zjft.usp.zj.work.baseinfo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 保修状态DTO类
 *
 * @author zgpi
 * @date 2020/3/27 20:49
 */
@ApiModel(value = "保修状态DTO类")
@Data
public class WarrantyStatusDto implements Serializable {

    private static final long serialVersionUID = 2209779583542174031L;

    @ApiModelProperty(value = "状态ID")
    private String id;

    @ApiModelProperty(value = "状态名称")
    private String name;
}
