package com.zjft.usp.anyfix.settle.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 客户结算单明细
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("settle_custom_detail")
public class SettleCustomDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("work_id")
    @ApiModelProperty(value = "工单编号")
    private Long workId;

    @TableId("settle_id")
    @ApiModelProperty(value = "结算单编号")
    private Long settleId;

}
