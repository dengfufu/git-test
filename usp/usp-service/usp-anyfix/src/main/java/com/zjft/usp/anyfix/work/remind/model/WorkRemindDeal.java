package com.zjft.usp.anyfix.work.remind.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 工单预警处理
 * </p>
 *
 * @author Qiugm
 * @since 2020-05-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_remind_deal")
@ApiModel(value = "WorkRemindDeal对象")
public class WorkRemindDeal implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编号")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "工单ID")
    private Long workId;

    @ApiModelProperty(value = "提醒类型")
    private int remindType;

    @ApiModelProperty(value = "预警时间")
    private Date remindTime;

    @ApiModelProperty(value = "是否有效")
    private String enabled;

    @ApiModelProperty(value = "修改次数")
    private int num;

    @ApiModelProperty(value = "处理说明")
    private String note;

    @ApiModelProperty(value = "建立人ID")
    private Long operator;

    @ApiModelProperty(value = "建立时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;
}
