package com.zjft.usp.zj.work.baseinfo.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 服务网点filter类
 *
 * @author zgpi
 * @date 2020/3/24 13:53
 */
@ApiModel(value = "服务网点filter类")
@Data
public class ServiceBranchFilter extends Page {

    @ApiModelProperty(value = "网点名称")
    private String branchName;
}
