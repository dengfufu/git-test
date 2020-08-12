package com.zjft.usp.zj.work.cases.atmcase.dto.atmcase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 关闭CASEDto
 *
 * @author canlei
 * @version 1.0
 * @date 2020-03-26 09:01
 **/
@ApiModel(value = "关闭CASEDto")
@Data
public class CaseFinishDto implements Serializable {

    private static final long serialVersionUID = -5437140862897095989L;

    @ApiModelProperty(value = "CASE编号")
    private String workCode;

    @ApiModelProperty(value = "CASE类型，只有大类，不包括子类型")
    private String workTypeName;

    @ApiModelProperty(value = "CASE子类编号")
    private Integer workSubType;

    @ApiModelProperty(value = "CASE类型,包括子类型")
    private String workTypeAllName;

    @ApiModelProperty(value = "设备网点名称")
    private String deviceBranchName;

    @ApiModelProperty(value = "机器型号编号")
    private String modelId;

    @ApiModelProperty(value = "机器型号")
    private String modelName;

    @ApiModelProperty(value = "服务请求")
    private String serviceRequest;

    @ApiModelProperty(value = "交通工具编号")
    private String traffic;

    @ApiModelProperty(value = "交通工具")
    private String trafficName;

    @ApiModelProperty(value = "交通工具说明")
    private String trafficNote;

    @ApiModelProperty(value = "出发时间")
    private String goTime;

    @ApiModelProperty(value = "行方陪同人员统一认证号")
    private String escort;

    @ApiModelProperty(value = "行方陪同人员姓名")
    private String escortName;

    @ApiModelProperty(value = "行方陪同人员电话")
    private String escortPhone;

    @ApiModelProperty(value = "工单状态名称")
    private String workStatusName;

    @ApiModelProperty(value = "服务方式名称")
    private String serviceModeName;

    @ApiModelProperty(value = "客户编号，即银行编号")
    private String customId;

    @ApiModelProperty(value = "客户名称，即银行名称")
    private String customName;

    @ApiModelProperty(value = "设备网点")
    private String deviceBranch;

    @ApiModelProperty(value = "服务商网点编号")
    private String serviceBranch;

    @ApiModelProperty(value = "CASE所属服务站编号")
    private String yoCodeDepot;

    @ApiModelProperty(value = "服务商网点名称")
    private String serviceBranchName;

    @ApiModelProperty(value = "预约时间")
    private String bookTime;

    @ApiModelProperty(value = "设备编号")
    private String deviceCode;

    @ApiModelProperty(value = "出厂序列号")
    private String serial;

    @ApiModelProperty(value = "设备品牌名称")
    private String brandName;

    @ApiModelProperty(value = "保修状态名称")
    private String warrantyName;

    @ApiModelProperty(value = "软件版本")
    private String softVersion;

    @ApiModelProperty(value = "SP软件版本")
    private String spSoftVersion;

    @ApiModelProperty(value = "BV软件版本")
    private String bvSoftVersion;

    @ApiModelProperty(value = "其他软件版本")
    private String otherSoftVersion;

    @ApiModelProperty(value = "故障时间")
    private String faultTime;

    @ApiModelProperty(value = "报修时间")
    private String reportTime;

    @ApiModelProperty(value = "故障代码")
    private String faultCode;

    @ApiModelProperty(value = "故障模块")
    private String faultModuleId;

    @ApiModelProperty(value = "经度")
    private BigDecimal lon;

    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;

    @ApiModelProperty(value = "预计出发时间")
    private String planGoTime;

    @ApiModelProperty(value = "预计到达时间")
    private String planArriveTime;

    @ApiModelProperty(value = "签到时间")
    private String signTime;

    @ApiModelProperty(value = "完成描述")
    private String finishDescription;

    @ApiModelProperty(value = "预计完成时间，可修改")
    private String planEndTime;

    @ApiModelProperty(value = "服务工程师ID拼接，以英文逗号分隔")
    private String engineers;

    @ApiModelProperty(value = "服务工程师姓名拼接，以英文逗号分隔")
    private String engineerNames;

    @ApiModelProperty(value = "服务开始时间")
    private String startTime;

    @ApiModelProperty(value = "服务结束时间，用户输入的服务完成时间，可以修改")
    private String endTime;

    @ApiModelProperty(value = "要求跟单")
    private String traceRequired;

    @ApiModelProperty(value = "跟单分类")
    private String traceRule;

    @ApiModelProperty(value = "其他跟单分类")
    private String otherTraceRule;

    @ApiModelProperty(value = "要求巡检")
    private String inspectionRequired;

    @ApiModelProperty(value = "修改时间")
    private String modTime;

    @ApiModelProperty(value = "修改说明")
    private String modNote;

    @ApiModelProperty(value = "是否手机关闭")
    private String mobileFinish;

    @ApiModelProperty(value = "是否手机建单")
    private String isMobile;

    @ApiModelProperty(value = "纸币环流状态")
    private String zbhlKt;

    @ApiModelProperty(value = "是否有备件更换")
    private String isPartReplace;

    @ApiModelProperty(value = "处理结果编号")
    private String dealResultId;

    @ApiModelProperty(value = "处理结果")
    private String dealResultName;

    @ApiModelProperty(value = "新终端编号")
    private String newDeviceCode;

    @ApiModelProperty(value = "是否认为损坏")
    private String manMade;

    @ApiModelProperty(value = "PM处理方式")
    private String dealWay;

    @ApiModelProperty(value = "银行是否要求上传设备照片")
    private String needIcbcImage;

    @ApiModelProperty(value = "新机器型号")
    private String newModelId;

    @ApiModelProperty(value = "新制造号")
    private String newSerial;

    @ApiModelProperty(value = "未关联工单说明")
    private String unRelatedNote;

    @ApiModelProperty(value = "关闭说明")
    private String finishNote;

    @ApiModelProperty(value = "是否要求上传单据照片")
    private String needReceiptPic;

}
