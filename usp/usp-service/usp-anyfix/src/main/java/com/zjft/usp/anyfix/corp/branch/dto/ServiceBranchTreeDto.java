package com.zjft.usp.anyfix.corp.branch.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 服务网点树
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/12/9 14:47
 */
@ApiModel("服务网点树")
@Getter
@Setter
public class ServiceBranchTreeDto {

    @ApiModelProperty(value = "值", notes = "网点编号")
    private Long key;

    @ApiModelProperty(value = "标题", notes = "网点名称")
    private String title;

    @ApiModelProperty(value = "父节点")
    private Long parentId;

    @ApiModelProperty(value = "是否叶子节点")
    private Boolean isLeaf;

    @ApiModelProperty(value = "子节点")
    private List<ServiceBranchTreeDto> children;
}
