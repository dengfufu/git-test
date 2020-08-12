package com.zjft.usp.anyfix.work.request.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.baseinfo.model.CustomFieldData;
import com.zjft.usp.anyfix.baseinfo.model.FaultType;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import com.zjft.usp.anyfix.work.request.model.WorkRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 工单请求DTO
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/9/23 5:01 下午
 **/
@Getter
@Setter
public class WorkRequestDto extends WorkRequest {

    @ApiModelProperty(value = "设备数量")
    private Integer deviceNum;

    @ApiModelProperty(value = "设备ID", notes = "多个用英文逗号隔开")
    private String deviceIds;

    @ApiModelProperty(value = "出厂序列号", notes = "多个用英文逗号隔开")
    private String serials;

    @ApiModelProperty(value = "设备编号", notes = "多个用英文逗号隔开")
    private String deviceCodes;

    @ApiModelProperty(value = "设备ID")
    private Long deviceId;

    @ApiModelProperty(value = "设备小类名称")
    private String smallClassName;

    @ApiModelProperty(value = "网点名称")
    private String deviceBranchName;

    @ApiModelProperty(value = "服务商")
    private Long serviceCorp;

    @ApiModelProperty(value = "客户名称")
    private String customCorpName;

    @ApiModelProperty(value = "故障现象列表")
    private List<FaultType> faultTypeList;

    @ApiModelProperty(value = "自定义字段")
    private List<CustomFieldData> customFieldDataList;

    @ApiModelProperty(value = "建单人类型")
    private Integer creatorType;

    @ApiModelProperty(value = "工单状态")
    private Integer workStatus;

    @ApiModelProperty(value = "是否重提单")
    private Boolean ifResubmitWork;

    @ApiModelProperty(value = "重新提单工单号")
    private String resubmitWorkCode;

    @ApiModelProperty(value = "服务时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date serviceTime;

    @ApiModelProperty(value = "新增的企业")
    private List<Long> newFileIdList;

    @ApiModelProperty(value = "删除的企业")
    private List<Long> deleteFileIdList;


    @ApiModelProperty(value = "费用报价")
    private BigDecimal basicServiceFee;

    @ApiModelProperty(value = "其他费用")
    private BigDecimal otherFee;

    @ApiModelProperty(value = "其他费用说明")
    private String otherFeeNote;

    @ApiModelProperty(value = "提单时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date dispatchTime;

    @ApiModelProperty(value = "微信建单方式: scan - 扫一扫；demander: 委托商建单")
    private String wxCreateType;

    @ApiModelProperty(value = "设备ID List")
    private List<Long> deviceIdList;

    @ApiModelProperty(value = "设备状态List")
    private List<Integer> workStatusList;

    @ApiModelProperty(value = "设备序列号集合")
    private List<String> serialList;
}
