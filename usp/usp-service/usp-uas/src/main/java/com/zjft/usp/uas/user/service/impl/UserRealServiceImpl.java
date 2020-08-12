package com.zjft.usp.uas.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.uas.user.dto.UserRealDto;
import com.zjft.usp.uas.user.mapper.UserRealMapper;
import com.zjft.usp.uas.user.model.UserMobile;
import com.zjft.usp.uas.user.model.UserReal;
import com.zjft.usp.uas.user.service.UserMobileService;
import com.zjft.usp.uas.user.service.UserRealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zphu
 * @date 2019/8/13 16:25
 * @Version 1.0
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class UserRealServiceImpl extends ServiceImpl<UserRealMapper, UserReal> implements UserRealService {

    @Autowired
    private UserMobileService userMobileService;

    @Override
    public boolean save(UserRealDto userRealDto, Long userId) {
        Assert.notNull(userRealDto, "UserRealDto 不能为null");
        Assert.notNull(userId, "userId 不能为空");
        UserReal userReal = new UserReal();
        userReal.setUserId(userId);
        userReal.setUserName(userRealDto.getUserName());
        userReal.setIdCard(userRealDto.getIdCard());

        return this.saveOrUpdate(userReal);
    }

    @Override
    public Map<Long, String> mapIdAndName(List<Long> userIdList) {
        Map<Long, String> mapIdAndName = new HashMap<>();
        if (userIdList != null && userIdList.size() > 0) {
            List<UserReal> userRealList = this.list(new QueryWrapper<UserReal>().in("userid",userIdList));
            if(userRealList != null){
                for(UserReal userReal : userRealList){
                    mapIdAndName.put(userReal.getUserId(),userReal.getUserName());
                }
            }
        }
        return mapIdAndName;
    }

    @Override
    public UserRealDto findUserRealDtoById(Long userId) {
        UserRealDto userRealDto = new UserRealDto();
        UserReal userReal = this.getById(userId);
        if(userReal != null){
            BeanUtils.copyProperties(userReal, userRealDto);
        }
        return userRealDto;
    }

    /**
     * 删除人员认证信息
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2019/11/28 10:37
     **/
    @Override
    public void delUserReal(Long userId) {
        this.removeById(userId);
    }

    /**
     * 身份证实名认证
     *
     * @param userId
     * @return
     * @throws
     * @author zphu
     * @date 2019/8/29 8:48
     **/
    @Override
    public UserRealDto findUserReal(Long userId) {
        UserRealDto realnameInfoDto = new UserRealDto();
        UserMobile userMobile = userMobileService.getOne(new QueryWrapper<UserMobile>().eq("userid", userId), true);
        UserReal userReal = this.getById(userId);
        if (userMobile != null && StrUtil.isNotBlank(userMobile.getMobile())) {
            if (userMobile.getMobile().length() > 7) {
                //遮盖部分字符
                String mobile = StrUtil.replace(userMobile.getMobile(), userMobile.getMobile().substring(3, 7), "****");
                realnameInfoDto.setMobile(mobile);
            }
        }
        if (userReal != null && StrUtil.isNotBlank(userReal.getIdCard())) {
            if (userReal.getIdCard().length() > 12) {
                String idCard = StrUtil.replace(userReal.getIdCard(), userReal.getIdCard().substring(5, 12), "*******");
                realnameInfoDto.setIdCard(idCard);
                realnameInfoDto.setUserName(userReal.getUserName());
            }
        }
        return realnameInfoDto;
    }
}
