package com.zjft.usp.anyfix.work.ware.model;

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
 * 工单回收物品表
 * </p>
 *
 * @author zgpi
 * @since 2019-10-14
 */
@ApiModel(value="WorkWareRecycle对象", description="工单回收物品表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_ware_recycle")
public class WorkWareRecycle implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键，编号")
    @TableId("recycle_id")
    private Long recycleId;

    @ApiModelProperty(value = "工单编号")
    private Long workId;

    @ApiModelProperty(value = "物品型号名称")
    private String modelName;

    @ApiModelProperty(value = "物品品牌名称")
    private String brandName;

    @ApiModelProperty(value = "物品分类名称")
    private String catalogName;

    @ApiModelProperty(value = "物品序列号")
    private String wareSerial;

    @ApiModelProperty(value = "数量")
    private Integer quantity;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;


}
