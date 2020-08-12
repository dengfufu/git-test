package com.zjft.usp.anyfix.settle.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 供应商结算单明细filter
 *
 * @author canlei
 * @version 1.0
 * @date 2020-01-15 15:00
 **/
@ApiModel("供应商结算单明细filter")
@Data
public class SettleDemanderDetailFilter extends Page {

    @ApiModelProperty("结算单编号")
    private Long settleId;

}
