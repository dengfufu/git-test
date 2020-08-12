package com.zjft.usp.zj.work.cases.atmcase.dto.partreplace;

import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.CaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 修改备件更换页面DTO
 *
 * @author zgpi
 * @version 1.0
 * @date 2020-04-05 16:32
 **/
@ApiModel("修改备件更换页面DTO")
@Data
public class PartReplaceModPageDto {

    @ApiModelProperty(value = "备件更换对象")
    private PartReplaceModDto partReplaceModDto;

    @ApiModelProperty(value = "当前用户")
    private String userId;

    @ApiModelProperty(value = "当前时间")
    private String currentTime;

    @ApiModelProperty(value = "ATM机CASE")
    private CaseDto caseDto;

    @ApiModelProperty(value = "制造号列表")
    private List<String> serialList;

    @ApiModelProperty(value = "更换类型列表")
    private List<ReplaceTypeDto> replaceTypeList;

    @ApiModelProperty(value = "备件类型列表")
    private List<PartTypeDto> partTypeList;

    @ApiModelProperty(value = "备件编码与是否粘贴条形码映射")
    private List<PartUseBarCodeDto> partUseBarCodeList;

    @ApiModelProperty(value = "备件列表")
    private List<PartInfoDto> partInfoList;

    @ApiModelProperty(value = "库房列表")
    private List<DepotInfoDto> depotInfoList;

    @ApiModelProperty(value = "备件来源映射")
    private List<ReplaceSourceDto> replaceSourceList;

    @ApiModelProperty(value = "备件状态列表")
    private List<PartStatusDto> partStatusList;

    @ApiModelProperty(value = "专用一维码")
    private String bar1codeReform;

    @ApiModelProperty(value = "是否启用个人备件库存")
    private String enablePriStockPart;

    @ApiModelProperty(value = "是否启用个人备件库存查询")
    private String enablePriStockQuery;

    @ApiModelProperty(value = "备件库存状态列表")
    private List<PartStoreStatusDto> partStoreStatusList;

    @ApiModelProperty(value = "工程师列表")
    private List<EngineerDto> engineerList;
}
