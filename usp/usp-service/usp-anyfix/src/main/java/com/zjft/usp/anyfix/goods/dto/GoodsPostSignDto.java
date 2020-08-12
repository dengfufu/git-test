package com.zjft.usp.anyfix.goods.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 物品寄送签收信息DTO
 *
 * @author zgpi
 * @since 2020-04-24
 */
@ApiModel("物品寄送签收信息DTO")
@Data
public class GoodsPostSignDto implements Serializable {

    private static final long serialVersionUID = 1228959133956125564L;

    @ApiModelProperty(value = "寄送单ID")
    private Long postId;

    @ApiModelProperty(value = "明细ID列表")
    private List<Long> detailIdList;

    @ApiModelProperty(value = "收货备注")
    private String receiveNote;
}
