package com.zjft.usp.uas.right.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 枚举类Dto
 * @author zgpi
 * @version 1.0
 * @date 2020/3/11 16:15
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
