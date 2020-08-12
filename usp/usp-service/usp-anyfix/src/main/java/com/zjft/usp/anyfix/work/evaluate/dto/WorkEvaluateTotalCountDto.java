package com.zjft.usp.anyfix.work.evaluate.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zrlin
 * @date 2019-11-06 15:13
 */
@Data
public class WorkEvaluateTotalCountDto {


    @ApiModelProperty(value = "当前评价统计量")
    private Integer count;

    @ApiModelProperty(value = "当前分数")
    private Integer score;

    @ApiModelProperty(value = "名称")
    private String name;

}
