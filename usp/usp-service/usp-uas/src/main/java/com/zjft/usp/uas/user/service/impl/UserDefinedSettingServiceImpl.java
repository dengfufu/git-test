package com.zjft.usp.uas.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.uas.user.dto.UserDefinedSettingDto;
import com.zjft.usp.uas.user.mapper.UserDefinedSettingMapper;
import com.zjft.usp.uas.user.model.UserDefinedSetting;
import com.zjft.usp.uas.user.service.UserDefinedSettingService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户自定义配置 服务实现类
 * </p>
 *
 * @author minji
 * @since 2020-05-13
 */
@Service
public class UserDefinedSettingServiceImpl extends ServiceImpl<UserDefinedSettingMapper, UserDefinedSetting> implements UserDefinedSettingService {

//    @Resource
//    private UserDefinedSettingMapper userDefinedSettingMapper;

    @Override public List<UserDefinedSettingDto> listUserDefinedSetting(Long userId) {
        Assert.notNull(userId, "用户编号不能为空");
        List<UserDefinedSetting> list = this.list(new QueryWrapper<UserDefinedSetting>().eq("userid", userId));
        List<UserDefinedSettingDto> retList = list.stream().map(i -> {
            UserDefinedSettingDto userDefinedSettingDto = new UserDefinedSettingDto();
            BeanUtils.copyProperties(i, userDefinedSettingDto);
            return userDefinedSettingDto;
        }).collect(Collectors.toList());
        return retList;
    }

    @Override public void merge(UserDefinedSettingDto userDefinedSettingDto) {
        Assert.notNull(userDefinedSettingDto.getUserid(), "用户编号不能为空");
        Assert.notNull(userDefinedSettingDto.getSettingKey(), "自定义配置Key不能为空");
        UserDefinedSetting userDefinedSetting = new UserDefinedSetting();
        BeanUtils.copyProperties(userDefinedSettingDto, userDefinedSetting);
        this.baseMapper.merge(userDefinedSetting);
    }
}
