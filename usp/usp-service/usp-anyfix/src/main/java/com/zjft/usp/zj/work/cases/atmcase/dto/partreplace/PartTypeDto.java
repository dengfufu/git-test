package com.zjft.usp.zj.work.cases.atmcase.dto.partreplace;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 备件类型DTO类
 *
 * @author zgpi
 * @date 2020-4-2 14:42
 **/
@ApiModel("备件类型DTO类")
@Data
public class PartTypeDto {

    @ApiModelProperty(value = "备件编码")
    private String partCode;

    @ApiModelProperty(value = "值")
    private String value;
}
