package com.zjft.usp.uas.user.mapper;

import com.zjft.usp.uas.user.model.UserAlipay;

/**
 * @description: 用户支付宝账号mapper接口
 * @author chenxiaod
 * @date 2019/8/8 10:50
 */
public interface UserAlipayMapper {

    /**
     * description: 绑定支付宝
     *
     * @param userAlipay
     * @return void
     */
    void alipayBind(UserAlipay userAlipay);
}
