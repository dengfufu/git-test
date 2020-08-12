package com.zjft.usp.anyfix.work.fee.model;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 委托商对账单明细表
 * </p>
 *
 * @author canlei
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_fee_verify_detail")
@ApiModel(value="WorkFeeVerifyDetail对象", description="委托商对账单明细表")
public class WorkFeeVerifyDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "对账单明细编号")
    @TableId(value = "detail_id")
    private Long detailId;

    @ApiModelProperty(value = "对账单系统编号")
    private Long verifyId;

    @ApiModelProperty(value = "工单编号")
    private Long workId;

    @ApiModelProperty(value = "对账后费用")
    private BigDecimal verifyAmount;

    @ApiModelProperty(value = "备注")
    private String note;


}
