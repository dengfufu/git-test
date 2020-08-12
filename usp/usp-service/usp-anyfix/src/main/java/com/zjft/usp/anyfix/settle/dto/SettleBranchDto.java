package com.zjft.usp.anyfix.settle.dto;

import com.zjft.usp.anyfix.settle.model.SettleBranch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 网点结算单DTO
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-11 09:15
 **/
@ApiModel("网点结算单DTO")
@Getter
@Setter
public class SettleBranchDto extends SettleBranch {

    @ApiModelProperty("网点名称")
    private String branchName;

    @ApiModelProperty("状态名称")
    private String statusName;

    @ApiModelProperty("操作人姓名")
    private String operatorName;

}
