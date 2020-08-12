package com.zjft.usp.anyfix.work.review.model;

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
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 客户回访表
 * </p>
 *
 * @author ljzhu
 */
@ApiModel(value="WorkReview对象", description="工单客户回访表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_review")
public class WorkReview implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "工单编号")
    private Long workId;

    @ApiModelProperty(value = "是否解决，1:是 2:否 3:客户未联系上")
    private Integer isSolved;

    @ApiModelProperty(value = "沟通记录")
    private String record;


    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operatTime;

    @ApiModelProperty(value = "操作人公司")
    private Long corpId;



}
