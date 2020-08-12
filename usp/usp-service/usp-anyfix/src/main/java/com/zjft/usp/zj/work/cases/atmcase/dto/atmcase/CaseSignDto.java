package com.zjft.usp.zj.work.cases.atmcase.dto.atmcase;

import com.zjft.usp.zj.work.cases.atmcase.dto.file.ImageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: JFZOU
 * @Date: 2020-03-19 19:45
 * @Version 1.0
 */
@ApiModel(value = "ATM机CASE签到页面对象类")
@Data
public class CaseSignDto {
    @ApiModelProperty(value = "工单编号")
    private String workCode;
    @ApiModelProperty(value = "工单类型")
    private String workTypeName;
    @ApiModelProperty(value = "设备网点名称")
    private String deviceBranchName;
    @ApiModelProperty(value = "产品品牌")
    private String brandName;
    @ApiModelProperty(value = "产品型号")
    private String modelName;
    @ApiModelProperty(value = "客户名称")
    private String customName;
    @ApiModelProperty(value = "服务请求")
    private String serviceRequest;
    @ApiModelProperty(value = "预约时间")
    private String bookTimeBegin;
    @ApiModelProperty(value = "创建时间")
    private String sysCreateTime;
    @ApiModelProperty(value = "出发时间")
    private String goTime;
    @ApiModelProperty(value = "服务站")
    private String serviceBranchName;
    @ApiModelProperty(value = "工程师姓名")
    private String engineerNames;
    @ApiModelProperty(value = "行方陪同人员统一认证号")
    private String escort;
    @ApiModelProperty(value = "实际签到时间")
    private String signTime;
    @ApiModelProperty(value = "人脸签到照片，实际只有一张")
    private String images;
    @ApiModelProperty(value = "BASE64格式签名图片")
    private String signatureBase64;
    @ApiModelProperty(value = "人脸签到开关 Y=打开")
    private String faceIdentFlag;
    @ApiModelProperty(value = "行方陪同人员开关 Y=打开")
    private String signEscortFlag;

    @ApiModelProperty(value = "图片信息列表")
    private List<ImageDto> imageList;

    @ApiModelProperty(value = "当前时间")
    private String currentTime;
}
