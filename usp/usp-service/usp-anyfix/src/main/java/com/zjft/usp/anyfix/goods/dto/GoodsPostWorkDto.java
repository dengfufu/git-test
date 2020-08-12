package com.zjft.usp.anyfix.goods.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 物品寄送工单信息DTO
 *
 * @author zgpi
 * @date 2020/4/26 15:14
 */
@ApiModel(value = "物品寄送工单信息DTO")
@Data
public class GoodsPostWorkDto implements Serializable {

    private static final long serialVersionUID = 887860588929777021L;

    @ApiModelProperty(value = "工单ID")
    private Long workId;

    @ApiModelProperty(value = "工单号")
    private String workCode;
}
