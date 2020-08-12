package com.zjft.usp.anyfix.work.remind.dto;

import com.zjft.usp.anyfix.work.remind.model.WorkRemindMain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 工单预警主表Dto
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-04-23 21:14
 **/
@Data
public class WorkRemindMainDto extends WorkRemindMain {
    @ApiModelProperty(value = "企业名称")
    private String corpName;

    @ApiModelProperty(value = "客户名称")
    private String customName;

    @ApiModelProperty(value = "设备客户名称")
    private String customCorpName;

    @ApiModelProperty(value = "委托商企业名称")
    private String demanderCorpName;

    @ApiModelProperty(value = "服务商企业名称")
    private String serviceCorpName;

    @ApiModelProperty(value = "工单类型名称")
    private String workTypeName;

    @ApiModelProperty(value = "设备大类名称")
    private String largeClassName;

    @ApiModelProperty(value = "设备小类名称")
    private String smallClassName;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "型号名称")
    private String modelName;

    @ApiModelProperty(value = "省名称")
    private String provinceName;

    @ApiModelProperty(value = "市名称")
    private String cityName;

    @ApiModelProperty(value = "行政区划")
    private String districtName;

    @ApiModelProperty(value = "设备网点名称")
    private String deviceBranchName;

    @ApiModelProperty(value = "服务网点名称")
    private String serviceBranchName;

    @ApiModelProperty(value = "创建人姓名")
    private String creatorName;

    @ApiModelProperty(value = "预警类型")
    private Integer remindType;

    @ApiModelProperty(value = "预警类型名称")
    private String remindTypeName;

    @ApiModelProperty(value = "超时时间")
    private Integer expireTimeMin;

    private List<WorkRemindDetailDto> workRemindDetailDtoList;
}
