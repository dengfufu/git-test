package com.zjft.usp.anyfix.work.transfer.model;

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
 * 工单流转表
 * </p>
 *
 * @author zphu
 * @since 2019-09-25
 */
@ApiModel(value="WorkTransfer对象", description="工单流转表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_transfer")
public class WorkTransfer implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID主键")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "工单ID")
    private Long workId;

    @ApiModelProperty(value = "流转方式")
    private Integer mode;

    @ApiModelProperty(value = "关联表单ID")
    private Long referForm;

    @ApiModelProperty(value = "服务商")
    private Long serviceCorp;

    @ApiModelProperty(value = "服务商网点")
    private Long serviceBranch;

    @ApiModelProperty(value = "服务方式")
    private Integer serviceMode;

    @ApiModelProperty(value = "原因码")
    private Integer reasonId;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "处理人")
    private Long operator;

    @ApiModelProperty(value = "处理时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;

    @ApiModelProperty(value = "经度")
    private BigDecimal lon;

    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;


}
