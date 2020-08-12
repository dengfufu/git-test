package com.zjft.usp.anyfix.work.deal.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 供应商核对Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2020-01-18 13:07
 **/
@ApiModel("委托商确认Dto")
@Data
public class DemanderCheckDto {

    @ApiModelProperty(value = "工单编号")
    private Long workId;

    @ApiModelProperty(value = "确认结果")
    private String demanderCheckResult;

    @ApiModelProperty(value = "确认状态", notes = "1=待确认，2=已通过，3=不通过")
    private Integer demanderCheckStatus;

    @ApiModelProperty(value = "确认状态名称")
    private String demanderCheckStatusName;

    @ApiModelProperty(value = "核对备注")
    private String demanderCheckNote;

}
