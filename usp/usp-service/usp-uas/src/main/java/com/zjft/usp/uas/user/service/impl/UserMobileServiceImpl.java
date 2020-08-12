package com.zjft.usp.uas.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.uas.user.mapper.UserInfoMapper;
import com.zjft.usp.uas.user.mapper.UserMobileMapper;
import com.zjft.usp.uas.user.model.UserMobile;
import com.zjft.usp.uas.user.service.UserMobileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author zphu
 * @Description
 * @date 2019/8/11 19:09
 * @Version 1.0
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class UserMobileServiceImpl extends ServiceImpl<UserMobileMapper, UserMobile> implements UserMobileService {

    @Resource
    private UserMobileMapper userMobileMapper;
    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public void insert(String mobile, Long userId) {
        Assert.notNull(userId, "userId 不能为空");
        Assert.notEmpty(mobile, "mobile 不能为空");
        //插入用户手机号表
        UserMobile userMobile = new UserMobile();
        userMobile.setMobile(mobile);
        userMobile.setUserId(userId);
        userMobile.insert();
    }

    @Override
    public boolean update(String mobile, Long userId) {
        Assert.notNull(userId, "userId 不能为空");
        Assert.notEmpty(mobile, "mobile 不能为空");

        UserMobile userMobile = userMobileMapper.selectOne(new QueryWrapper<UserMobile>().eq("userid", userId));
        if (userMobile == null) {
            return false;
        }
        userMobile.setMobile(mobile);
        userMobileMapper.update(userMobile, new UpdateWrapper<UserMobile>().eq("userid", userId));
        return true;
    }

    /**
     * 删除记录
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2019/11/28 10:36
     **/
    @Override
    public void delUserMobile(Long userId) {
        this.remove(new UpdateWrapper<UserMobile>().eq("userid", userId));
    }

    @Override
    public boolean isMobileRegister(String mobile) {
        Assert.notEmpty(mobile, "mobile 不能为空");
        UserMobile userMobile = userMobileMapper.selectById(mobile);
        return userMobile != null ? true : false;
    }
}
