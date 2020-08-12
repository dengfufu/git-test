package com.zjft.usp.uas.user.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @description: 用户微信账号
 * @author chenxiaod
 * @date 2019/8/8 17:08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uas_user_wx")
public class UserWx {

    /** 微信openid **/
    @TableId("openid")
    private String openId;

    /** 微信unionid **/
    private String unionId;

    /** 用户ID **/
    private Long userId;

    /** 绑定时间 **/
    private Date addTime;
}
