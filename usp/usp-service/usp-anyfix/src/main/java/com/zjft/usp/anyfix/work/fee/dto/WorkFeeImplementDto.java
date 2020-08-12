package com.zjft.usp.anyfix.work.fee.dto;

import com.zjft.usp.anyfix.work.fee.model.WorkFeeImplementDefine;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 实施发生费用定义Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2020-04-20 09:09
 **/
@Data
@ApiModel(value = "实施发生费用定义Dto")
public class WorkFeeImplementDto extends WorkFeeImplementDefine {

    @ApiModelProperty(value = "服务商名称")
    private String serviceCorpName;

    @ApiModelProperty(value = "委托商名称")
    private String demanderCorpName;

    @ApiModelProperty(value = "是否被工单引用")
    private boolean used;

    @ApiModelProperty(value = "费用金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "工单支出费用类别名称")
    private String implementTypeName;

}
