package com.zjft.usp.anyfix.work.finish.dto;

import com.zjft.usp.anyfix.baseinfo.model.CustomFieldData;
import com.zjft.usp.anyfix.work.deal.dto.EngineerDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeImplementDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkPostDto;
import com.zjft.usp.anyfix.work.finish.model.WorkFinish;
import com.zjft.usp.anyfix.work.ware.dto.WareDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 服务完成dto
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/10/14 1:30 下午
 **/
@ApiModel("服务完成")
@Getter
@Setter
public class WorkFinishDto extends WorkFinish {

    @ApiModelProperty(value = "委托商")
    private Long demanderCorp;

    @ApiModelProperty(value = "客户ID")
    private Long customId;

    @ApiModelProperty(value = "客户企业编号")
    private Long customCorp;

    @ApiModelProperty(value = "客户企业名称")
    private String customCorpName;

    @ApiModelProperty(value = "服务商")
    private Long serviceCorp;

    @ApiModelProperty(value = "设备网点")
    private Long deviceBranch;

    @ApiModelProperty(value = "设备网点名称")
    private String deviceBranchName;

    @ApiModelProperty(value = "行政区划")
    private String district;

    @ApiModelProperty(value = "联系人姓名")
    private String contactName;

    @ApiModelProperty(value = "联系人电话")
    private String contactPhone;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "城郊", notes = "1=市区 2=郊县")
    private Integer zone;

    @ApiModelProperty(value = "工单类型")
    private Integer workType;

    @ApiModelProperty(value = "设备小类")
    private Long smallClass;

    @ApiModelProperty(value = "设备小类名称")
    private String smallClassName;

    @ApiModelProperty(value = "设备规格")
    private Long specification;

    @ApiModelProperty(value = "设备规格名称")
    private String specificationName;

    @ApiModelProperty(value = "设备编号")
    private String deviceCode;

    @ApiModelProperty(value = "出厂序列号")
    private String serial;

    @ApiModelProperty(value = "设备品牌")
    private Long brand;

    @ApiModelProperty(value = "设备型号")
    private Long model;

    @ApiModelProperty(value = "型号名称")
    private String modelName;

    @ApiModelProperty(value = "协同工程师列表")
    private List<EngineerDto> togetherEngineerList;

    @ApiModelProperty(value = "协同工程师")
    private String togetherEngineers;

    @ApiModelProperty(value = "外部协同人员")
    private String helpNames;

    @ApiModelProperty(value = "维保方式")
    private Integer warrantyMode;

    @ApiModelProperty(value = "使用备件列表")
    private List<WareDto> usedWareList;

    @ApiModelProperty(value = "回收备件列表")
    private List<WareDto> recycleWareList;

    @ApiModelProperty(value = "BASE64格式签名图片")
    private String signatureBase64;

    @ApiModelProperty(value = "回收备件邮寄单")
    private List<WorkPostDto> workPostDtoList;

    @ApiModelProperty(value = "工单费用")
    private WorkFeeDto workFeeDto;

    @ApiModelProperty(value = "实施发生费用列表")
    List<WorkFeeImplementDto> workFeeImplementDtoList;

    @ApiModelProperty(value = "自定义字段")
    private List<CustomFieldData> customFieldDataList;

    @ApiModelProperty(value = "设备描述")
    private String deviceDescription;

    @ApiModelProperty(value = "工单费用录入状态")
    private Integer workFeeStatus;

    @ApiModelProperty(value = "文件列表")
    private Map<String, List<Long>> fileConfigListMap;

    @ApiModelProperty(value = "工单系统类型")
    private Integer workSysType;

    @ApiModelProperty(value = "是否为完成附件")
    private boolean forFinishFile;

    @ApiModelProperty(value = "人天")
    private BigDecimal manDay;
}
