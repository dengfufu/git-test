package com.zjft.usp.uas.user.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 用户设备管理类
 *
 * @Author zphu
 * @Date 2019/8/13 13:43
**/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("uas_user_device")
public class UserDevice extends Model<UserDevice> {

    @TableId("userid")
    /** 用户ID **/
    private Long userId;

    @TableId("deviceid")
    /** 设备串号 **/
    private String deviceId;

    @TableField("devicetype")
    /** 设备类型 **/
    private Short deviceType;

    @TableField("osversion")
    /** 操作系统版本 **/
    private String osVersion;

    @TableField("addtime")
    /** 注册时间 **/
    private Date addTime;

}