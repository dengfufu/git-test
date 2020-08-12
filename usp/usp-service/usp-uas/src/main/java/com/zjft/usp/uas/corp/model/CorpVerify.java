package com.zjft.usp.uas.corp.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author canlei
 * @date 2019-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uas_corp_verify")
@ApiModel("企业认证")
public class CorpVerify extends Model<CorpVerify> {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "corpid")
    @ApiModelProperty("企业编号")
    private Long corpId;

    @ApiModelProperty("法人代表身份证号")
    private String larIdCard;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("营业执照文件编号")
    private Long licenseFileId;

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
}