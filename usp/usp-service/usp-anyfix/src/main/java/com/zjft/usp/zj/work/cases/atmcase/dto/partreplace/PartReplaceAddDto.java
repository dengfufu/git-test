package com.zjft.usp.zj.work.cases.atmcase.dto.partreplace;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 添加备件更换DTO类
 *
 * @author zgpi
 * @date 2020-4-4 19:50
 **/
@ApiModel("添加备件更换DTO类")
@Data
public class PartReplaceAddDto {

    @ApiModelProperty(value = "CASE号")
    private String workCode;

    @ApiModelProperty(value = "服务器时间")
    private String serverTime;

    @ApiModelProperty(value = "CASE类型，包含子类型")
    private String workTypeAllName;

    @ApiModelProperty(value = "服务站")
    private String serviceBranch;

    @ApiModelProperty(value = "机器型号")
    private String deviceModel;

    @ApiModelProperty(value = "机器制造号")
    private String serial;

    @ApiModelProperty(value = "工程师")
    private String engineerId;

    @ApiModelProperty(value = "更换类型")
    private Integer replaceType;

    @ApiModelProperty(value = "硬件只换上不换下")
    private String aloneUpConfirmResult;

    @ApiModelProperty(value = "换下备件留置银行")
    private Integer isLeftBank;

    @ApiModelProperty(value = "备件编码")
    private String partCode;

    @ApiModelProperty(value = "备件名称")
    private String partName;

    @ApiModelProperty(value = "备件更换时间")
    private String replaceTime;

    @ApiModelProperty(value = "换上备件更换来源")
    private Integer replaceSource;

    @ApiModelProperty(value = "是否厂商备件")
    private String isVendorPart;

    @ApiModelProperty(value = "使用后是否需要厂商返还")
    private String isNeedReturn;

    @ApiModelProperty(value = "换上备件存放状态")
    private Integer storeStatus;

    @ApiModelProperty(value = "车牌号")
    private String carNo;

    @ApiModelProperty(value = "换上备件编号")
    private String newPartId;

    @ApiModelProperty(value = "换上备件条形码")
    private String newBarCode;

    @ApiModelProperty(value = "换上备件状态")
    private Integer status;

    @ApiModelProperty(value = "换上备件来源库房")
    private String upDepot;

    @ApiModelProperty(value = "换下备件带回库房")
    private String downDepot;

    @ApiModelProperty(value = "换下备件编号")
    private String oldPartId;

    @ApiModelProperty(value = "换下备件条形码")
    private String oldBarCode;

    @ApiModelProperty(value = "专用一维码")
    private String bar1codeReform;

    @ApiModelProperty(value = "备件更换数量")
    private Integer quantity;

    @ApiModelProperty(value = "对应硬件升级换下备件")
    private String upPartId;

    @ApiModelProperty(value = "备注")
    private String replaceDesc;

    @ApiModelProperty(value = "是否启用个人库存")
    private String enablePriStockPart;

    @ApiModelProperty(value = "是否启用个人库存查询")
    private String enablePriStockQuery;


}
