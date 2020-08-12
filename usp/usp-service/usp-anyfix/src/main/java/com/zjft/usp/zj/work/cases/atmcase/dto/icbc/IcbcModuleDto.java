package com.zjft.usp.zj.work.cases.atmcase.dto.icbc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: JFZOU
 * @Date: 2020-04-06 8:53
 * @Version 1.0
 */
@Data
public class IcbcModuleDto {
    @ApiModelProperty(value = "模块编码")
    private String code;
    @ApiModelProperty(value = "模块名称")
    private String name;
}
