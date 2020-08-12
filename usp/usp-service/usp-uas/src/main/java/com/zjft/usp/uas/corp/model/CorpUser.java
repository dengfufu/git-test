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
@TableName("uas_corp_user")
@ApiModel("企业用户表")
public class CorpUser extends Model<CorpUser> implements Serializable {

    @TableId(value = "userid")
    @ApiModelProperty("用户编号")
    private Long userId;

    @TableId(value = "corpid")
    @ApiModelProperty("企业编号")
    private Long corpId;

    @ApiModelProperty("是否隐藏(0=显示，1=隐藏)")
    private Long hidden;

    @ApiModelProperty("企业员工账号")
    private String account;

    @ApiModelProperty("添加时间")
    private Date addTime;

}