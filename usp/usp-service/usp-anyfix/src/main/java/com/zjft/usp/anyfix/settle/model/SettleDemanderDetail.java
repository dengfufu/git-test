package com.zjft.usp.anyfix.settle.model;

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
 * 供应商结算单明细
 * </p>
 *
 * @author canlei
 * @since 2020-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("settle_demander_detail")
@ApiModel(value="SettleDemanderDetail对象", description="供应商结算单明细")
public class SettleDemanderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "明细编号")
    @TableId("detail_id")
    private Long detailId;

    @ApiModelProperty(value = "结算单编号")
    private Long settleId;

    @ApiModelProperty(value = "对账单编号")
    private Long verifyId;

    @ApiModelProperty(value = "工单编号")
    private Long workId;

}
