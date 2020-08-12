package com.zjft.usp.uas.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.uas.user.mapper.UserMobileInvalidMapper;
import com.zjft.usp.uas.user.mapper.UserMobileMapper;
import com.zjft.usp.uas.user.model.UserMobile;
import com.zjft.usp.uas.user.model.UserMobileInvalid;
import com.zjft.usp.uas.user.service.UserMobileInvalidService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author zphu
 * @date 2019/8/13 19:48
 * @Version 1.0
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class UserMobileInvalidServiceImpl extends ServiceImpl<UserMobileInvalidMapper, UserMobileInvalid> implements UserMobileInvalidService {

    @Resource
    private UserMobileMapper userMobileMapper;

    @Override
    public void insert(String mobile) {
        Assert.notEmpty(mobile, "mobile 不能为空");
        UserMobileInvalid userMobileInvalid = new UserMobileInvalid();
        UserMobile userMobile = userMobileMapper.selectOne(new QueryWrapper<UserMobile>().eq("mobile", mobile));
        if (userMobile != null) {
            userMobileInvalid.setUserId(userMobile.getUserId());
            userMobileInvalid.setMobile(mobile);
            userMobileInvalid.insert();
        }
    }
}
