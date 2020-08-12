package com.zjft.usp.uas.user.service;

import com.zjft.usp.uas.user.model.UserAlipay;

/**
 * @description: TODO
 * @author chenxiaod
 * @date 2019/8/8 11:09
 */
public interface UserAlipayService {

    /**
     * description: 绑定支付宝
     *
     * @param userAlipay
     * @return void
     */
    void alipayBind(UserAlipay userAlipay);
}