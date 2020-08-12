package com.zjft.usp.wms.business.income.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 入库单统计Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2019-12-03 15:12
 **/
@ApiModel(value = "入库单统计Dto")
@Data
public class IncomeStatDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "入库状态, 0=未提交，10=待审批，20=已入库")
    private Integer incomeStatus;

    @ApiModelProperty(value = "入库单数量")
    private Long incomeNumber;

}
