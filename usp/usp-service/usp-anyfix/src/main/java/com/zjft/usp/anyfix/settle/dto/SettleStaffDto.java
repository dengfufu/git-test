package com.zjft.usp.anyfix.settle.dto;

import com.zjft.usp.anyfix.settle.model.SettleStaff;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;


/**
 * 员工结算单Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-11 15:40
 **/
@ApiModel(value = "员工结算单Dto")
@Getter
@Setter
public class SettleStaffDto extends SettleStaff {

    @ApiModelProperty(value = "员工姓名")
    private String userName;

    @ApiModelProperty(value = "操作人编号")
    private Long operator;

    @ApiModelProperty(value = "操作人姓名")
    private String operatorName;

    @ApiModelProperty(value = "操作时间")
    private Timestamp operateTime;

    @ApiModelProperty(value = "结算起始时间")
    private Date startDate;

    @ApiModelProperty(value = "结算终止时间")
    private Date endDate;

}
