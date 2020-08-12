package com.zjft.usp.zj.work.cases.atmcase.filter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 个人备件FILTER类
 *
 * @author zgpi
 * @date 2020-4-2 19:45
 **/
@ApiModel("个人备件FILTER类")
@Data
public class PriStockPartFilter {

    @ApiModelProperty(value = "备件编码")
    private String partCode;

    @ApiModelProperty(value = "服务站")
    private String serviceBranch;

    @ApiModelProperty(value = "工程师编号")
    private String engineerId;

    @ApiModelProperty(value = "库存状态")
    private Integer storeStatus;

    @ApiModelProperty(value = "车牌号")
    private String carNo;
}
