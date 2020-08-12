package com.zjft.usp.anyfix.work.transfer.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import com.zjft.usp.anyfix.work.transfer.model.WorkTransfer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 工单流转dto
 * @author zphu
 * @date 2019/9/29 15:59
 * @Version 1.0
 **/
@ApiModel(value="工单流转dto")
@Getter
@Setter
public class WorkTransferDto extends WorkTransfer {

    @ApiModelProperty("服务工程师")
    private Long engineer;

    @ApiModelProperty("工单状态")
    private Integer workStatus;

    @ApiModelProperty(value = "出发时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date goTime;


}
