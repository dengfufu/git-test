package com.zjft.usp.anyfix.settle.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zjft.usp.anyfix.settle.model.SettleCustomDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * 客户结算单明细DTO
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-10 15:58
 **/
@ApiModel(value = "客户结算单明细DTO")
@Getter
@Setter
public class SettleCustomDetailDto extends SettleCustomDetail {

    @ApiModelProperty("工单编号")
    private String workCode;

    @ApiModelProperty("工单类型编号")
    private Integer workType;

    @ApiModelProperty("工单类型名称")
    private String workTypeName;

    @ApiModelProperty("设备客户")
    private Long customCorp;

    @ApiModelProperty("设备客户名称")
    private String customCorpName;

    @ApiModelProperty("保修状态编号")
    private Integer warranty;

    @ApiModelProperty("保修状态名称")
    private String warrantyName;

    @ApiModelProperty("创建时间")
    private Timestamp createTime;

}
