package com.zjft.usp.anyfix.work.request.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.baseinfo.model.CustomFieldData;
import com.zjft.usp.anyfix.common.feign.dto.FileInfoDto;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import com.zjft.usp.anyfix.work.assign.dto.WorkAssignEngineerDto;
import com.zjft.usp.anyfix.work.deal.dto.EngineerDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkPostDto;
import com.zjft.usp.anyfix.work.operate.dto.WorkOperateDto;
import com.zjft.usp.anyfix.work.ware.dto.WareDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 金融设备工单信息
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-02-24 22:08
 **/

@ApiModel(value = "金融设备工单信息")
@Data
public class CaseDto implements Serializable {

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

    @ApiModelProperty(value = "工单费用明细")
    private WorkFeeDto workFeeDto;

    @ApiModelProperty(value = "工单费用")
    private BigDecimal totalFee;

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

    @ApiModelProperty(value = "服务工程师姓名")
    private String engineerName;

    @ApiModelProperty(value = "协同工程师")
    private String togetherEngineers;

    @ApiModelProperty(value = "协同工程师列表")
    private List<EngineerDto> togetherEngineerList;

    @ApiModelProperty(value = "外部协同人员")
    private String helpNames;

    @ApiModelProperty(value = "处理过程")
    private List<WorkOperateDto> workOperateList;

    @ApiModelProperty(value = "服务项目")
    private Integer serviceItem;

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

    @ApiModelProperty(value = "委托商核对状态")
    private Integer demanderCheckStatus;

    @ApiModelProperty(value = "委托商核对状态名称")
    private String demanderCheckStatusName;

    @ApiModelProperty(value = "委托商核对说明")
    private String demanderCheckNote;

    @ApiModelProperty(value = "自定义字段数据")
    private List<CustomFieldData> customFieldDataList;

    @ApiModelProperty(value = "服务工程师ID拼接，以英文逗号分隔")
    private String engineers;

    @ApiModelProperty(value = "提单日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date dispatchTime;

    @ApiModelProperty(value = "签到时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date signTime;

    @ApiModelProperty(value = "交通工具")
    private Integer traffic;

    @ApiModelProperty(value = "交通说明")
    private String trafficNote;

    @ApiModelProperty(value = "交通工具名称")
    private String trafficName;

    @ApiModelProperty(value = "CASE编号")
    private String caseId;

    @ApiModelProperty(value = "机器型号ID")
    private String machineTypeId;

    @ApiModelProperty(value = "预约时间")
    private String bookTime;

    @ApiModelProperty(value = "终端号")
    private String terminalNO;

    @ApiModelProperty(value = "取消时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date cancelTime;

}
