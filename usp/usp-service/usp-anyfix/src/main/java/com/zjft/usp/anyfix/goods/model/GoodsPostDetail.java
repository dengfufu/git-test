package com.zjft.usp.anyfix.goods.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 物品寄送明细信息表
 * </p>
 *
 * @author zgpi
 * @since 2020-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("goods_post_d")
@ApiModel(value="GoodsPostDetail对象", description="物品寄送明细信息表")
public class GoodsPostDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "明细ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "物品寄送申请单号")
    private Long postId;

    @ApiModelProperty(value = "物品名称")
    private String goodsName;

    @ApiModelProperty(value = "数量")
    private Integer quantity;

    @ApiModelProperty(value = "序列号")
    private String sn;

    @ApiModelProperty(value = "分箱号")
    private Integer subBoxNum;

    @ApiModelProperty(value = "是否签收， Y=是 N=否")
    private String signed;

    @ApiModelProperty(value = "签收人")
    private Long signer;

    @ApiModelProperty(value = "签收时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date signTime;


}
