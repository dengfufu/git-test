package com.zjft.usp.anyfix.work.request.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 区域下单统计量查询
 *
 * @author zrlin
 * @version 1.0
 * @date 2019/11/01 17:06
 **/
@ApiModel("工单待接单工程师")
@Getter
@Setter
public class WorkForAssignDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单ID")
    private Long workId;

    @ApiModelProperty(value = "服务工程师ID拼接，以英文逗号分隔")
    private String engineers;

}
