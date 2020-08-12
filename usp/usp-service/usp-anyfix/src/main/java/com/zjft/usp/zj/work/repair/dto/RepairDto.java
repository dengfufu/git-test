package com.zjft.usp.zj.work.repair.dto;

import com.zjft.usp.zj.work.workorder.dto.WorkOrderDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 老平台报修信息
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-18 10:14
 **/
@ApiModel(value = "老平台报修信息")
@Data
public class RepairDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "报修编号")
    private String id;

    @ApiModelProperty(value = "报修客户")
    private String bankCode;

    @ApiModelProperty(value = "网点名称")
    private String branchName;

    @ApiModelProperty(value = "机器型号")
    private String deviceType;

    @ApiModelProperty(value = "机器制造号")
    private String deviceCode;

    @ApiModelProperty(value = "终端号")
    private String NUM;

    @ApiModelProperty(value = "故障时间")
    private String faultTime;

    @ApiModelProperty(value = "报修时间")
    private String reportTime;

    @ApiModelProperty(value = "报修人")
    private String reportPeople;

    @ApiModelProperty(value = "报修电话")
    private String reportTel;

    @ApiModelProperty(value = "其他联系电话")
    private String lxTel;

    @ApiModelProperty(value = "报修内容")
    private String content;

    @ApiModelProperty(value = "处理情况")
    private String dealNote;

    @ApiModelProperty(value = "处理时间")
    private String dealTime;

    @ApiModelProperty(value = "行方报障ID")
    private String dmssCaseId;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "服务站")
    private String depotId;

    @ApiModelProperty(value = "片区")
    private String areaCode;

    @ApiModelProperty(value = "产品系列")
    private String seriesName;

    @ApiModelProperty(value = "处理人")
    private String dealPeople;

    @ApiModelProperty(value = "处理人手机")
    private String dealManPhone;

    @ApiModelProperty(value = "受理人")
    private String acceptPerson;

    @ApiModelProperty(value = "现场联系人")
    private String contactsName;

    @ApiModelProperty(value = "现场联系人手机")
    private String contactsPhone;

    @ApiModelProperty(value = "服务评分")
    private Integer evaluation;

    @ApiModelProperty(value = "评价描述")
    private String evaluateDesc;

    @ApiModelProperty(value = "评价人")
    private String appraiser;

    @ApiModelProperty(value = "评价方式")
    private String appraiserPhone;

    @ApiModelProperty(value = "评价时间")
    private String evaluateTime;

    @ApiModelProperty(value = "保修状态")
    private String bxdate;

    @ApiModelProperty(value = "行方标识【1=工行】")
    private String serviceId;

    @ApiModelProperty(value = "OME工单号【工单在OME应用的唯一标识")
    private String appNo;

    @ApiModelProperty(value = "工单类型【工单的类型，参见OME工单类型】")
    private String appType;

    @ApiModelProperty(value = "服务类型【录服务单时，此字段必输。参考OME服务单类型表】")
    private String serType;

    @ApiModelProperty(value = "OME工单状态【工单状态，参见OME工单状态】")
    private String appStatus;

    @ApiModelProperty(value = "紫金工单状态【1=待分派； 2=已分派；3=已预约； 4=case中； 5=已到达； 6=已完成；7=已退单；8=行里撤单；9=行里关闭；10=例外关闭；】")
    private String bxAppStatus;

    @ApiModelProperty(value = "预约时间【格式：yyyymmddhhMMss】")
    private String preBookTime;

    @ApiModelProperty(value = "设备所在行政区划【设备所在机构的省市区县，如：浙江省|温州市|鹿城区】")
    private String adminValue;

    @ApiModelProperty(value = "是否自动工单【故障单类型工单才有此信息，1-是；0-否(默认)；2-自动转手工】")
    private String autoCreate;

    @ApiModelProperty(value = "要求服务商处理时间【故障单类型工单才有此信息】")
    private String requirerTime;

    @ApiModelProperty(value = "服务主管")
    private String manager;

    @ApiModelProperty(value = "服务主管电话")
    private String managerPhone;

    @ApiModelProperty(value = "服务工程师")
    private String engineer;

    @ApiModelProperty(value = "服务工程师电话")
    private String engineerPhone;

    @ApiModelProperty(value = "到达时间【格式：yyyymmddhhMMss】")
    private String arriveTime;

    @ApiModelProperty(value = "是否电话处理完成【0-否 1-服务主管处理 2-工程师处理】")
    private String remoteFlag;

    @ApiModelProperty(value = "处理开始时间【格式：yyyymmddhhMMss】")
    private String beginWorkTime;

    @ApiModelProperty(value = "处理完成时间【格式：yyyymmddhhMMss】")
    private String endWorkTime;

    @ApiModelProperty(value = "退单原因【故障单，服务商退回环节为必输字段（1-设备已报废 2- 网点答复设备不维修 3-分派有误 4-忙不过来 5-其他）】")
    private String retReason;

    @ApiModelProperty(value = "是否有催单 0-否 1-有")
    private String supervised;

    @ApiModelProperty(value = "是否行外补单 1-是，0-否")
    private String facadded;

    @ApiModelProperty(value = "CASE编号")
    private String caseId;

    @ApiModelProperty(value = "故障报修接收建单表对象")
    private BxReceiveCreateDto bxReceiveCreate;

    @ApiModelProperty(value = "OME当前工单状态")
    private String appStatus2;

    @ApiModelProperty(value = "人脸识别开关（0-关；1-开）")
    private String faceidentFlag;

    @ApiModelProperty(value = "行内陪同人员签到开关（0-关；1-开）")
    private String signEscort;

    @ApiModelProperty(value = "故障报修附加表")
    private RepairExtraDto repairExtra;

    @ApiModelProperty(value = "处理结果")
    private String dealResult;

    @ApiModelProperty(value = "对应的派工单是否关闭(Y-关闭)")
    private List<WorkOrderDto> workOrderList;

    @ApiModelProperty(value = "未处理超时时间显示红色")
    private String isDisplayRed;

    @ApiModelProperty(value = "报修客户名称")
    private String bankName;

    @ApiModelProperty(value = "机器型号名称")
    private String deviceTypeName;

    @ApiModelProperty(value = "服务站名称")
    private String depotName;

    @ApiModelProperty(value = "处理人名称")
    private String dealPeopleName;

    @ApiModelProperty(value = "受理人名称")
    private String acceptPersonName;

    @ApiModelProperty(value = "片区名称")
    private String areaCodeName;

    @ApiModelProperty(value = "紫金CASE编号")
    private String yjcaseId;

    @ApiModelProperty(value = "服务评分")
    private String evaluationName;

    @ApiModelProperty(value = "预约时间【格式：yyyy-MM-dd hh:MM】")
    private String displayPreBookTime;

    @ApiModelProperty(value = "要求服务商处理时间【格式：yyyy-MM-dd hh:MM】")
    private String displayRequirerTime;

    @ApiModelProperty(value = "到达时间【格式：yyyy-MM-dd hh:MM】")
    private String displayArriveTime;

    @ApiModelProperty(value = "处理开始时间【格式：yyyy-MM-dd hh:MM】")
    private String displayBeginWorkTime;

    @ApiModelProperty(value = "处理结束时间【格式：yyyy-MM-dd hh:MM】")
    private String displayEndWorkTime;

    @ApiModelProperty(value = "服务主管名称")
    private String managerName;

    @ApiModelProperty(value = "服务工程师名称")
    private String engineerName;

    @ApiModelProperty(value = "工单类型名称")
    private String appTypeName;

    @ApiModelProperty(value = "紫金工单状态名称")
    private String bxAppStatusName;

    @ApiModelProperty(value = "OME工单状态")
    private String appTypeAndStatusName;

    @ApiModelProperty(value = "是否自动工单名称")
    private String autoCreateName;

    @ApiModelProperty(value = "是否电话处理完成名称")
    private String remoteFlagName;

    @ApiModelProperty(value = "退单原因名称")
    private String retReasonName;

    @ApiModelProperty(value = "是否有催单名称")
    private String supervisedName;

    @ApiModelProperty(value = "是否行外补单名称")
    private String facaddedName;

    @ApiModelProperty(value = "服务类型名称")
    private String serTypeName;

    @ApiModelProperty(value = "工单描述")
    private String contentAndAddDesc;

    @ApiModelProperty(value = "1=工行模式")
    private String bankMode;

    @ApiModelProperty(value = "派工单状态")
    private int workOrderStatus;

    @ApiModelProperty(value = "派工单状态名称")
    private String workOrderStatusName;

    @ApiModelProperty(value = "OME工单当前状态名称")
    private String appTypeAndStatus2Name;

    @ApiModelProperty(value = "人脸识别开关名称")
    private String faceidentFlagName;

    @ApiModelProperty(value = "网点座机")
    private String branchTel;

    @ApiModelProperty(value = "行内陪同人员签到开关名称")
    private String signEscortName;

    @ApiModelProperty(value = "时间戳，用于控制并发")
    private long modTime;

    @ApiModelProperty(value = "最新的派工信息")
    private WorkOrderDto workOrderDto;

}
