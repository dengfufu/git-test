package com.zjft.usp.anyfix.baseinfo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工单来源
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/10/11 9:26 上午
 **/
@ApiModel(value = "工单来源")
@Data
public class WorkSourceDto {

    @ApiModelProperty(value = "编码")
    private Integer code;

    @ApiModelProperty(value = "名称")
    private String name;
}
