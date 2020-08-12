package com.zjft.usp.uas.user.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.util.Date;

/**
 * @description: 用户支付宝账号
 * @author chenxiaod
 * @date 2019/8/8 10:44
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uas_user_alipay")
public class UserAlipay {

    /** 支付宝userid **/
    private String alipayUserId;

    /** 支付宝logonid **/
    private String alipayLogonId;

    /** 用户ID **/
    private Long userId;

    /** 绑定时间 **/
    private Date addTime;
}
