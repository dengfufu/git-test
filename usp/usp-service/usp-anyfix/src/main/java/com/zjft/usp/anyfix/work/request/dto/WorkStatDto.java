package com.zjft.usp.anyfix.work.request.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 工单统计
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/10/14 1:35 下午
 **/
@ApiModel("工单统计")
@Getter
@Setter
public class WorkStatDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单状态")
    private Integer workStatus;

    @ApiModelProperty(value = "工单数量")
    private Long workNumber;
}
