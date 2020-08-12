package com.zjft.usp.uas.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.uas.user.mapper.UserLogonIdMapper;
import com.zjft.usp.uas.user.model.UserLogonId;
import com.zjft.usp.uas.user.service.UserLogonIdService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author zphu
 * @date 2019/8/13 17:01
 * @Version 1.0
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class UserLogonIdServiceImpl extends ServiceImpl<UserLogonIdMapper, UserLogonId> implements UserLogonIdService {

    @Resource
    private UserLogonIdMapper userLogonIdMapper;

    @Override
    public boolean saveOrUpdate(String logonId, long userId) {
        Assert.notNull(userId, "userId 不能为空");

        UserLogonId userLogonId = userLogonIdMapper.selectOne(new QueryWrapper<UserLogonId>().eq("userid", userId));
        if (StringUtils.isEmpty(logonId) && userLogonId != null) {
            //loginid变成null，删除该记录
            userLogonIdMapper.deleteById(userLogonId);
            return true;
        }
        //查询logonid是否被占用
        UserLogonId userLogonIdUsed = userLogonIdMapper.selectById(logonId);
        if (userLogonIdUsed != null && userLogonIdUsed.getUserId().equals(userId)) {
            throw new AppException("该登录名已经被占用，请重新输入");
        }

        UserLogonId userLogonIdForUpdate = new UserLogonId();
        userLogonIdForUpdate.setLogonId(logonId);
        userLogonIdForUpdate.setUserId(userId);
        if (userLogonId != null) {
            userLogonIdMapper.update(userLogonIdForUpdate, new UpdateWrapper<UserLogonId>().set("logonid", logonId).eq("userid", userId));
            return true;
        }
        //记录不存在，插入记录
        userLogonIdForUpdate.insert();
        return true;
    }
}
