package com.zjft.usp.anyfix.settle.dto;

import com.zjft.usp.anyfix.common.feign.dto.FileInfoDto;
import com.zjft.usp.anyfix.settle.model.SettleDemanderPayment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 委托商结算单付款Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2020-04-28 21:51
 **/
@Data
@ApiModel(value = "委托商结算单付款Dto")
public class SettleDemanderPaymentDto extends SettleDemanderPayment {

    @ApiModelProperty(value = "付款操作人姓名")
    private String payOperatorName;

    @ApiModelProperty(value = "收款人姓名")
    private String receiptUserName;

    @ApiModelProperty(value = "开票状态名称")
    private String invoiceStatusName;

    @ApiModelProperty(value = "付款状态名称")
    private String payStatusName;

    @ApiModelProperty(value = "收款状态名称")
    private String receiptStatusName;

    @ApiModelProperty(value = "开票人姓名")
    private String invoiceUserName;

    @ApiModelProperty(value = "开票操作人姓名")
    private String invoiceOperatorName;

    @ApiModelProperty(value = "是否已收款")
    private String receipted;

    @ApiModelProperty(value = "开票附件ID列表")
    private List<Long> invoiceFileIdList;

    @ApiModelProperty(value = "开票附件列表")
    private List<FileInfoDto> invoiceFileList;

    @ApiModelProperty(value = "付款附件ID列表")
    private List<Long> payFileIdList;

    @ApiModelProperty(value = "付款附件列表")
    private List<FileInfoDto> payFileList;

    @ApiModelProperty(value = "收款附件ID列表")
    private List<Long> receiptFileIdList;

    @ApiModelProperty(value = "收款附件列表")
    private List<FileInfoDto> receiptFileList;

    @ApiModelProperty(value = "收款附件列表")
    private String payMethodName;
}
