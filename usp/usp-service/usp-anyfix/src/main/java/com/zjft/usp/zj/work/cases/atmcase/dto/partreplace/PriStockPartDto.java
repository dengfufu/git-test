package com.zjft.usp.zj.work.cases.atmcase.dto.partreplace;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 个人备件DTO类
 *
 * @author zgpi
 * @date 2020-4-2 19:42
 **/
@ApiModel("个人备件DTO类")
@Data
public class PriStockPartDto {

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "名称")
    private String name;
}
