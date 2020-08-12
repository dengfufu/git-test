package com.zjft.usp.anyfix.corp.user.dto;

import com.zjft.usp.anyfix.corp.user.model.ServiceBranchUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zphu
 * @date 2019/9/26 16:10
 * @Version 1.0
 **/
@ApiModel("服务网点人员")
@Getter
@Setter
public class ServiceBranchUserDto extends ServiceBranchUser {

    @ApiModelProperty("服务人员姓名")
    private String userName;

    @ApiModelProperty(value = "推荐级别", notes = "1=强烈推荐，2=推荐，3=可派单，4=不建议派单，5=无法派单")
    private Integer recommend;

    @ApiModelProperty("推荐理由")
    private String recommendReason;

    @ApiModelProperty(value = "服务人员状态", notes = "1=预约时间内有工单未完成，2=预约时间内空闲（紧限于今天内工单），" +
            "其他时间段工单不考虑工程师已领取多少工单")
    private Integer status;

    @ApiModelProperty("服务人员距离")
    private Integer distance;

    @ApiModelProperty("已完成工单得到的评价的总分数")
    private Integer scores;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("角色")
    private String roleNames;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("人员列表")
    private List<Long> userIdList;

}
