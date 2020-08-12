package com.zjft.usp.anyfix.goods.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 物品寄送基本信息工单表
 * </p>
 *
 * @author zgpi
 * @since 2020-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("goods_post_m_work")
@ApiModel(value="GoodsPostWork对象", description="物品寄送基本信息工单表")
public class GoodsPostWork implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "寄送申请ID")
    @TableId("post_id")
    private Long postId;

    @ApiModelProperty(value = "工单ID")
    @TableId("work_id")
    private Long workId;

    @ApiModelProperty(value = "工单号")
    private String workCode;


}
