package com.zjft.usp.uas.user.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 *个人设备管理
 * @Author zphu
 * @Date 2019/8/13 13:44
**/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("uas_user_mobile")
public class UserMobile extends Model<UserMobile> {

    @TableId("mobile")
    /** 手机号 **/
    private String mobile;

    @TableField("userid")
    /** 用户ID **/
    private Long userId;

}