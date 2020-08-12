package com.zjft.usp.anyfix.work.fee.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 工单服务项目结算规则
 * </p>
 *
 * @author canlei
 * @since 2020-01-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("service_item_fee_rule")
@ApiModel(value="ServiceItemFeeRule对象", description="工单服务项目结算规则")
public class ServiceItemFeeRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "规则编号")
    @TableId("rule_id")
    private Long ruleId;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @ApiModelProperty(value = "服务商企业编号")
    private Long serviceCorp;

    @ApiModelProperty(value = "服务项目编号")
    private Integer serviceItemId;

    @ApiModelProperty(value = "适用起始日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date startDate;

    @ApiModelProperty(value = "适用结束日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date endDate;

    @ApiModelProperty(value = "每单费用")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "条件配置")
    private Long conditionId;

}
