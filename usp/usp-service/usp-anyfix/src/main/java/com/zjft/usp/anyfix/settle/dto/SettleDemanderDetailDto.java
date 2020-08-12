package com.zjft.usp.anyfix.settle.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import com.zjft.usp.anyfix.settle.model.SettleDemanderDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 供应商结算单明细
 *
 * @author canlei
 * @version 1.0
 * @date 2020-01-13 10:18
 **/
@ApiModel("供应商结算单明细")
@Getter
@Setter
public class SettleDemanderDetailDto extends SettleDemanderDetail {

    @ApiModelProperty(value = "对账单名称")
    private String verifyName;

    @ApiModelProperty(value = "对账起始日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date startDate;

    @ApiModelProperty(value = "对账截止日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date endDate;

    @ApiModelProperty(value = "行政区划")
    private String district;

    @ApiModelProperty(value = "行政区划名称")
    private String districtName;

    @ApiModelProperty(value = "对账工单总数")
    private Integer workQuantity;

    @ApiModelProperty(value = "结算总金额")
    private BigDecimal verifyAmount;

    @ApiModelProperty(value = "对账单状态")
    private Integer status;

    @ApiModelProperty(value = "对账单状态名称")
    private String statusName;

}
