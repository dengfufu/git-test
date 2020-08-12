package com.zjft.usp.anyfix.work.assign.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 派单filter
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/10/12 2:41 下午
 **/
@ApiModel("派单filter")
@Data
public class WorkAssignFilter extends Page {

    @ApiModelProperty("工单ID")
    private Long workId;

    @ApiModelProperty("工单类型")
    private Integer workType;

    @ApiModelProperty("服务请求")
    private String serviceRequest;

    @ApiModelProperty("设备客户")
    private Long customCorp;

    @ApiModelProperty("设备网点")
    private Long deviceBranch;

    @ApiModelProperty("优先级")
    private Integer priority;

    @ApiModelProperty("设备小类")
    private Long smallClass;

    @ApiModelProperty("设备品牌")
    private Long brand;

    @ApiModelProperty("设备型号")
    private Long model;

    @ApiModelProperty("是否匹配技能")
    private Boolean isMatchSkill;

    @ApiModelProperty("工程师姓名")
    private String userName;

    @ApiModelProperty("当前用户")
    private Long userId;

    @ApiModelProperty("当前企业")
    private Long corpId;

    @ApiModelProperty("服务网点")
    private Long serviceBranch;

    @ApiModelProperty(value = "是否获取所有工程师")
    private String forAll;
}
