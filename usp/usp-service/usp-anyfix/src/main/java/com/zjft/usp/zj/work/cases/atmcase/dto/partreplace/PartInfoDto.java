package com.zjft.usp.zj.work.cases.atmcase.dto.partreplace;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 备件信息DTO类
 *
 * @author zgpi
 * @date 2020-4-2 10:18
 **/
@ApiModel("备件信息DTO类")
@Data
public class PartInfoDto {

    @ApiModelProperty(value = "备件ID")
    private String code;

    @ApiModelProperty(value = "备件名称")
    private String name;
}
