package com.zjft.usp.anyfix.work.deal.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 服务商审核Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2020-03-09 10:35
 **/
@ApiModel("服务商审核Dto")
@Data
public class ServiceCheckDto {

    @ApiModelProperty(value = "工单编号")
    private Long workId;

    @ApiModelProperty(value = "审核结果", notes = "Y=通过，N=不通过")
    private String serviceCheckResult;

}
