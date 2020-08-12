package com.zjft.usp.zj.work.baseinfo.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设备网点filter类
 *
 * @author zgpi
 * @date 2020/3/24 13:53
 */
@ApiModel(value = "设备网点filter类")
@Data
public class DeviceBranchFilter extends Page {

    @ApiModelProperty(value = "服务网点")
    private String serviceBranch;

    @ApiModelProperty(value = "银行编号")
    private String bankCode;

    @ApiModelProperty(value = "网点名称")
    private String branchName;
}
