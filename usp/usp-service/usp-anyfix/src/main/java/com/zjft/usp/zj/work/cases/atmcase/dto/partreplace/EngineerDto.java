package com.zjft.usp.zj.work.cases.atmcase.dto.partreplace;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工程师DTO类
 *
 * @author zgpi
 * @date 2020-4-2 10:18
 **/
@ApiModel("工程师DTO类")
@Data
public class EngineerDto {

    @ApiModelProperty(value = "工程师ID")
    private String code;

    @ApiModelProperty(value = "工程师姓名")
    private String name;
}
