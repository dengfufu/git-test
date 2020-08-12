package com.zjft.usp.zj.work.cases.atmcase.dto.atmcase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * CASE取消Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2020-03-26 19:42
 **/
@ApiModel(value = "CASE取消Dto")
@Data
public class CaseCancelDto implements Serializable {

    private static final long serialVersionUID = 4519976263862762397L;

    @ApiModelProperty(value = "case编号")
    private String workCode;

    @ApiModelProperty(value = "取消原因")
    private String cancelReason;

    @ApiModelProperty(value = "行方陪同人员姓名")
    private String escortName;

    @ApiModelProperty(value = "行方陪同人员联系方式")
    private String escortPhone;

    @ApiModelProperty(value = "取消时间")
    private String cancelTime;

    @ApiModelProperty(value = "修改时间")
    private String modTime;

}
