package com.zjft.usp.zj.work.baseinfo.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 客户filter类
 *
 * @author zgpi
 * @date 2020/3/24 13:53
 */
@ApiModel(value = "客户filter类")
@Data
public class BankFilter extends Page {

    @ApiModelProperty(value = "总行编号")
    private String headBankCode;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "服务网点")
    private String serviceBranch;

    @ApiModelProperty(value = "银行级别", notes = "1=总行 2=分行")
    private Integer bankLevel;
}
