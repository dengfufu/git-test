package com.zjft.usp.anyfix.settle.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 员工结算单明细filter
 *
 * @author user
 * @version 1.0
 * @date 2019-10-25 15:01
 **/
@ApiModel(value = "员工结算单明细filter")
@Getter
@Setter
public class SettleStaffDetailFilter extends Page {

    @ApiModelProperty("员工结算单号")
    private Long settleId;

}
