package com.zjft.usp.anyfix.settle.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 结算记录filter
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-11 15:45
 **/
@Getter
@Setter
public class SettleStaffRecordFilter extends Page {

    @ApiModelProperty("服务商企业编号")
    private Long serviceCorp;

    @ApiModelProperty("记录名称")
    private String recordName;

}
