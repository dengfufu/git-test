package com.zjft.usp.zj.work.cases.atmcase.dto.icbc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: JFZOU
 * @Date: 2020-04-01 8:59
 * @Version 1.0
 */
@Data
public class IcbcMediaDto {

    @ApiModelProperty(value = "随机主键")
    private long replaceId;
    @ApiModelProperty(value = "CASE编号，用于添加")
    private String ycaseId;
    @ApiModelProperty(value = "CASE编号，老系统命名不规范，不统一，用于修改")
    private String caseId;
    @ApiModelProperty(value = "机器型号")
    private String deviceType;
    @ApiModelProperty(value = "机器制造号")
    private String deviceCode;
    @ApiModelProperty(value = "是否更换存储介质(1=是，0=否)")
    private String isReplaceMedia;
    @ApiModelProperty(value = "存储介质名称")
    private String medName;
    @ApiModelProperty(value = "存储介质序列号")
    private String medSN;
    @ApiModelProperty(value = "行内介质交接人员统一认证号")
    private String handoverUser;
    @ApiModelProperty(value = "机器型号名称")
    private String deviceTypeName;
    @ApiModelProperty(value = "是否更换存储介质名称")
    private String isReplaceMediaName;
    @ApiModelProperty(value = "是否已确认")
    private String mediaConfirmName;
}
