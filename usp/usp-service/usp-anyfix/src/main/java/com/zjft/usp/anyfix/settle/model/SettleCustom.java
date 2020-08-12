package com.zjft.usp.anyfix.settle.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 客户结算单
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("settle_custom")
public class SettleCustom implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "结算单编号")
    @TableId("settle_id")
    private Long settleId;

    @ApiModelProperty(value = "服务商编号")
    private Long serviceCorp;

    @ApiModelProperty(value = "客户编号")
    private Long customCorp;

    @ApiModelProperty(value = "结算起始日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date startDate;

    @ApiModelProperty(value = "结算截止日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date endDate;

    @ApiModelProperty(value = "结算工单总数")
    private Integer workQuantity;

    @ApiModelProperty(value = "结算单状态")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "结算人员")
    private Long operator;

    @ApiModelProperty(value = "结算时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;

    @ApiModelProperty(value = "对账人员")
    private Long checkUser;

    @ApiModelProperty(value = "对账时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date checkTime;

    @ApiModelProperty(value = "对账备注")
    private String checkNote;

}
