package com.zjft.usp.anyfix.settle.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 结算filter
 *
 * @author user
 * @version 1.0
 * @date 2019-10-11 15:41
 **/
@Getter
@Setter
public class SettleStaffFilter extends Page {

    @ApiModelProperty("服务商企业编号")
    private Long serviceCorp;

    @ApiModelProperty(value = "结算记录编号")
    private Long recordId;

    @ApiModelProperty("人员编号")
    private Long userId;

}
