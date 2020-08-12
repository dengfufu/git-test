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
 * 员工结算记录
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("settle_staff_record")
public class SettleStaffRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "结算记录编号")
    @TableId(value = "record_id")
    private Long recordId;

    @ApiModelProperty(value = "服务商编号")
    private Long serviceCorp;

    @ApiModelProperty(value = "记录名称")
    private String recordName;

    @ApiModelProperty(value = "结算起始日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date startDate;

    @ApiModelProperty(value = "结算终止日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date endDate;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "结算人员")
    private Long operator;

    @ApiModelProperty(value = "结算时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;


}
