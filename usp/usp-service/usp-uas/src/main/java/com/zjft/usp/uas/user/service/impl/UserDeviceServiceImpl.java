package com.zjft.usp.uas.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.user.mapper.UserDeviceMapper;
import com.zjft.usp.uas.user.model.UserDevice;
import com.zjft.usp.uas.user.service.UserDeviceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zphu
 * @Description
 * @date 2019/8/9 8:57
 * @Version 1.0
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class UserDeviceServiceImpl extends ServiceImpl<UserDeviceMapper, UserDevice> implements UserDeviceService {

    @Override
    public void addUserDeviceInfo(ReqParam reqParam, Long userId) {
        Assert.notNull(reqParam, "公共参数不能为空");
        Assert.notNull(userId, "用户编号不能为空");

        this.remove(new UpdateWrapper<UserDevice>().eq("userid", userId)
                .eq("deviceid", reqParam.getDeviceId()));

        UserDevice userDevice = new UserDevice();
        userDevice.setUserId(userId);
        if (StrUtil.isNotBlank(reqParam.getDeviceId())) {
            userDevice.setDeviceId(StrUtil.trimToEmpty(reqParam.getDeviceId()));
        }
        if (IntUtil.isNotZero(reqParam.getDeviceType())) {
            userDevice.setDeviceType(reqParam.getDeviceType().shortValue());
        }
        userDevice.setOsVersion(StrUtil.sub(reqParam.getOsVersion(), 0, 300));
        this.saveOrUpdate(userDevice);
    }

    /**
     * 是否有记录
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2019/11/28 10:31
     **/
    @Override
    public boolean findHasRecord(Long userId) {
        int count = this.count(new QueryWrapper<UserDevice>().eq("userid", userId));
        return count > 0;
    }
}
