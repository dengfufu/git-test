package com.zjft.usp.zj.work.cases.atmcase.dto.icbc;

import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.OldCaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author: JFZOU
 * @Date: 2020-03-30 10:34
 * @Version 1.0
 */
@ApiModel(value = "ICBC备件更换DTO类")
@Data
public class IcbcPartReplaceDto {
    @ApiModelProperty(value = "随机主键")
    private long replaceId;
    @ApiModelProperty(value = "CASE编号")
    private String caseId;
    @ApiModelProperty(value = "CASE编号")
    private String ycaseId;
    @ApiModelProperty(value = "机器型号")
    private String deviceType;
    @ApiModelProperty(value = "机器制造号")
    private String deviceCode;
    @ApiModelProperty(value = "设备小类")
    private String smallClass;
    @ApiModelProperty(value = "模块代码")
    private String moduleCode;
    @ApiModelProperty(value = "更换备注")
    private String replaceNote;
    @ApiModelProperty(value = "更换类型")
    private int replaceType;
    @ApiModelProperty(value = "更换数量")
    private int quantity;
    @ApiModelProperty(value = "设备小类+模块代码")
    private String smallClassAndCode;
    @ApiModelProperty(value = "更换类型名称")
    private String replaceTypeName;
    @ApiModelProperty(value = "机器型号名称")
    private String deviceTypeName;
    @ApiModelProperty(value = "备件名称")
    private String moduleCodeName;

    @ApiModelProperty(value = "旧平台CASE对象")
    private OldCaseDto oldCaseDto;

    @ApiModelProperty(value = "工行维修登记的备件名称映射")
    private Map<String, String> mapModuleCodeAndName;

    @ApiModelProperty(value = "模块对象列表")
    private List<IcbcModuleDto> icbcModuleDtoList;
}
