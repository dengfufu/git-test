package com.zjft.usp.zj.work.cases.atmcase.dto.atmcase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * CASE关闭页面Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2020-04-02 09:11
 **/
@ApiModel(value = "CASE关闭页面Dto")
@Data
public class CaseFinishPageDto {

    @ApiModelProperty(value = "CASE关闭Dto")
    private CaseFinishDto caseFinishDto;

    @ApiModelProperty(value = "当前机器map", notes = "key为器型号编号_机器制造号")
    private Map<String, Object> curDeviceMap;

    @ApiModelProperty(value = "CASE机器", notes = "key为机器型号编号_机器制造号")
    private Map<String, Object> deviceInfoMap;

    @ApiModelProperty(value = "机器型号编号与名称映射")
    private Map<String, String> modelMap;

    @ApiModelProperty(value = "故障模块编号与名称映射")
    private Map<String, String> faultModelMap;

    @ApiModelProperty(value = "处理结果编号与名称映射")
    private Map<Integer, String> caseDealMap;

    @ApiModelProperty(value = "PM处理方式编号与名称映射")
    private Map<Integer, String> dealWayMap;

    @ApiModelProperty(value = "维护类型编号与名称映射")
    private Map<Integer, String> maintainMap;

    @ApiModelProperty(value = "预设粘贴一维码映射")
    private Map<String, String> pasteBarcodeMap;

    @ApiModelProperty(value = "软件版本映射")
    private Map<String, Object> softVersionMap;

    @ApiModelProperty(value = "机器映射")
    private Map<String, String> machineMap;

    @ApiModelProperty(value = "非接触式读卡器模块编号与名称映射")
    private Map<String, String> zModuleICReaderMap;

    @ApiModelProperty(value = "是否需要上传工行对接照片标志")
    private Boolean needIcbcImageFlag;

    @ApiModelProperty(value = "必须上传工行对接照片标志")
    private Boolean mustUploadIcbcImageFlag;

    @ApiModelProperty(value = "必须上传单据照片标志")
    private Boolean mustUploadReceiptPicFlag;

    @ApiModelProperty(value = "第一个case标志")
    private Boolean firstCaseFlag;

    @ApiModelProperty(value = "允许在线保修标志")
    private Boolean enableOnlineBxFlag;

    @ApiModelProperty(value = "是否启用工行对接")
    private Boolean enableOmeBxFlag;

    @ApiModelProperty(value = "是否存在关联的报修单")
    private Boolean ifExistFaultRepair;

    @ApiModelProperty(value = "故障类型标志")
    private Boolean faultTypeFlag;

    @ApiModelProperty(value = "PM类型标志")
    private Boolean inspectTypeFlag;

    @ApiModelProperty(value = "是否允许上传单据照片")
    private Boolean enableUploadReceiptPic;

    @ApiModelProperty(value = "工行总行编号")
    private String icbcTopBankCode;

    @ApiModelProperty(value = "故障类型")
    private String appTypeFault;

    @ApiModelProperty(value = "PM类型")
    private String appTypeInspect;

    @ApiModelProperty(value = "工行对接设备照片上传最大数量")
    private Integer icbcImageMaxNum;

    @ApiModelProperty(value = "银行联机报修上传单据照片最大数量")
    private Integer uploadReceiptPicMaxNum;

    @ApiModelProperty(value = "行方陪同人员快捷选择数据")
    private List<String> setEScortNameAndPhone;

}
