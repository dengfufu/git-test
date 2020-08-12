package com.zjft.usp.anyfix.work.remind.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工单预警类型Dto
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-04-23 11:06
 **/
@Data
public class WorkRemindTypeDto {
    @ApiModelProperty(value = "编号")
    private int id;

    @ApiModelProperty(value = "名称")
    private String name;
}
