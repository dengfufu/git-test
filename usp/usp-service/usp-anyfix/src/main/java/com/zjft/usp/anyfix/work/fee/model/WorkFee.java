package com.zjft.usp.anyfix.work.fee.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 工单费用表
 * </p>
 *
 * @author canlei
 * @since 2020-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_fee")
@ApiModel(value="WorkFee对象", description="工单费用表")
public class WorkFee implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单ID")
    @TableId("work_id")
    private Long workId;

//    @ApiModelProperty(value = "差旅费")
//    private BigDecimal travelFee;

//    @ApiModelProperty(value = "服务项目费用")
//    private BigDecimal serviceItemFee;

    @ApiModelProperty(value = "费用报价")
    private BigDecimal basicServiceFee;

    @ApiModelProperty(value = "备件费用")
    private BigDecimal wareUseFee;

    @ApiModelProperty(value = "分类费用")
    private BigDecimal assortFee;

    @ApiModelProperty(value = "实施发生费用")
    private BigDecimal implementFee;

//    @ApiModelProperty(value = "换下备件邮寄费")
//    private BigDecimal warePostFee;

    @ApiModelProperty(value = "其他费用")
    private BigDecimal otherFee;

    @ApiModelProperty(value = "其他费用说明")
    private String otherFeeNote;

    @ApiModelProperty(value = "总费用")
    private BigDecimal totalFee;

}
