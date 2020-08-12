package com.zjft.usp.anyfix.work.support.model;

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
 * 技术支持
 * </p>
 *
 * @author cxd
 * @since 2020-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_support")
@ApiModel(value="WorkSupport对象", description="技术支持")
public class WorkSupport implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键，编号")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "工单编号")
    private Long workId;

    @ApiModelProperty(value = "企业编号")
    private Long corpId;

    @ApiModelProperty(value = "严重度")
    private Integer severity;

    @ApiModelProperty(value = "处理人")
    private String handler;

    @ApiModelProperty(value = "技术说明")
    private String description;

    @ApiModelProperty(value = "支持类型 Y=关闭支持，N=未关闭支持")
    private String supportType;

    @ApiModelProperty(value = "关闭时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date closeTime;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;
}
