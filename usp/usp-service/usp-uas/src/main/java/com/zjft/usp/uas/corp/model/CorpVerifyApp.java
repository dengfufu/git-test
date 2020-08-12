package com.zjft.usp.uas.corp.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author canlei
 * @date 2019-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uas_corp_verify_app")
@ApiModel("企业认证申请")
public class CorpVerifyApp extends Model<CorpVerifyApp> {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "corpid")
    @ApiModelProperty("企业编号")
    private Long corpId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "txid")
    @ApiModelProperty("交易编号")
    private Long txId;

    @ApiModelProperty("审核状态")
    private Short status;

    @ApiModelProperty("法人代表身份证号")
    private String larIdCard;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("营业执照文件编号")
    private Long licenseFileId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("申请人身份证照片文件编号")
    private Long applyIdCardImg;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("验证视频文件编号")
    private Long applyVideo;

    @ApiModelProperty("视频验证码")
    private String videoCheckCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("申请用户编号")
    private Long applyUserId;

    @ApiModelProperty("申请时间")
    private Timestamp applyTime;

    @ApiModelProperty("申请人身份证号")
    private String applyIdCardNum;

    @ApiModelProperty("统一社会信用代码")
    private String creditCode;

    @ApiModelProperty("法人代表姓名")
    private String larName;

    @ApiModelProperty("成立日期")
    private Date foundDate;

    @ApiModelProperty("有效日期")
    private Date expireDate;

    @ApiModelProperty("省编号")
    private String province;

    @ApiModelProperty("市编号")
    private String city;

    @ApiModelProperty("详细地址")
    private String address;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("审核用户编号")
    private Long checkUserId;

    @ApiModelProperty("审核时间")
    private Timestamp checkTime;

    @ApiModelProperty("审核备注")
    private String checkNote;

}