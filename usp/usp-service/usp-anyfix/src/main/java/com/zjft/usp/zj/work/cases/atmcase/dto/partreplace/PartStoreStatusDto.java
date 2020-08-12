package com.zjft.usp.zj.work.cases.atmcase.dto.partreplace;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 备件库存状态DTO类
 *
 * @author zgpi
 * @date 2020-4-2 14:22
 **/
@ApiModel("备件库存状态DTO类")
@Data
public class PartStoreStatusDto {

    @ApiModelProperty(value = "状态编码")
    private Integer code;

    @ApiModelProperty(value = "状态名称")
    private String name;
}
