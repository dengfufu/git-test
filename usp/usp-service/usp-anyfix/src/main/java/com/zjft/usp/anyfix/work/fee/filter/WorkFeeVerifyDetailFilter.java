package com.zjft.usp.anyfix.work.fee.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 对账单明细过滤器
 *
 * @author canlei
 * @version 1.0
 * @date 2020-05-14 17:39
 **/
@Data
@ApiModel(value = "对账单明细过滤器")
public class WorkFeeVerifyDetailFilter extends Page {

    @ApiModelProperty(value = "对账单编号")
    private Long verifyId;

}
