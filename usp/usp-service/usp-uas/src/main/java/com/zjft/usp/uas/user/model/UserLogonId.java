package com.zjft.usp.uas.user.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 个人登录信息
 *
 * @Author zphu
 * @Date 2019/8/13 13:43
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("uas_user_logonid")
public class UserLogonId extends Model<UserLogonId> {

    @TableId("logonid")
    /** 登录名 **/
    private String logonId;

    /**
     * 用户ID
     **/
    @TableField("userid")
    private Long userId;


}
