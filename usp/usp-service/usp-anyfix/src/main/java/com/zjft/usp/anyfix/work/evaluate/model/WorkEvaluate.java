package com.zjft.usp.anyfix.work.evaluate.model;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 客户评价表
 * </p>
 *
 * @author zphu
 * @since 2019-09-23
 */
@ApiModel(value="WorkEvaluate对象", description="客户评价表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_evaluate")
public class WorkEvaluate extends Model<WorkEvaluate> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单ID")
    @TableId("work_id")
    private Long workId;

    @ApiModelProperty(value = "评价内容")
    private String remark;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;

    @ApiModelProperty(value = "经度")
    private BigDecimal lon;

    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;

}
