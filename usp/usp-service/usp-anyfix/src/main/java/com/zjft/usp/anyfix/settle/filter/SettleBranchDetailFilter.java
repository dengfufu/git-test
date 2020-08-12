package com.zjft.usp.anyfix.settle.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 网点结算单明细filter
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-17 08:56
 **/
@Getter
@Setter
public class SettleBranchDetailFilter extends Page {

    @ApiModelProperty("结算单编号")
    private Long settleId;

}
