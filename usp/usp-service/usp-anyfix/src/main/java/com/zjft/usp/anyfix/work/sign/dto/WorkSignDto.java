package com.zjft.usp.anyfix.work.sign.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import com.zjft.usp.anyfix.work.sign.model.WorkSign;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 工单签到dto
 *
 * @author canlei
 * @version 1.0
 * @date 2019-09-26 09:01
 **/
@ApiModel("工单签到dto")
@Getter
@Setter
public class WorkSignDto extends WorkSign {

    @ApiModelProperty("服务方式")
    private Integer serviceMode;

    @ApiModelProperty("预约开始时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date bookTimeBegin;

    @ApiModelProperty("预约结束时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date bookTimeEnd;

    @ApiModelProperty(value = "出发时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date goTime;

}
