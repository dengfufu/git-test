package com.zjft.usp.anyfix.settle.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 供应商结算单filter
 *
 * @author canlei
 * @version 1.0
 * @date 2020-01-13 10:15
 **/
@ApiModel("供应商结算单filter")
@Data
public class SettleDemanderFilter extends Page {

    @ApiModelProperty("结算单号")
    private String settleCode;

    @ApiModelProperty("供应商编号")
    private Long demanderCorp;

    @ApiModelProperty(value = "委托商编号列表")
    private List<Long> demanderCorpList;

    @ApiModelProperty("服务商编号")
    private Long serviceCorp;

    @ApiModelProperty("当前企业编号")
    private Long currentCorp;

    @ApiModelProperty(value = "收款账户信息")
    private String accountFilter;

    @ApiModelProperty(value = "开票状态列表")
    private List<Integer> invoiceStatusList;

    @ApiModelProperty(value = "付款状态列表")
    private List<Integer> payStatusList;

    @ApiModelProperty(value = "结算方式")
    private Integer settleWay;

}
