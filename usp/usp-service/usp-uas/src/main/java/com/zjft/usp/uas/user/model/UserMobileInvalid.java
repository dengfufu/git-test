package com.zjft.usp.uas.user.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 手机二次放号管理
 *
 * @Author zphu
 * @Date 2019/8/13 13:44
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("uas_user_mobile_invalid")
public class UserMobileInvalid extends Model<UserMobileInvalid> {

    @TableId("userid")
    /** 用户ID **/
    private Long userId;

    @TableId("mobile")
    /** 手机号 **/
    private String mobile;

    @TableId("voidtime")
    /** 失效时间 **/
    private Date voidTime;

}