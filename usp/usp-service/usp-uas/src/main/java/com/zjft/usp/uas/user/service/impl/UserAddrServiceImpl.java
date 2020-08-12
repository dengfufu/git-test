package com.zjft.usp.uas.user.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.uas.baseinfo.service.CfgAreaService;
import com.zjft.usp.uas.user.dto.UserAddrDto;
import com.zjft.usp.uas.user.mapper.UserAddrMapper;
import com.zjft.usp.uas.user.model.UserAddr;
import com.zjft.usp.uas.user.service.UserAddrService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户地址服务实现类
 *
 * @author zgpi
 * @version 1.0
 * @date 2019-08-20 10:39
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class UserAddrServiceImpl extends ServiceImpl<UserAddrMapper, UserAddr> implements UserAddrService {

    @Resource
    private UserAddrMapper userAddrMapper;
    @Autowired
    private CfgAreaService cfgAreaService;

    @Override
    public List<UserAddrDto> listUserAddr(long userId) {
        List<UserAddr> userAddrList = userAddrMapper.listUserAddr(userId);
        Map<String, String> areaMap = cfgAreaService.selectAreaMap();
        List<UserAddrDto> list = new ArrayList<>();
        UserAddrDto userAddrDto;
        for (UserAddr userAddr : userAddrList) {
            userAddrDto = this.transUserAddr(userAddr, areaMap);
            list.add(userAddrDto);
        }
        return list;
    }

    @Override
    public UserAddrDto findUserAddr(long addrId) {
        UserAddr userAddr = userAddrMapper.findUserAddr(addrId);
        Map<String, String> areaMap = cfgAreaService.selectAreaMap();
        UserAddrDto userAddrDto = this.transUserAddr(userAddr, areaMap);
        return userAddrDto;
    }

    @Override
    public void addUserAddr(UserAddrDto dto) {
        UserAddr userAddr = this.transUserAddrDto(dto);
        userAddr.setAddrId(KeyUtil.getId());
        userAddr.setAddTime(DateUtil.parse(DateUtil.now()));
        userAddrMapper.addUserAddr(userAddr);
        if (userAddr.getIsDefault().equalsIgnoreCase("1")) {
            userAddrMapper.updateIsNotDefault(userAddr.getAddrId(), userAddr.getUserId());
        }
    }

    @Override
    public void updateUserAddr(UserAddrDto dto) {
        UserAddr userAddr = this.transUserAddrDto(dto);
        userAddrMapper.updateUserAddr(userAddr);
        if (userAddr.getIsDefault().equalsIgnoreCase("1")) {
            userAddrMapper.updateIsNotDefault(userAddr.getAddrId(), userAddr.getUserId());
        }
    }

    @Override
    public void deleteUserAddr(Long addrId) {
        userAddrMapper.deleteUserAddr(addrId);
    }

    private UserAddrDto transUserAddr(UserAddr userAddr, Map<String, String> areaMap) {
        UserAddrDto userAddrDto = new UserAddrDto();
        BeanUtils.copyProperties(userAddr, userAddrDto);
        userAddrDto.setAddrIdStr(String.valueOf(userAddrDto.getAddrId()));
        String provinceName = StrUtil.trimToEmpty(areaMap.get(userAddr.getProvince()));
        userAddrDto.setProvinceName(provinceName);
        String cityName = StrUtil.trimToEmpty(areaMap.get(userAddr.getCity()));
        userAddrDto.setCityName(cityName);
        String districtName = StrUtil.trimToEmpty(areaMap.get(userAddr.getDistrict()));
        userAddrDto.setDistrictName(districtName);
        return userAddrDto;
    }

    private UserAddr transUserAddrDto(UserAddrDto userAddrDto) {
        UserAddr userAddr = new UserAddr();
        BeanUtils.copyProperties(userAddrDto, userAddr);
        return userAddr;
    }
}
