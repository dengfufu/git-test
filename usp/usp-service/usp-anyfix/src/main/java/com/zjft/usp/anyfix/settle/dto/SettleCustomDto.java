package com.zjft.usp.anyfix.settle.dto;

import com.zjft.usp.anyfix.settle.model.SettleCustom;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 客户结算单DTO
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-10 15:10
 **/
@ApiModel(value = "客户结算单DTO")
@Getter
@Setter
public class SettleCustomDto extends SettleCustom {

    @ApiModelProperty(value = "客户名称")
    private String customName;

    @ApiModelProperty(value = "状态名称")
    private String statusName;

    @ApiModelProperty(value = "结算人姓名")
    private String operatorName;

}
