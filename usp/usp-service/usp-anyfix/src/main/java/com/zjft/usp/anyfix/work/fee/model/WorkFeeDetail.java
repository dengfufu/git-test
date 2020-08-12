package com.zjft.usp.anyfix.work.fee.model;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 工单费用明细
 * </p>
 *
 * @author canlei
 * @since 2020-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_fee_detail")
@ApiModel(value="WorkFeeDetail对象", description="工单费用明细")
public class WorkFeeDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "费用明细编号")
    @TableId(value = "detail_id")
    private Long detailId;

    @ApiModelProperty(value = "工单编号")
    private Long workId;

    @ApiModelProperty(value = "服务商企业编号")
    private Integer feeType;

    @ApiModelProperty(value = "费用定义编号", notes = "分类费用时为分类费用规则编号，实施发生费用时为实施发生费用定义编号")
    private Long feeId;

    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;


}
