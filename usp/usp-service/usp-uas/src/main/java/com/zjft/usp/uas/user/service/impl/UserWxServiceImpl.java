package com.zjft.usp.uas.user.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.uas.user.mapper.UserWxMapper;
import com.zjft.usp.uas.user.model.UserMobile;
import com.zjft.usp.uas.user.model.UserWx;
import com.zjft.usp.uas.user.service.UserMobileService;
import com.zjft.usp.uas.user.service.UserRegisterService;
import com.zjft.usp.uas.user.service.UserWxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenxiaod
 * @description: 微信账号服务实现类
 * @date 2019/8/8 17:18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserWxServiceImpl extends ServiceImpl<UserWxMapper, UserWx> implements UserWxService {

    @Resource
    private UserWxMapper userWxMapper;

    @Resource
    private UserMobileService userMobileService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void wxpayBind(UserWx userWx) {
        userWxMapper.wxpayBind(userWx);
    }

    @Override
    public void insert(String openId, Long userId) {
        Assert.notNull(userId, "userId 不能为空");
        Assert.notEmpty(openId, "openId 不能为空");
        UserWx userWx = new UserWx();
        userWx.setOpenId(openId);
        userWx.setUserId(userId);
        userWx.setAddTime(DateUtil.parse(DateUtil.now()));
        userWxMapper.insert(userWx);
    }


    /**
     * 根据用户编号获取openid
     * @param userId
     * @return
     */
    @Override
    public String getOpenidByUserid(Long userId) {
        UserWx userWx = this.getUserWxByUserid(userId);
        if (userWx == null) {
            return null;
        } else {
            return userWx.getOpenId();
        }
    }

    /**
     * 根据用户编号获得微信信息
     * @param userId
     * @return
     */
    public UserWx getUserWxByUserid(Long userId) {
        QueryWrapper<UserWx> queryWrapper = new QueryWrapper<>();
        if (LongUtil.isNotZero(userId)) {
            queryWrapper.eq("userid",userId);
        }
        List<UserWx> list = userWxMapper.selectList(queryWrapper);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    /**
     * 电话号码是否已经存在绑定的微信账号
     * @param mobile
     * @return
     */
    @Override
    public Boolean checkIsBindWx(String mobile) {
        Assert.notEmpty(mobile, "手机号不能为空");
        UserMobile userMobile = userMobileService.getById(mobile);
        UserWx userWx = this.getUserWxByUserid(userMobile.getUserId());
        return userWx != null;
    }

    @Override
    public void delByUserId(Long userId) {
        UserWx userWx = this.getUserWxByUserid(userId);
        if (userWx != null) {
            userWxMapper.deleteById(userWx.getOpenId());
        }
    }

}
