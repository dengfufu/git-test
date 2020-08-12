package com.zjft.usp.zj.work.cases.atmcase.dto.partreplace;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: JFZOU
 * @Date: 2020-03-23 15:39
 * @Version 1.0
 */

@ApiModel("备件更换DTO类")
@Data
public class PartReplaceDto {

    @ApiModelProperty(value = "备件更换记录ID")
    private String id;
    @ApiModelProperty(value = "CASE编号")
    private String ycaseId;
    @ApiModelProperty(value = "更换备件所属服务站")
    private String caseDepot;
    @ApiModelProperty(value = "机器型号")
    private String machineType;
    @ApiModelProperty(value = "机器制造号")
    private String machineCode;
    @ApiModelProperty(value = "更换类型")
    private int replaceType;
    @ApiModelProperty(value = "备件模块")
    private String zcode;
    @ApiModelProperty(value = "更换时间")
    private String replaceTime;
    @ApiModelProperty(value = "换下备件进入库房")
    private String downDepot;
    @ApiModelProperty(value = "换下备件编号")
    private String oldPartId;
    @ApiModelProperty(value = "换下备件条形码")
    private String oldBarcode;
    @ApiModelProperty(value = "是否留置给银行(1=是 2=否)")
    private int isLeftBank;
    @ApiModelProperty(value = "确认硬件升级只有换上没有换下")
    private String aloneUpConfirmResult;
    @ApiModelProperty(value = "换上备件来源")
    private int useSource;
    @ApiModelProperty(value = "换上备件来源库房")
    private String upDepot;
    @ApiModelProperty(value = "换上备件状态")
    private int status;
    @ApiModelProperty(value = "换上备件编号")
    private String newPartId;
    @ApiModelProperty(value = "换上备件条形码")
    private String newBarcode;
    @ApiModelProperty(value = "硬件改造专用一维码")
    private String bar1codeReform;
    @ApiModelProperty(value = "更换备件数量,默认为1")
    private int quantity;
    @ApiModelProperty(value = "更换备注")
    private String replaceDesc;
    @ApiModelProperty(value = "是否异常")
    private String ifException;
    @ApiModelProperty(value = "异常备注")
    private String exceptionDesc;
    @ApiModelProperty(value = "操作人员用户名")
    private String operatorId;
    @ApiModelProperty(value = "操作时间")
    private String operateTime;
    @ApiModelProperty(value = "附加字段，这里使用MO_YJCASESBGH_T中字段含义 是否通过800审核的标记（Y=是，N=否）")
    private String passed;
    @ApiModelProperty(value = "机器型号名称")
    private String machineTypeName;
    @ApiModelProperty(value = "备件模块名称")
    private String zcodeName;
    @ApiModelProperty(value = "换下备件进入库房名称")
    private String downDepotName;
    @ApiModelProperty(value = "换上备件来源库房名称")
    private String upDepotName;
    @ApiModelProperty(value = "换上备件来源名称")
    private String useSourceName;
    @ApiModelProperty(value = "换上备件状态名称")
    private String statusName;
    @ApiModelProperty(value = "更换类型名称")
    private String replaceTypeName;
    @ApiModelProperty(value = "已选择数量")
    private Integer hasSelectQuantity;
}
