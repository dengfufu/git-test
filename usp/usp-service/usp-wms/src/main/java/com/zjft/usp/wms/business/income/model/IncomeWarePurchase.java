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
 * 入库明细采购专属表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("income_ware_purchase")
@ApiModel(value = "IncomeWarePurchase对象", description = "入库明细采购专属表")
public class IncomeWarePurchase implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "入库明细ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "财务记账公司ID")
    private Long financialRecord;

    @ApiModelProperty(value = "采购合同号")
    private String contId;

    @ApiModelProperty(value = "采购明细号")
    private String purchaseDetailId;

}
