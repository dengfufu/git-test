package com.zjft.usp.anyfix.work.request.filter;

import com.zjft.usp.common.dto.RightScopeDto;
import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 工单filter
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/10/10 7:02 下午
 **/

@ApiModel(value = "工单filter")
@Data
public class WorkFilter extends Page {

    @ApiModelProperty(value = "工单编号")
    private Long workId;

    @ApiModelProperty(value = "工单号")
    private String workCode;

    @ApiModelProperty(value = "委托单号")
    private String checkWorkCode;

    @ApiModelProperty(value = "服务商")
    private Long serviceCorp;

    @ApiModelProperty(value = "委托商")
    private Long demanderCorp;

    @ApiModelProperty(value = "客户")
    private Long customCorp;

    @ApiModelProperty(value = "客户ID")
    private Long customId;

    @ApiModelProperty(value = "出厂序列号")
    private String serial;

    @ApiModelProperty(value = "设备小类")
    private Long smallClassId;

    @ApiModelProperty(value = "设备型号")
    private Long deviceModel;

    @ApiModelProperty(value = "设备品牌")
    private Long deviceBrand;

    @ApiModelProperty(value = "设备网点")
    private Long deviceBranch;

    @ApiModelProperty(value = "设备编号")
    private Long deviceId;

    @ApiModelProperty(value = "工单类型")
    private String workTypes;

    @ApiModelProperty(value = "工单状态")
    private Integer workStatus;

    @ApiModelProperty(value = "工单状态")
    private String workStatuses;

    @ApiModelProperty(value = "工单状态列表")
    private List<Integer> workStatusList;

    @ApiModelProperty(value = "工单类型列表")
    private List<Integer> workTypeList;

    @ApiModelProperty(value = "工单类型列表")
    private List<Integer> workTypeNoInList;

    @ApiModelProperty(value = "服务方式")
    private Integer serviceMode;

    @ApiModelProperty(value = "服务方式")
    private String serviceModes;

    @ApiModelProperty(value = "服务人员")
    private String staffs;

    @ApiModelProperty(value = "服务网点")
    private String serviceBranches;

    @ApiModelProperty(value = "请求来源")
    private String sources;

    @ApiModelProperty(value = "开始日期")
    private Date startDate;

    @ApiModelProperty(value = "结束日期")
    private Date endDate;

    @ApiModelProperty(value = "维保方式，10=整机保 20=单次保")
    private Integer warrantyMode;

    @ApiModelProperty(value = "维保方式，多个逗号隔开")
    private String warrantyModes;

    @ApiModelProperty(value = "查询范围", notes = "1=本月工单，2=当天工单")
    private Integer queryScope;

    @ApiModelProperty(value = "移动端搜索")
    private String mobileFilter;

    @ApiModelProperty(value = "统计时间")
    private Integer countTime;

    @ApiModelProperty(value = "行政区划")
    private String district;

    @ApiModelProperty(value = "当前登录用户")
    private Long userId;

    @ApiModelProperty(value = "当前企业编号")
    private Long corpId;

    @ApiModelProperty(value = "权限类型")
    private Integer rightType;

    @ApiModelProperty(value = "权限类型列表", notes = "用于查询工单区分权限")
    private List<Integer> rightTypeList;

    @ApiModelProperty(value = "是否今日完工")
    private String ifTodayClose;

    @ApiModelProperty(value = "是否本月工单")
    private String ifMonthClose;

    @ApiModelProperty(value = "供应商核对状态")
    private Integer demanderCheckStatus;

    @ApiModelProperty(value = "服务商审核状态")
    private Integer serviceCheckStatus;

    @ApiModelProperty(value = "服务商费用审核状态")
    private Integer feeCheckStatus;

    @ApiModelProperty(value = "委托商结算状态", notes = "1=待对账，2=待结算，3=结算中，4=已结算")
    private Integer settleDemanderStatus;

    @ApiModelProperty(value = "委托商结算状态", notes = "用逗号隔开")
    private String settleDemanderStatuses;

    @ApiModelProperty(value = "企业人员编号列表")
    private List<Long> corpUserIdList;

    @ApiModelProperty(value = "服务工程师ID拼接，以英文逗号分隔")
    private Long engineers;

    @ApiModelProperty(value = "服务工程师")
    private Long engineer;

    @ApiModelProperty(value = "工单单号列表")
    private List<Long> listWorkIds;

    @ApiModelProperty(value = "提单日期开始")
    private Date dispatchTimeStart;

    @ApiModelProperty(value = "提单日期结束")
    private Date dispatchTimeEnd;

    @ApiModelProperty(value = "完成日期的具体一天")
    private Date finishTimeDay;

    @ApiModelProperty(value = "完成日期的具体一天")
    private String finishDayString;

    @ApiModelProperty(value = "完成日期的具体月份")
    private Date finishTimeMonth;

    @ApiModelProperty(value = "客户名称")
    private String customCorpName;

    @ApiModelProperty(value = "设备品牌")
    private String deviceBrandName;

    @ApiModelProperty(value = "设备型号")
    private String deviceModelName;

    @ApiModelProperty(value = "出厂序列号")
    private String machineCode;

    @ApiModelProperty(value = "工单类型名称")
    private String workTypeName;

    @ApiModelProperty(value = "联系人")
    private String contactName;

    @ApiModelProperty(value = "联系方式")
    private String contactPhone;

    @ApiModelProperty(value = "CASE编号")
    private String caseId;

    @ApiModelProperty(value = "完单日期开始时间")
    private Date finishStartDate;

    @ApiModelProperty(value = "完单日期结束时间")
    private Date finishEndDate;

    @ApiModelProperty(value = "省份")
    private String provinceCode;

    @ApiModelProperty(value = "行政区划地址字符串")
    private String addressStr;

    @ApiModelProperty(value = "省份名称")
    private String provinceName;

    @ApiModelProperty(value = "查询次数")
    private int queryCount;

    @ApiModelProperty(value = "预计出发日期开始时间")
    private Date ycStartDate;

    @ApiModelProperty(value = "预计出发日期结束时间")
    private Date ycEndDate;

    @ApiModelProperty(value = "服务网点编号列表")
    private List<Long> serviceBranchIdRightList;

    @ApiModelProperty(value = "是否获取待接单人")
    private String forAssignees;

    @ApiModelProperty(value = "委托商对账单号")
    private Long verifyId;

    @ApiModelProperty(value = "委托商结算单号")
    private Long settleId;

    @ApiModelProperty(value = "是否有沟通记录，1无，2有")
    private String hasWorkChat;


    @ApiModelProperty(value = "企业编号列表")
    private List<Long> corpIdList;

    @ApiModelProperty(value = "服务网点编号列表")
    private List<Long> serviceBranchIdList;

    @ApiModelProperty(value = "工单是否关注")
    private String isAttention;

    @ApiModelProperty(value = "工单是否技术支持")
    private String isSupport;

    @ApiModelProperty(value = "是否已经回访", notes = "1=已回访，2=未回访")
    private Integer isReview;

    @ApiModelProperty(value = "是否审核通过", notes = "Y=是，N=否")
    private String isCheckPass;

    @ApiModelProperty(value = "工单费用录入状态")
    private Integer workFeeStatus;

    @ApiModelProperty(value = "是否需要录入工单费用")
    private String isWorkFee;

    @ApiModelProperty(value = "服务审核状态")
    private String finishCheckStatuses;

    @ApiModelProperty(value = "排除的服务确认状态", notes = "当选择了服务审核状态时，需要排除的确认状态列表")
    private List<Integer> notInFinishConfirmStatusList;

    @ApiModelProperty(value = "服务审核人")
    private Long finishCheckUser;

    @ApiModelProperty(value = "服务审核开始日期")
    private Date finishCheckStartDate;

    @ApiModelProperty(value = "服务审核结束日期")
    private Date finishCheckEndDate;

    @ApiModelProperty(value = "费用审核状态")
    private String feeCheckStatuses;

    @ApiModelProperty(value = "排除的费用确认状态", notes = "当选择了费用审核状态时，需要排除的确认状态列表")
    private List<Integer> notInFeeConfirmStatusList;

    @ApiModelProperty(value = "费用审核人")
    private Long feeCheckUser;

    @ApiModelProperty(value = "费用审核开始日期")
    private Date feeCheckStartDate;

    @ApiModelProperty(value = "费用审核结束日期")
    private Date feeCheckEndDate;

    @ApiModelProperty(value = "服务确认状态")
    private String finishConfirmStatuses;

    @ApiModelProperty(value = "确认服务内容操作人")
    private Long finishConfirmUser;

    @ApiModelProperty(value = "服务确认开始日期")
    private Date finishConfirmStartDate;

    @ApiModelProperty(value = "服务确认结束日期")
    private Date finishConfirmEndDate;

    @ApiModelProperty(value = "费用确认状态")
    private String feeConfirmStatuses;

    @ApiModelProperty(value = "确认费用内容操作人")
    private Long feeConfirmUser;

    @ApiModelProperty(value = "费用确认开始日期")
    private Date feeConfirmStartDate;

    @ApiModelProperty(value = "费用确认结束日期")
    private Date feeConfirmEndDate;

    @ApiModelProperty(value = "是否有基础维护费", notes = "N=无，Y=有")
    private String hasAssortFee;

    @ApiModelProperty(value = "附件状态，1=通过，2=未通过")
    private String filesStatus;

    @ApiModelProperty(value = "签名状态，1=通过，2=未通过")
    private String signatureStatus;

    @ApiModelProperty(value = "工单预警类型")
    private String[] remindTypes;

    @ApiModelProperty(value = "是否客服审核", notes = "Y=是，N=否")
    private String ifServiceCheck;

    @ApiModelProperty(value = "是否客户经理审核", notes = "Y=是，N=否")
    private String ifManagerCheck;

    @ApiModelProperty(value = "委托商列表")
    private List<Long> demanderCorpList;

    @ApiModelProperty(value = "范围权限列表")
    List<RightScopeDto> rightScopeList;
}
