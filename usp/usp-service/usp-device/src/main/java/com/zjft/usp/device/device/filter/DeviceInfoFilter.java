package com.zjft.usp.device.device.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 设备filter
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/10/16 6:47 下午
 **/
@ApiModel("设备filter")
@Getter
@Setter
public class DeviceInfoFilter extends Page {

    @ApiModelProperty(value = "当前企业编号")
    private Long curCorpId;

    @ApiModelProperty(value = "设备小类ID")
    private Long smallClassId;

    @ApiModelProperty(value = "设备大类ID")
    private Long largeClassId;

    @ApiModelProperty(value = "设备品牌ID")
    private Long brandId;

    @ApiModelProperty(value = "设备型号ID")
    private Long modelId;

    @ApiModelProperty(value = "出厂序列号")
    private String serial;

    @ApiModelProperty(value = "自定义设备编号")
    private String deviceCode;

    @ApiModelProperty(value = "需求服务方")
    private Long demanderCorp;

    @ApiModelProperty(value = "服务商")
    private Long serviceCorp;

    @ApiModelProperty(value = "出厂开始日期")
    private Date factoryStartDate;

    @ApiModelProperty(value = "出厂结束日期")
    private Date factoryEndDate;

    @ApiModelProperty(value = "购买开始日期")
    private Date purchaseStartDate;

    @ApiModelProperty(value = "购买结束日期")
    private Date purchaseEndDate;

    @ApiModelProperty(value = "保修开始日期")
    private Date warrantyStartDate;

    @ApiModelProperty(value = "保修结束日期")
    private Date warrantyEndDate;

    @ApiModelProperty(value = "保修状态", notes = "1=保内，2=保外")
    private Integer warrantyStatus;

    @ApiModelProperty(value = "供应商客户关系编号")
    private Long customId;

    @ApiModelProperty(value = "供应商客户关系编号list")
    private List<Long> customIdList;

    @ApiModelProperty(value = "客户企业")
    private Long customCorp;

    @ApiModelProperty(value = "客户企业名称")
    private String customCorpName;

    @ApiModelProperty(value = "行政区划")
    private String district;

    @ApiModelProperty(value = "设备网点")
    private Long branchId;

    @ApiModelProperty(value = "设备状态，1=运行 2=暂停 3=死亡")
    private Integer status;

    @ApiModelProperty(value = "安装开始日期")
    private Date installStartDate;

    @ApiModelProperty(value = "安装结束日期")
    private Date installEndDate;

    @ApiModelProperty(value = "服务主管")
    private Long workManager;

    @ApiModelProperty(value = "服务工程师")
    private Long engineer;

    @ApiModelProperty(value = "工单ID")
    private Long workId;

    @ApiModelProperty(value = "移动端公共查询条件,可能是品牌名称、型号名称、设备小类名称、序列号")
    private String mobileFilter;

    @ApiModelProperty(value = "保修到期日开始时间")
    private Date warrantyEndDateStart;

    @ApiModelProperty(value = "保修到期日结束时间")
    private Date warrantyEndDateEnd;
}
