package com.zjft.usp.anyfix.work.follow.model;

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
 * 跟进记录表
 * </p>
 *
 * @author cxd
 * @since 2020-05-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_follow")
@ApiModel(value="WorkFollow对象", description="跟进记录表")
public class WorkFollow implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键，编号")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "企业编号")
    private Long corpId;

    @ApiModelProperty(value = "工单id")
    private Long workId;

    @ApiModelProperty(value = "关联id")
    private Long referId;

    @ApiModelProperty(value = "跟进说明")
    private String followRecord;

    @ApiModelProperty(value = "处理人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;
}
