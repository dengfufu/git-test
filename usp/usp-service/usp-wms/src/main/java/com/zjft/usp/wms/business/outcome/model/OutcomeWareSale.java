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
 * 出库信息销售出库专用表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("outcome_ware_sale")
@ApiModel(value = "OutcomeWareSale对象", description = "出库信息销售出库专用表")
public class OutcomeWareSale implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "出库明细ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "协助经办人")
    private Long assistUserId;


}
