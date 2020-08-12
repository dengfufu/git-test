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
import java.sql.Date;
import java.sql.Timestamp;

/**
 * <p>
 * 员工结算单
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("settle_staff")
public class SettleStaff implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "结算单编号")
    @TableId("settle_id")
    private Long settleId;

    @ApiModelProperty(value = "服务商编号")
    private Long recordId;

    @ApiModelProperty(value = "客户编号")
    private Long userId;

    @ApiModelProperty(value = "结算工单总数")
    private Integer workQuantity;

    @ApiModelProperty(value = "结算单状态")
    private Integer status;

    @ApiModelProperty(value = "对账结果")
    private Integer result;

    @ApiModelProperty(value = "对账人员")
    private Long checkUser;

    @ApiModelProperty(value = "对账时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date checkTime;


}
