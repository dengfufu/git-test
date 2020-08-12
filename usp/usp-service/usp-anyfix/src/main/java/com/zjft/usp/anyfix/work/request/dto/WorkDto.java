package com.zjft.usp.anyfix.work.request.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.baseinfo.dto.CustomFieldDataDto;
import com.zjft.usp.anyfix.baseinfo.model.FaultType;
import com.zjft.usp.anyfix.common.feign.dto.FileInfoDto;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import com.zjft.usp.anyfix.corp.fileconfig.dto.WorkFilesDto;
import com.zjft.usp.anyfix.work.assign.dto.WorkAssignEngineerDto;
import com.zjft.usp.anyfix.work.deal.dto.EngineerDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDetailDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkPostDto;
import com.zjft.usp.anyfix.work.follow.dto.WorkFollowDto;
import com.zjft.usp.anyfix.work.operate.dto.WorkOperateDto;
import com.zjft.usp.anyfix.work.sign.model.WorkSign;
import com.zjft.usp.anyfix.work.support.dto.WorkSupportDto;
import com.zjft.usp.anyfix.work.ware.dto.WareDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 工单信息
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/10/11 9:48 上午
 **/
@ApiModel(value = "工单信息")
@Data
public class WorkDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单ID")
    private Long workId;

    @ApiModelProperty(value = "工单号")
    private String workCode;

    @ApiModelProperty(value = "委托单号")
    private String checkWorkCode;

    @ApiModelProperty(value = "工单状态")
    private Integer workStatus;

    @ApiModelProperty(value = "工单状态名称")
    private String workStatusName;

    @ApiModelProperty(value = "工单类型")
    private Integer workType;

    @ApiModelProperty(value = "工单类型名称")
    private String workTypeName;

    @ApiModelProperty(value = "工单系统类型")
    private Integer workSysType;

    @ApiModelProperty(value = "请求来源 1=APP，2=微信，3=PC")
    private Integer source;

    @ApiModelProperty(value = "服务方式")
    private Integer serviceMode;

    @ApiModelProperty(value = "服务方式名称")
    private String serviceModeName;

    @ApiModelProperty(value = "服务请求")
    private String serviceRequest;

    @ApiModelProperty(value = "委托商")
    private Long demanderCorp;

    @ApiModelProperty(value = "委托商名称")
    private String demanderCorpName;

    @ApiModelProperty(value = "客户编号")
    private Long customId;

    @ApiModelProperty(value = "客户企业编号")
    private Long customCorp;

    @ApiModelProperty(value = "客户企业名称")
    private String customCorpName;

    @ApiModelProperty(value = "服务商")
    private Long serviceCorp;

    @ApiModelProperty(value = "服务商名称")
    private String serviceCorpName;

    @ApiModelProperty(value = "设备网点")
    private Long deviceBranch;

    @ApiModelProperty(value = "设备网点名称")
    private String deviceBranchName;

    @ApiModelProperty(value = "服务商网点")
    private Long serviceBranch;

    @ApiModelProperty(value = "服务商网点名称")
    private String serviceBranchName;

    @ApiModelProperty(value = "行政区划")
    private String district;

    @ApiModelProperty(value = "行政区划名称")
    private String districtName;

    @ApiModelProperty(value = "城郊，1=市区 2=郊县")
    private Integer zone;

    @ApiModelProperty(value = "省名称")
    private String provinceName;

    @ApiModelProperty(value = "市名称")
    private String cityName;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "联系人姓名")
    private String contactName;

    @ApiModelProperty(value = "联系人电话")
    private String contactPhone;

    @ApiModelProperty(value = "预约时间开始")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date bookTimeBegin;

    @ApiModelProperty(value = "预约时间结束")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date bookTimeEnd;

    @ApiModelProperty(value = "优先级")
    private Integer priority;

    @ApiModelProperty(value = "优先级名称")
    private String priorityName;

    @ApiModelProperty(value = "设备大类ID")
    private Long largeClassId;

    @ApiModelProperty(value = "设备大类名称")
    private String largeClassName;

    @ApiModelProperty(value = "设备小类ID")
    private Long smallClass;

    @ApiModelProperty(value = "设备小类名称")
    private String smallClassName;

    @ApiModelProperty(value = "设备规格ID")
    private Long specification;

    @ApiModelProperty(value = "设备规格名称")
    private String specificationName;

    @ApiModelProperty(value = "设备系统编号")
    private Long deviceId;

    @ApiModelProperty(value = "设备编号")
    private String deviceCode;

    @ApiModelProperty(value = "出厂序列号")
    private String serial;

    @ApiModelProperty(value = "设备品牌")
    private Long brand;

    @ApiModelProperty(value = "设备品牌名称")
    private String brandName;

    @ApiModelProperty(value = "设备型号")
    private Long model;

    @ApiModelProperty(value = "设备型号名称")
    private String modelName;

    @ApiModelProperty(value = "保修状态")
    private Integer warranty;

    @ApiModelProperty(value = "保修状态名称")
    private String warrantyName;

    @ApiModelProperty(value = "维保方式，10=整机保 20=单次保")
    private Integer warrantyMode;

    @ApiModelProperty(value = "维保方式名称")
    private String warrantyModeName;

    @ApiModelProperty(value = "故障时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date faultTime;

    @ApiModelProperty(value = "故障现象")
    private String faultType;

    @ApiModelProperty(value = "故障现象")
    private String faultTypeName;

    @ApiModelProperty(value = "故障代码")
    private String faultCode;

    @ApiModelProperty(value = "工单来源")
    private String sourceName;

    @ApiModelProperty(value = "使用部件")
    private List<WareDto> usedPartList;

    @ApiModelProperty(value = "故障部件")
    private List<WareDto> faultPartList;

    @ApiModelProperty(value = "故障备件邮寄单")
    private List<WorkPostDto> workPostDtoList;

    @ApiModelProperty(value = "费用报价")
    private BigDecimal basicServiceFee;

    @ApiModelProperty(value = "其他费用")
    private BigDecimal otherFee;

    @ApiModelProperty(value = "其他费用说明")
    private String otherFeeNote;

    @ApiModelProperty(value = "工单总费用")
    private BigDecimal totalFee;

    @ApiModelProperty(value = "工单费用")
    private WorkFeeDto workFeeDto;

    @ApiModelProperty(value = "工单费用明细")
    private List<WorkFeeDetailDto> workFeeDetailDtoList;

    @ApiModelProperty(value = "工单费用录入状态")
    private Integer workFeeStatus;

    @ApiModelProperty(value = "工单费用录入状态名称")
    private String workFeeStatusName;

    @ApiModelProperty(value = "经度")
    private BigDecimal lon;

    @ApiModelProperty(value = "维度")
    private BigDecimal lat;

    @ApiModelProperty(value = "创建人ID")
    private Long creator;

    @ApiModelProperty(value = "创建人姓名")
    private String creatorName;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date createTime;

    @ApiModelProperty(value = "分派的工程师列表")
    private List<WorkAssignEngineerDto> assignEngineerList;

    @ApiModelProperty(value = "服务工程师")
    private Long engineer;

    @ApiModelProperty(value = "签到时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date signTime;

    @ApiModelProperty(value = "服务工程师姓名")
    private String engineerName;

    @ApiModelProperty(value = "协同工程师")
    private String togetherEngineers;

    @ApiModelProperty(value = "协同工程师列表")
    private List<EngineerDto> togetherEngineerList;

    @ApiModelProperty(value = "签到记录")
    private List<WorkSign> workSignList;

    @ApiModelProperty(value = "外部协同人员")
    private String helpNames;

    @ApiModelProperty(value = "处理过程")
    private List<WorkOperateDto> workOperateList;

    @ApiModelProperty(value = "服务项目")
    private String serviceItem;

    @ApiModelProperty(value = "服务项目名称")
    private String serviceItemName;

    @ApiModelProperty(value = "远程处理方式")
    private Integer remoteWay;

    @ApiModelProperty(value = "远程处理方式名称")
    private String remoteWayName;

    @ApiModelProperty(value = "完成描述")
    private String finishDescription;

    @ApiModelProperty(value = "完成时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date finishTime;

    @ApiModelProperty(value = "完成时附件list")
    private List<FileInfoDto> finishFileList;

    @ApiModelProperty(value = "客户签名图片编号")
    private Long signature;

    @ApiModelProperty(value = "工单附件")
    private List<FileInfoDto> fileList;

    @ApiModelProperty(value = "服务商服务审核状态")
    private Integer finishCheckStatus;

    @ApiModelProperty(value = "服务商服务审核状态名称")
    private String finishCheckStatusName;

    @ApiModelProperty(value = "服务商服务审核人")
    private Long finishCheckUser;

    @ApiModelProperty(value = "服务商服务审核人姓名")
    private String finishCheckUserName;

    @ApiModelProperty(value = "服务商服务审核时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date finishCheckTime;

    @ApiModelProperty(value = "服务商服务审核备注")
    private String finishCheckNote;

    @ApiModelProperty(value = "服务商费用审核状态")
    private Integer feeCheckStatus;

    @ApiModelProperty(value = "服务商费用审核状态名称")
    private String feeCheckStatusName;

    @ApiModelProperty(value = "服务商费用审核人")
    private Long feeCheckUser;

    @ApiModelProperty(value = "服务商费用审核人姓名")
    private String feeCheckUserName;

    @ApiModelProperty(value = "服务商费用审核时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date feeCheckTime;

    @ApiModelProperty(value = "服务商费用审核备注")
    private String feeCheckNote;

    @ApiModelProperty(value = "服务商服务确认状态")
    private Integer finishConfirmStatus;

    @ApiModelProperty(value = "服务商服务确认状态名称")
    private String finishConfirmStatusName;

    @ApiModelProperty(value = "委托商确认服务内容备注")
    private String finishConfirmNote;

    @ApiModelProperty(value = "委托商确认服务内容操作人")
    private Long finishConfirmUser;

    @ApiModelProperty(value = "委托商确认服务内容操作人姓名")
    private String finishConfirmUserName;

    @ApiModelProperty(value = "服务商服务确认时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date finishConfirmTime;

    @ApiModelProperty(value = "服务商费用确认状态")
    private Integer feeConfirmStatus;

    @ApiModelProperty(value = "服务商费用确认状态名称")
    private String feeConfirmStatusName;

    @ApiModelProperty(value = "服务审核确认状态名称")
    private String finishStatusName;

    @ApiModelProperty(value = "费用审核确认状态名称")
    private String feeStatusName;

    @ApiModelProperty(value = "委托商确认费用内容备注")
    private String feeConfirmNote;

    @ApiModelProperty(value = "委托商确认费用内容操作人")
    private Long feeConfirmUser;

    @ApiModelProperty(value = "委托商确认费用内容操作人姓名")
    private String feeConfirmUserName;

    @ApiModelProperty(value = "服务商费用确认时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date feeConfirmTime;

    @ApiModelProperty(value = "委托商结算状态")
    private Integer settleDemanderStatus;

    @ApiModelProperty(value = "委托商结算状态名称")
    private String settleDemanderStatusName;

    @ApiModelProperty(value = "自定义字段数据")
    private List<CustomFieldDataDto> customFieldDataList;

    @ApiModelProperty(value = "服务工程师ID拼接，以英文逗号分隔")
    private String engineers;

    @ApiModelProperty(value = "服务工程师姓名拼接，以英文逗号分隔")
    private String engineerNames;

    @ApiModelProperty(value = "提单日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date dispatchTime;

    @ApiModelProperty(value = "交通工具")
    private Integer traffic;

    @ApiModelProperty(value = "交通说明")
    private String trafficNote;

    @ApiModelProperty(value = "出发时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date goTime;

    @ApiModelProperty(value = "服务开始时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date startTime;

    @ApiModelProperty(value = "重提单的工单ID")
    private Long resubmitWorkId;

    @ApiModelProperty(value = "服务情况")
    private String description;

    @ApiModelProperty(value = "故障现象列表")
    private List<FaultType> faultTypeList;

    @ApiModelProperty(value = "设备数量")
    private Integer deviceNum;

    @ApiModelProperty(value = "设备ID", notes = "多个用英文逗号隔开")
    private String deviceIds;

    @ApiModelProperty(value = "出厂序列号", notes = "多个用英文逗号隔开")
    private String serials;

    @ApiModelProperty(value = "设备编号", notes = "多个用英文逗号隔开")
    private String deviceCodes;

    @ApiModelProperty(value = "建单人类型")
    private Integer creatorType;

    @ApiModelProperty(value = "使用备件列表")
    private List<WareDto> usedWareList;

    @ApiModelProperty(value = "回收备件列表")
    private List<WareDto> recycleWareList;

    @ApiModelProperty(value = "BASE64格式签名图片")
    private String signatureBase64;

    @ApiModelProperty(value = "是否解决")
    private String solved;

    @ApiModelProperty(value = "服务完成时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date endTime;

    @ApiModelProperty(value = "工单附件|维修附件")
    private String workFiles;

    @ApiModelProperty(value = "服务完成附件")
    private String files;

    @ApiModelProperty(value = "处理人")
    private Long operator;

    @ApiModelProperty(value = "处理人姓名")
    private String operatorName;

    @ApiModelProperty(value = "远程服务时间")
    private Date serviceTime;

    @ApiModelProperty(value = "是否补单")
    private String isSupplement;

    @ApiModelProperty(value = "接单时间")
    private Date acceptTime;

    @ApiModelProperty(value = "设备描述")
    private String deviceDescription;

    @ApiModelProperty(value = "工单是否关注")
    private String isAttention;

    @ApiModelProperty(value = "工单是否技术支持")
    private String isSupport;

    @ApiModelProperty(value = "技术支持跟踪记录")
    private List<WorkSupportDto> workSupportList;

    @ApiModelProperty(value = "跟进记录")
    private List<WorkFollowDto> workFollowList;

    @ApiModelProperty(value = "当前时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date currentTime;

    @ApiModelProperty(value = "预警类型")
    private int remindType;

    @ApiModelProperty(value = "预警类型名称")
    private String remindTypeName;

    @ApiModelProperty(value = "预计完成时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date planFinishTime;

    @ApiModelProperty(value = "预警时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date remindTime;

    @ApiModelProperty(value = "实际超时时间")
    private int overTime;

    @ApiModelProperty(value = "最少超时时间（预警条件）")
    private int expireTimeMin;

    @ApiModelProperty(value = "分组附件")
    private List<WorkFilesDto> fileGroupList;

    @ApiModelProperty(value = "文件列表")
    private Map<String, List<Long>> fileConfigListMap;

    @ApiModelProperty(value = "受理时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date handleTime;

    @ApiModelProperty(value = "派单时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date assignTime;

    @ApiModelProperty(value = "附件状态")
    private String filesStatus;

    @ApiModelProperty(value = "签名状态")
    private String signatureStatus;

    @ApiModelProperty(value = "签名描述")
    private String signatureDescription;

    @ApiModelProperty(value = "人天")
    private BigDecimal manDay;
}
