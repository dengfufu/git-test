package com.zjft.usp.anyfix.corp.manage.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 委托商自动化配置表
 * </p>
 *
 * @author canlei
 * @since 2020-07-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("demander_auto_config")
@ApiModel(value="DemanderAutoConfig对象", description="委托商自动化配置表")
public class DemanderAutoConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键，委托商与服务商关系表主键")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "自动对账，Y=开启，N=关闭")
    private String autoVerify;

    @ApiModelProperty(value = "结算方式，1=按单结算，2=按月结算，3=按季度结算")
    private Integer settleType;

    @ApiModelProperty(value = "结算日期")
    private Integer settleDay;

    @ApiModelProperty(value = "自动确认服务，Y=开启，N=关闭")
    private String autoConfirmService;

    @ApiModelProperty(value = "自动确认服务时长，单位：小时")
    private BigDecimal autoConfirmServiceHours;

    @ApiModelProperty(value = "自动确认服务，Y=开启，N=关闭")
    private String autoConfirmFee;

    @ApiModelProperty(value = "自动确认费用时长，单位：小时")
    private BigDecimal autoConfirmFeeHours;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    private Date operateTime;


}
