package com.zjft.usp.uas.corp.model;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @author: CK
 * @create: 2020-02-28 14:55
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uas_corp_user_trace")
@ApiModel("企业用户变化表")
public class CorpUserTrace extends Model<CorpUser> implements Serializable {

    @TableField(value = "user_id")
    @ApiModelProperty("用户编号")
    private Long userId;

    @TableField(value = "corp_id")
    @ApiModelProperty("企业编号")
    private Long corpId;

    @TableField(value = "operate")
    @ApiModelProperty("操作: 1-加入 2-离开")
    private int operate;

    @TableField(value = "operator")
    @ApiModelProperty("操作人,userId")
    private Long operator;

    @TableField("client_id")
    @ApiModelProperty("操作客户端")
    private String clientId;

    @TableField("operate_time")
    @ApiModelProperty("操作时间")
    private Date operateTime;
}