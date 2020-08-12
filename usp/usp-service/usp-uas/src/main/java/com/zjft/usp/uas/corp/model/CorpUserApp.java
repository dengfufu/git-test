package com.zjft.usp.uas.corp.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author canlei
 * @date 2019-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uas_corp_apply")
@ApiModel("加入企业申请")
public class CorpUserApp extends Model<CorpUserApp> implements Serializable {

    @TableId(value = "corpid")
    @ApiModelProperty("企业编号")
    private Long corpId;

    @TableId(value = "txid")
    @ApiModelProperty("交易编号")
    private Long txId;

    @ApiModelProperty("申请用户编号")
    private Long applyUserId;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("审核状态")
    private int status;

    @ApiModelProperty("验证信息")
    private String applyNote;

    @ApiModelProperty("企业员工账号")
    private String account;

    @ApiModelProperty("申请时间")
    private Date applyTime;

    @ApiModelProperty("邀请用户编号")
    private Long inviteUserId;

    @ApiModelProperty("审核用户编号")
    private Long checkUserId;

    @ApiModelProperty("审核时间")
    private Date checkTime;

    @ApiModelProperty("审核备注")
    private String checkNote;

}