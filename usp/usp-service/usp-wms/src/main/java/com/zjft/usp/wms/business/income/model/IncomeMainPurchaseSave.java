package com.zjft.usp.wms.business.income.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 入库基本信息采购专属暂存表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("income_m_purchase_save")
@ApiModel(value = "IncomeMainPurchaseSave对象", description = "入库基本信息采购专属暂存表")
public class IncomeMainPurchaseSave implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "入库单ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "是否为二手物料(Y=是,N=否)")
    private String secondHand;

    @ApiModelProperty(value = "财务记账公司ID")
    private Long financialRecord;

    @ApiModelProperty(value = "采购合同号")
    private String contId;

    @ApiModelProperty(value = "采购明细号")
    private String purchaseDetailId;

}
