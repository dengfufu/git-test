package com.zjft.usp.anyfix.work.fee.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeVerifyDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 对账单明细Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2020-05-12 17:29
 **/
@Getter
@Setter
@ApiModel(value = "对账单明细Dto")
public class WorkFeeVerifyDetailDto extends WorkFeeVerifyDetail {

    @ApiModelProperty("工单编号")
    private String workCode;

    @ApiModelProperty(value = "委托单号")
    private String checkWorkCode;

    @ApiModelProperty(value = "工单创建人")
    private Long creator;

    @ApiModelProperty(value = "创建人名字")
    private String creatorName;

    @ApiModelProperty("创建时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date createTime;

    @ApiModelProperty("设备小类")
    private Long smallClass;

    @ApiModelProperty("设备大类名称")
    private String largeClassName;

    @ApiModelProperty("设备小类名称")
    private String smallClassName;

    @ApiModelProperty(value = "设备规格")
    private Long specification;

    @ApiModelProperty(value = "设备规格名称")
    private String specificationName;

    @ApiModelProperty(value = "工单类型")
    private Integer workType;

    @ApiModelProperty(value = "工单类型名称")
    private String workTypeName;

    @ApiModelProperty(value = "服务方式")
    private Integer serviceMode;

    @ApiModelProperty(value = "服务方式名称")
    private String serviceModeName;

    @ApiModelProperty(value = "服务请求")
    private String serviceRequest;

    @ApiModelProperty(value = "客户关系编号")
    private Long customId;

    @ApiModelProperty(value = "设备客户")
    private Long customCorp;

    @ApiModelProperty(value = "设备客户名称")
    private String customCorpName;

    @ApiModelProperty(value = "设备网点")
    private Long deviceBranch;

    @ApiModelProperty(value = "设备网点名称")
    private String deviceBranchName;

    @ApiModelProperty(value = "服务商网点")
    private Long serviceBranch;

    @ApiModelProperty(value = "服务商网点名称")
    private String serviceBranchName;

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

    @ApiModelProperty(value = "行政区划")
    private String district;

    @ApiModelProperty(value = "省名称")
    private String provinceName;

    @ApiModelProperty(value = "市名称")
    private String cityName;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "总费用")
    private BigDecimal totalFee;

    @ApiModelProperty(value = "是否修改金额")
    private Integer modAmount;

    @ApiModelProperty(value = "费用确认状态")
    private Integer feeConfirmStatus;

    @ApiModelProperty(value = "费用确认状态名称")
    private String feeConfirmStatusName;

    @ApiModelProperty(value = "费用审核状态")
    private Integer feeCheckStatus;

    @ApiModelProperty(value = "费用审核状态名称")
    private String feeCheckStatusName;

    @ApiModelProperty(value = "工单费用Dto")
    private WorkFeeDto workFeeDto;

}
