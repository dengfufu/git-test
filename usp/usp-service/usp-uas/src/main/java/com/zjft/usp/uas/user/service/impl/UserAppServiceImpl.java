package com.zjft.usp.uas.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.uas.user.mapper.UserAppMapper;
import com.zjft.usp.uas.user.model.UserApp;
import com.zjft.usp.uas.user.service.UserAppService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zphu
 * @date 2019/8/13 20:32
 * @Version 1.0
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class UserAppServiceImpl extends ServiceImpl<UserAppMapper, UserApp> implements UserAppService {

    @Override
    public void insert(String clientId, Long userId) {
        Assert.notNull(clientId, "clientId 不能为空");
        Assert.notNull(userId, "userId 不能为空");
        UserApp userApp = new UserApp();
        userApp.setClientId(clientId);
        userApp.setUserId(userId);
        userApp.insert();
    }
}
