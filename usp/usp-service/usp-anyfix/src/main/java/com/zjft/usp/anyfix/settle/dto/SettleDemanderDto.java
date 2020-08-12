package com.zjft.usp.anyfix.settle.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import com.zjft.usp.anyfix.settle.model.SettleDemander;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeVerifyDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 供应商结算单Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2020-01-13 10:16
 **/
@ApiModel("供应商结算单Dto")
@Getter
@Setter
public class SettleDemanderDto extends SettleDemander {

    @ApiModelProperty("结算单明细list")
    List<SettleDemanderDetailDto> detailDtoList;

    @ApiModelProperty("结算对账单明细")
    private List<WorkFeeVerifyDto> workFeeVerifyDtoList;

    @ApiModelProperty("结算工单明细")
    private List<WorkFeeDto> workFeeDtoList;

    @ApiModelProperty("供应商名称")
    private String demanderCorpName;

    @ApiModelProperty("服务商名称")
    private String serviceCorpName;

    @ApiModelProperty("结算方式名称")
    private String settleWayName;

    @ApiModelProperty(value = "行政区划名称")
    private String districtName;

    @ApiModelProperty("结算单状态名称")
    private String statusName;

    @ApiModelProperty("结算人姓名")
    private String operatorName;

    @ApiModelProperty("核对人姓名")
    private String checkUserName;

    @ApiModelProperty("核对结果,Y=通过，N=不通过")
    private String checkResult;

    @ApiModelProperty("已确认工单数")
    private Integer checkedNum;

    @ApiModelProperty("未确认工单数")
    private Integer uncheckedNum;

    @ApiModelProperty(value = "开票状态名称")
    private String invoiceStatusName;

    @ApiModelProperty(value = "付款状态")
    private String payStatusName;

    @ApiModelProperty(value = "收款状态名称")
    private String receiptStatusName;

    @ApiModelProperty(value = "工单编号", notes = "按单结算时使用")
    private Long workId;

    @ApiModelProperty(value = "开票时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date invoiceTime;

    @ApiModelProperty(value = "收款确认时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date receiptTime;

    @ApiModelProperty(value = "结算单号前缀")
    private String prefix;

    @ApiModelProperty(value = "委托协议号")
    private String contNo;

}
