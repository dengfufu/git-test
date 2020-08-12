package com.zjft.usp.anyfix.work.fee.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 工单基础服务费结算规则Filter
 *
 * @author canlei
 * @version 1.0
 * @date 2020-01-07 20:29
 **/
@ApiModel("工单基础服务费结算规则Filter")
@Getter
@Setter
public class WorkBasicFeeRuleFilter extends Page {

    @ApiModelProperty(value = "服务商企业编号")
    private Long serviceCorp;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

}
