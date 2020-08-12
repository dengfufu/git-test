package com.zjft.usp.anyfix.settle.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 客户结算明细filter
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-21 19:21
 **/
@ApiModel(value = "客户结算单明细过滤器")
@Getter
@Setter
public class SettleCustomDetailFilter extends Page {

    @ApiModelProperty(value = "客户结算单编号")
    private Long settleId;

}
