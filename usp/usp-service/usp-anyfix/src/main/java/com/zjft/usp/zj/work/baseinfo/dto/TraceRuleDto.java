package com.zjft.usp.zj.work.baseinfo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 跟单规则Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2020-03-26 15:49
 **/
@ApiModel(value = "跟单规则Dto")
@Data
public class TraceRuleDto implements Serializable {

    private static final long serialVersionUID = 368645083782013138L;

    @ApiModelProperty(value = "工单类型")
    private String workTypeName;

    @ApiModelProperty(value = "要求跟单")
    private Integer traceRequired;

    @ApiModelProperty(value = "规则内容")
    private String ruleContent;

    @ApiModelProperty(value = "规则编号")
    private Long ruleId;

    @ApiModelProperty(value = "规则备注")
    private String ruleNote;

}
