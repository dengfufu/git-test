package com.zjft.usp.anyfix.work.fee.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import com.zjft.usp.anyfix.work.fee.model.WorkFee;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 工单费用Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2020-01-07 15:28
 **/
@ApiModel("工单费用Dto")
@Getter
@Setter
public class WorkFeeDto extends WorkFee {

    @ApiModelProperty(value = "工单费用明细")
    List<WorkFeeDetailDto> detailList;

    @ApiModelProperty("工单编号")
    private String workCode;

    @ApiModelProperty("委托单号")
    private String checkWorkCode;

    @ApiModelProperty("创建时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date createTime;

    @ApiModelProperty("设备大类")
    private Long largeClassId;

    @ApiModelProperty("设备小类")
    private Long smallClassId;

    @ApiModelProperty("设备大类名称")
    private String largeClassName;

    @ApiModelProperty("设备小类名称")
    private String smallClassName;

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

    @ApiModelProperty(value = "规格")
    private Long specification;

    @ApiModelProperty(value = "规格名称")
    private String specificationName;

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

    @ApiModelProperty(value = "行政区划编号")
    private String district;

    @ApiModelProperty(value = "行政区划名称")
    private String districtName;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "服务商费用审核状态")
    private Integer feeCheckStatus;

    @ApiModelProperty(value = "服务商费用审核状态")
    private String feeCheckStatusName;

    @ApiModelProperty(value = "委托商费用确认状态")
    private Integer feeConfirmStatus;

    @ApiModelProperty(value = "委托商费用确认状态")
    private String feeConfirmStatusName;

    @ApiModelProperty(value = "服务完成时间")
    private Date endTime;

    @ApiModelProperty(value = "对账单编号")
    private Long verifyId;

    @ApiModelProperty(value = "对账后费用")
    private BigDecimal verifyAmount;

    @ApiModelProperty(value = "对账说明")
    private String verifyNote;

    @ApiModelProperty(value = "基础维护费", notes = "工单收费，不包含辅助人员费用")
    private BigDecimal assortBasicFee;

    @ApiModelProperty(value = "辅助人员费用")
    private BigDecimal assortSupportFee;

    @ApiModelProperty(value = "郊区交通费")
    private BigDecimal suburbTrafficExpense;

    @ApiModelProperty(value = "长途交通费")
    private BigDecimal longTrafficExpense;

    @ApiModelProperty(value = "住宿费")
    private BigDecimal hotelExpense;

    @ApiModelProperty(value = "出差补助")
    private BigDecimal travelExpense;

    @ApiModelProperty(value = "邮寄费")
    private BigDecimal postExpense;

    @ApiModelProperty(value = "结算单号")
    private Long settleId;

    @ApiModelProperty(value = "结算单明细编号")
    private Long settleDetailId;

}
