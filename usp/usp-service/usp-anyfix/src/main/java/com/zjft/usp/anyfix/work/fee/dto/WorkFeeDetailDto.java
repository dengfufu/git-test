package com.zjft.usp.anyfix.work.fee.dto;

import com.zjft.usp.anyfix.work.fee.model.WorkFeeDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 工单费用明细Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2020-04-24 14:32
 **/
@Getter
@Setter
@ApiModel(value = "工单费用明细Dto")
public class WorkFeeDetailDto extends WorkFeeDetail {

    @ApiModelProperty(value = "费用名称")
    private String feeName;

    @ApiModelProperty(value = "工单收费规则编号")
    private Long assortId;

    @ApiModelProperty(value = "工单收费规则名称")
    private String assortName;

    @ApiModelProperty(value = "工单支出费用编号")
    private Long implementId;

    @ApiModelProperty(value = "工单支出费用名称")
    private String implementName;

    @ApiModelProperty(value = "工单支出费用类型")
    private Integer implementType;

    @ApiModelProperty(value = "工单支出费用类型名称")
    private String implementTypeName;

    @ApiModelProperty(value = "费用分类名称")
    private String feeTypeName;

    @ApiModelProperty(value = "是否用于协同人员")
    private String together;

}
