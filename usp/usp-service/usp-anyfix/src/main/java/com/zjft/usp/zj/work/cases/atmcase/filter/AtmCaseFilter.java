package com.zjft.usp.zj.work.cases.atmcase.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ATM机CASE查询条件类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-17 19:50
 **/
@ApiModel(value = "ATM机CASE查询条件类")
@Data
public class AtmCaseFilter extends Page {

    @ApiModelProperty(value = "CASE编号")
    private String workCode;

    @ApiModelProperty(value = "CASE类型")
    private String workType;

    @ApiModelProperty(value = "CASE子类编号")
    private Integer workSubType;

    @ApiModelProperty(value = "CASE状态")
    private String workStatus;

    @ApiModelProperty(value = "新平台工单状态，多个以逗号分割")
    private String workStatuses;

    @ApiModelProperty(value = "机器型号")
    private String deviceModel;

    @ApiModelProperty(value = "制造号")
    private String serial;

    @ApiModelProperty(value = "终端号")
    private String deviceCode;

    @ApiModelProperty(value = "客户编号，即银行编号")
    private String customId;

    @ApiModelProperty(value = "客户名称，即银行名称")
    private String customName;

    @ApiModelProperty(value = "设备网点")
    private String deviceBranch;

    @ApiModelProperty(value = "服务商网点编号")
    private String serviceBranch;

    @ApiModelProperty(value = "创建开始日期")
    private String createDateStart;

    @ApiModelProperty(value = "创建结束日期")
    private String createDateEnd;

    @ApiModelProperty(value = "服务工程师")
    private String engineer;

    @ApiModelProperty(value = "查询范围", notes = "1=今日工单，2=今日完工工单，3=本月完工工单")
    private Integer queryScope;

    @ApiModelProperty(value = "预计出发开始时间")
    private String planGoTimeStart;

    @ApiModelProperty(value = "预计出发结束时间")
    private String planGoTimeEnd;

    @ApiModelProperty(value = "是否个人的key列表")
    private String ifUserKey;

    @ApiModelProperty(value = "是否查看下级")
    private String isQueryLower;

    @ApiModelProperty(value = "是否CASE是否设置监控")
    private String isSetMonitor;

    @ApiModelProperty(value = "所在片区")
    private String areaCode;

    @ApiModelProperty(value = "CASE编号等快捷查询条件")
    private String quickQueryFilter;

    @ApiModelProperty(value = "报修内容")
    private String serviceRequest;


}
