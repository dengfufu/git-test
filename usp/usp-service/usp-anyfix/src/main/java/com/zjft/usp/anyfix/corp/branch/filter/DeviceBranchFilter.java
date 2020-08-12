package com.zjft.usp.anyfix.corp.branch.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DeviceBranch 分页查询过滤器
 *
 * @author CK
 * @date 2019-09-30 09:27
 */
@ApiModel("设备网点")
@Setter
@Getter
public class DeviceBranchFilter extends Page {

    @ApiModelProperty(value = "客户企业")
    private Long customCorp;

    @ApiModelProperty(value = "客户编号")
    private Long customId;

    @ApiModelProperty(value = "是否可用", notes = "Y=是，N=否")
    private String enabled;

    @ApiModelProperty(value = "上级网点编号")
    private Long upperBranchId;

    @ApiModelProperty(value = "网点名称")
    private String branchName;

    @ApiModelProperty(value = "企业编号",notes = "服务商/供应商/客户")
    private Long corpId;

    @ApiModelProperty(value = "模糊查询")
    private String matchFilter;

    @ApiModelProperty(value = "设备网点编号")
    private Long branchId;

    @ApiModelProperty(value = "地区编号")
    private String district;

    @ApiModelProperty(value = "客户编号列表", notes = "客户关系编号或客户企业编号")
    private List<Long> customOrCorpIdList;

    @ApiModelProperty(value = "是否查询服务商商的设备网点")
    private boolean serviceProvider;

    @ApiModelProperty(value = "委托商编号")
    private Long demanderCorp;
}
