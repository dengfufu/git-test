package com.zjft.usp.anyfix.baseinfo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 枚举类Dto
 * @author zgpi
 * @version 1.0
 * @date 2019/11/1 17:25
 */
@ApiModel("枚举类Dto")
@Getter
@Setter
public class EnumDto {

    @ApiModelProperty(value = "编码")
    private Integer code;

    @ApiModelProperty(value = "名称")
    private String name;
}
