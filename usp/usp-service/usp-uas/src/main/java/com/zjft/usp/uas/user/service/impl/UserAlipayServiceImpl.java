package com.zjft.usp.uas.user.service.impl;

import com.zjft.usp.uas.user.mapper.UserAlipayMapper;
import com.zjft.usp.uas.user.model.UserAlipay;
import com.zjft.usp.uas.user.service.UserAlipayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @description: TODO
 * @author chenxiaod
 * @date 2019/8/8 11:11
 */
@Service
public class UserAlipayServiceImpl implements UserAlipayService {

    @Resource
    private UserAlipayMapper userAlipayMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void alipayBind(UserAlipay userAlipay) {
        userAlipayMapper.alipayBind(userAlipay);
    }
}
