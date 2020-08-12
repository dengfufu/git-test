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
 * 物品寄送明细信息文件表
 * </p>
 *
 * @author zgpi
 * @since 2020-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("goods_post_d_file")
@ApiModel(value="GoodsPostDetailFile对象", description="物品寄送明细信息文件表")
public class GoodsPostDetailFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件编号")
    @TableId("file_id")
    private Long fileId;

    @ApiModelProperty(value = "寄送申请单号")
    private Long postId;

    @ApiModelProperty(value = "明细单号")
    private Long detailId;


}
