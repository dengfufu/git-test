package com.zjft.usp.zj.work.cases.atmcase.dto.partreplace;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 更换类型DTO类
 *
 * @author zgpi
 * @date 2020-4-1 20:56
 **/
@ApiModel("更换类型DTO类")
@Data
public class ReplaceTypeDto {

    @ApiModelProperty(value = "类型编码")
    private Integer code;

    @ApiModelProperty(value = "类型名称")
    private String name;
}
