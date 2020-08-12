package com.zjft.usp.device.device.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.dto.CustomFieldDataDto;
import com.zjft.usp.device.config.DateToLongSerializer;
import com.zjft.usp.device.device.model.DeviceInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 设备档案dto
 *
 * @author zgpi
 * @date 2019/10/16 7:29 下午
 **/
@ApiModel("设备档案dto")
@Getter
@Setter
public class DeviceInfoDto extends DeviceInfo {

    @ApiModelProperty(value = "设备编号")
    private String deviceCode;

    @ApiModelProperty(value = "设备小类名称")
    private String smallClassName;

    @ApiModelProperty(value = "设备规格名称")
    private String specificationName;

    @ApiModelProperty(value = "设备大类编号")
    private Long largeClassId;

    @ApiModelProperty(value = "设备大类名称")
    private String largeClassName;

    @ApiModelProperty(value = "设备品牌名称")
    private String brandName;

    @ApiModelProperty(value = "设备型号名称")
    private String modelName;

    @ApiModelProperty(value = "服务委托方名称")
    private String demanderCorpName;

    @ApiModelProperty(value = "服务商名称")
    private String serviceCorpName;

    @ApiModelProperty(value = "保修状态名称")
    private String warrantyStatusName;

    @ApiModelProperty(value = "维保方式名称")
    private String warrantyModeName;

    @ApiModelProperty(value = "委托方客户关系编号")
    private Long customId;

    @ApiModelProperty(value = "用户企业")
    private Long customCorp;

    @ApiModelProperty(value = "用户企业名称")
    private String customCorpName;

    @ApiModelProperty(value = "行政区划")
    private String district;

    @ApiModelProperty(value = "市郊")
    private Integer zone;

    @ApiModelProperty(value = "市郊名称")
    private String zoneName;

    @ApiModelProperty(value = "行政区划名称")
    private String districtName;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "设备网点")
    private Long branchId;

    @ApiModelProperty(value = "网点名称")
    private String branchName;

    @ApiModelProperty(value = "服务网点")
    private Long serviceBranch;

    @ApiModelProperty(value = "网点名称")
    private String serviceBranchName;

    @ApiModelProperty(value = "设备状态")
    private Integer status;

    @ApiModelProperty(value = "状态名称")
    private String statusName;

    @ApiModelProperty(value = "安装日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date installDate;

    @ApiModelProperty(value = "经度")
    private BigDecimal lon;

    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "派单主管")
    private Long workManager;

    @ApiModelProperty(value = "派单主管姓名")
    private String workManagerName;

    @ApiModelProperty(value = "工程师")
    private Long engineer;

    @ApiModelProperty(value = "工程师姓名")
    private String engineerName;

    @ApiModelProperty(value = "服务说明")
    private String serviceNote;

    @ApiModelProperty(value = "工单ID")
    private Long workId;

    @ApiModelProperty(value = "是否有位置信息")
    private Boolean hasLocate;

    @ApiModelProperty(value = "是否有服务信息")
    private Boolean hasService;

    @ApiModelProperty(value = "自定义字段")
    private List<CustomFieldDataDto> customFieldDataList;

    @ApiModelProperty(value = "设备描述")
    private String deviceDescription;

    @ApiModelProperty(value = "省名称")
    private String provinceName;

    @ApiModelProperty(value = "市名称")
    private String cityName;
}
