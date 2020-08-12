package com.zjft.usp.wms.business.outcome.model;

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
 * 出库明细信息共用暂存表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("outcome_d_common_save")
@ApiModel(value = "OutcomeDetailCommonSave对象", description = "出库明细信息共用暂存表")
public class OutcomeDetailCommonSave implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "出库明细ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "出库单号ID")
    private Long outcomeId;

    @ApiModelProperty(value = "库存明细ID")
    private Long stockId;

    @ApiModelProperty(value = "数量")
    private Integer quantity;

}
