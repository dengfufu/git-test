package com.zjft.usp.zj.device.atm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备DTO类
 *
 * @author zgpi
 * @date 2020/3/25 10:53
 */
@ApiModel(value = "设备DTO类")
@Data
public class DeviceDto implements Serializable {

    private static final long serialVersionUID = 4827299688849697984L;

    @ApiModelProperty(value = "设备型号")
    private String deviceModel;

    @ApiModelProperty(value = "设备型号名称")
    private String deviceModelName;

    @ApiModelProperty(value = "终端号")
    private String deviceCode;

    @ApiModelProperty(value = "制造号")
    private String serial;

    @ApiModelProperty(value = "设备网点编号")
    private String deviceBranch;

    @ApiModelProperty(value = "保修状态")
    private String warrantyStatus;

    @ApiModelProperty(value = "网点名称")
    private String branchName;

    @ApiModelProperty(value = "客户名称")
    private String bankName;

    @ApiModelProperty(value = "支行名称")
    private String bankSubBranchName;

    @ApiModelProperty(value = "所在城市名称")
    private String cityName;

    @ApiModelProperty(value = "机器状态")
    private String status;

    @ApiModelProperty(value = "PM关注")
    private String pmConcernStatus;

    @ApiModelProperty(value = "网点地址")
    private String branchAddress;

    @ApiModelProperty(value = "联系人员")
    private String contactName;

    @ApiModelProperty(value = "联系电话")
    private String contactPhone;

    @ApiModelProperty(value = "所需时间")
    private String needTime;

    @ApiModelProperty(value = "交通路线")
    private String route;

    @ApiModelProperty(value = "照明灯管情况")
    private String lightingLamp;

    @ApiModelProperty(value = "照明灯管情况备注")
    private String lightingLampNote;

    @ApiModelProperty(value = "设备图片确认情况", notes = "N-未确认，Y-已确认")
    private String photoConfirmed;

    @ApiModelProperty(value = "监控设备品牌")
    private String monitorBrand;

    @ApiModelProperty(value = "装机日期")
    private String installDate;

    @ApiModelProperty(value = "开通日期")
    private String openDate;

    @ApiModelProperty(value = "原厂商到保日期")
    private String origManuArrivalDate;

    @ApiModelProperty(value = "委保到期日期")
    private String warrantyExpireDate;

    @ApiModelProperty(value = "实际厂商到保日期")
    private String actualManuArrivalDate;

    @ApiModelProperty(value = "采购年度")
    private String purchaseYear;

    @ApiModelProperty(value = "纸币环流功能状态")
    private String moneyCirculate;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "设备预警信息")
    private EarlyWarnInfoDto earlyWarnInfoDto;

    @ApiModelProperty(value = "是否备件全保")
    private String isPartGuaranteed;

}
