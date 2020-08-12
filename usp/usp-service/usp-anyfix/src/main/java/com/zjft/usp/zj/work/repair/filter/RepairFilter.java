package com.zjft.usp.zj.work.repair.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 报修filter
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-18 16:02
 **/
@ApiModel(value = "报修filter")
@Data
public class RepairFilter extends Page {
    @ApiModelProperty(value = "报修单号")
    private String repairId;

    @ApiModelProperty(value = "银行工单号")
    private String appNo;

    @ApiModelProperty(value = "工行对接交易号")
    private String tranId;

    @ApiModelProperty(value = "行方标志")
    private String serviceId;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "银行编号")
    private String bankCode;

    @ApiModelProperty(value = "工行模式：1=工行模式")
    private String bankMode;

    @ApiModelProperty(value = "新平台工单状态，多个以逗号分割")
    private String workStatuses;

    @ApiModelProperty(value = "报修单号/银行工单号等快捷查询条件")
    private String quickQueryFilter;

    @ApiModelProperty(value = "工程师")
    private String engineer;

    @ApiModelProperty(value = "服务主管")
    private String manager;

    @ApiModelProperty(value = "是否排除关联CASE的报修单")
    private String exceptRelateCase;

    @ApiModelProperty(value = "报修时间开始")
    private String reportTimeFrom;

    @ApiModelProperty(value = "报修时间结束")
    private String reportTimeTo;

    @ApiModelProperty(value = "处理状态")
    private String[] status;

    @ApiModelProperty(value = "已分派工单受理情况")
    private String dispatchWorkStatus;

    @ApiModelProperty(value = "终端号")
    private String terminalNO;

    @ApiModelProperty(value = "机器制造号")
    private String deviceCode;

    @ApiModelProperty(value = "工单类型")
    private String appType;

    @ApiModelProperty(value = "是否行外补单")
    private String facadded;

    @ApiModelProperty(value = "是否催单")
    private String supervised;

    @ApiModelProperty(value = "查询范围", notes = "1=今日工单，2=今日完工工单，3=本月完工工单")
    private Integer queryScope;

    @ApiModelProperty(value = "是否个人的key列表")
    private String ifUserKey;

    @ApiModelProperty(value = "业务Id")
    private String businessId;
}
