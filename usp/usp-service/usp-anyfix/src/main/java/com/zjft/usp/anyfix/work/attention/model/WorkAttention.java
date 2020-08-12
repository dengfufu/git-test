package com.zjft.usp.anyfix.work.attention.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
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
 * 工单关注表
 * </p>
 *
 * @author cxd
 * @since 2020-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_attention")
@ApiModel(value="WorkAttention对象", description="工单关注表")
public class WorkAttention implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键，编号")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "工单编号")
    private Long workId;

    @ApiModelProperty(value = "关注人")
    private Long userId;

    @ApiModelProperty(value = "企业编号")
    private Long corpId;

    @ApiModelProperty(value = "操作类型（Y=关注，N=取消关注）")
    private String operateType;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;
}
