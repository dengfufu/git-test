package com.zjft.usp.uas.user.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

/**
 *
 *个人实名类
 * @Author zphu
 * @Date 2019/8/13 13:44
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("uas_user_real")
public class UserReal extends Model<UserReal> {

    @TableId("userid")
    /** 用户ID **/
    private Long userId;

    @NotEmpty(message = "username 不能为空")
    @TableField("username")
    /** 姓名 **/
    private String userName;

    @NotEmpty(message = "idcard 不能为空")
    @TableField("idcard")
    /** 身份证号 **/
    private String idCard;

    @TableField("verified")
    private Integer verified;
}