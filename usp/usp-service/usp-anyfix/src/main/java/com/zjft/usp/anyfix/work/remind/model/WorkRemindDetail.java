package com.zjft.usp.anyfix.work.remind.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: JFZOU
 * @Date: 2020-04-17 16:56
 * @Version 1.0
 */
@Data
@TableName(value = "work_remind_d")
public class WorkRemindDetail {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "预警ID，主键")
    @TableId("detail_id")
    private Long detailId;

    @ApiModelProperty(value = "预警ID，外键")
    private Long remindId;

    @ApiModelProperty(value = "预警类型ID")
    private Integer remindType;

    @ApiModelProperty(value = "超时时间")
    private Integer expireTimeMin;

    @ApiModelProperty(value = "提醒最大次数")
    private Integer remindMaxCount;

    @ApiModelProperty(value = "模板ID")
    private Long templateId;

    @ApiModelProperty(value = "是否通过APP提醒")
    private String remindByApp;

    @ApiModelProperty(value = "是否通过SMS提醒")
    private String remindBySms;

    @ApiModelProperty(value = "模是否通过EMAIL提醒")
    private String remindByEmail;
}
