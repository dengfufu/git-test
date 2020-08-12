package com.zjft.usp.wms.business.income.model;

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
 * 入库明细通用销账表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("income_ware_reversed")
@ApiModel(value = "IncomeWareReversed对象", description = "入库明细通用销账表")
public class IncomeWareReversed implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "随机主键id")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "入库明细号id")
    private Long detailId;

    @ApiModelProperty(value = "对应销账ID")
    private Long bookId;


}
