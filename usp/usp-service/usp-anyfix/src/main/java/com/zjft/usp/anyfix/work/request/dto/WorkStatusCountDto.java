package com.zjft.usp.anyfix.work.request.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zrlin
 * @date 2019-11-05 14:00
 */
@Data
public class WorkStatusCountDto {

    @ApiModelProperty(value = "工单状态编码")
    private Integer status;

    @ApiModelProperty(value = "工单状态名称")
    private String name;

    @ApiModelProperty(value = "工单数量")
    private Integer count;
}
