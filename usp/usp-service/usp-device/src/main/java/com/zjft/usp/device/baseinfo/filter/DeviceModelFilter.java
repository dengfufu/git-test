package com.zjft.usp.device.baseinfo.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 设备型号filter
 *
 * @author canlei
 * @version 1.0
 * @date 2019-11-08 10:45
 **/
@ApiModel(value = "设备型号filter")
@Getter
@Setter
public class DeviceModelFilter extends Page {

    @ApiModelProperty(value = "企业编号")
    private Long corp;

    @ApiModelProperty(value = "用于查询委托商编号")
    private Long corpIdForDemander;

    @ApiModelProperty(value = "设备大类编号")
    private Long largeClassId;

    @ApiModelProperty(value = "设备小类编号")
    private Long smallClassId;

    @ApiModelProperty(value = "设备小类编号列表")
    private List<Long> smallClassIdList;

    @ApiModelProperty(value = "设备品牌编号")
    private Long brandId;

    @ApiModelProperty(value = "设备品牌编号列表")
    private List<Long> brandIdList;

    @ApiModelProperty(value = "型号名称")
    private String name;

    @ApiModelProperty(value = "是否可用")
    private String enabled;

    @ApiModelProperty(value = "模糊查询")
    private String matchFilter;

    @ApiModelProperty(value = "委托商列表")
    private List<Long> demanderCorpList;

}
