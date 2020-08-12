package com.zjft.usp.anyfix.work.booking.model;

import java.math.BigDecimal;

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
 * 工单预约表
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
@ApiModel(value="WorkBooking对象", description="工单预约表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_booking")
public class WorkBooking implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "预约ID")
    @TableId("booking_id")
    private Long bookingId;

    @ApiModelProperty(value = "工单ID")
    private Long workId;

    @ApiModelProperty(value = "预约开始时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date bookTimeBegin;

    @ApiModelProperty(value = "预约结束时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date bookTimeEnd;

    @ApiModelProperty(value = "是否有效")
    private String enabled;

    @ApiModelProperty(value = "经度")
    private BigDecimal lon;

    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;


}
