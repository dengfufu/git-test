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
 * 入库明细通用销账暂存表
 * </p>
 *
 * @author canlei
 * @since 2019-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("income_d_reversed_save")
@ApiModel(value="IncomeDetailReversedSave对象", description="入库明细通用销账暂存表")
public class IncomeDetailReversedSave implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "随机主键编号")
    @TableId(value = "id")
    private Long id;

    @ApiModelProperty(value = "入库明细号ID")
    private Long detailId;

    @ApiModelProperty(value = "对应销账ID")
    private Long bookId;


}
