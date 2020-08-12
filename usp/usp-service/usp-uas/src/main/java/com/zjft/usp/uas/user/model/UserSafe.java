package com.zjft.usp.uas.user.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 *个人登录管理
 * @Author zphu
 * @Date 2019/8/13 13:44
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("uas_user_safe")
public class UserSafe extends Model<UserSafe> {

    @TableId("userid")
    /** 用户ID **/
    private Long userId;

    /** 密码 **/
    private String passwd;

    @TableField("logonid")
    /** 登录名 **/
    private String logonId;

    /** 电子邮箱 **/
    private String email;
}