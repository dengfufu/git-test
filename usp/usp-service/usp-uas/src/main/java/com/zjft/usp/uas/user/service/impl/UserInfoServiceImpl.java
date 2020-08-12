package com.zjft.usp.uas.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.utils.RandomUtil;
import com.zjft.usp.file.service.FileFeignService;
import com.zjft.usp.redis.template.RedisRepository;
import com.zjft.usp.uas.baseinfo.model.CfgArea;
import com.zjft.usp.uas.baseinfo.service.CfgAreaService;
import com.zjft.usp.uas.corp.dto.CorpUserDto;
import com.zjft.usp.uas.user.dto.UserInfoDto;
import com.zjft.usp.uas.user.dto.UserRegionDto;
import com.zjft.usp.uas.user.filter.UserFilter;
import com.zjft.usp.uas.user.filter.UserInfoFilter;
import com.zjft.usp.uas.user.mapper.UserAddrMapper;
import com.zjft.usp.uas.user.mapper.UserInfoMapper;
import com.zjft.usp.uas.user.model.UserAddr;
import com.zjft.usp.uas.user.model.UserReal;
import com.zjft.usp.uas.user.service.UserInfoService;
import com.zjft.usp.uas.user.service.UserRealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zphu
 * @date 2019/8/6 8:59
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;
    @Autowired
    private FileFeignService fileFeignService;
    @Resource
    private UserAddrMapper userAddrMapper;
    @Autowired
    private CfgAreaService cfgAreaService;
    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private UserRealService userRealService;
    @Override
    public ListWrapper<UserInfo> query(UserFilter userFilter) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        if (LongUtil.isNotZero(userFilter.getUserId())) {
            queryWrapper.eq("userid", userFilter.getUserId());
        }
        Page page = new Page(userFilter.getPageNum(), userFilter.getPageSize());
        IPage<UserInfo> userInfoIPage = this.page(page, queryWrapper);
        List<UserInfo> sysRightUrlList = userInfoIPage.getRecords();
        return ListWrapper.<UserInfo>builder().list(sysRightUrlList)
                .total(userInfoIPage.getTotal()).build();
    }

    @Override
    public UserInfo registerUserByMobile(String mobile) {
        Assert.notEmpty(mobile, "mobile 不能为空");
        //根据雪花算法获取用户id
        Long userId = KeyUtil.getId();

        //插入用户基础信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setMobile(mobile);
        //默认分配昵称
        userInfo.setNickname("用户_" + RandomUtil.getRandomNum(10));
        userInfo.insert();
        return userInfo;
    }

    @Override
    public Boolean setUserInfo(UserInfoDto userInfoDto, Long userId) {
        Assert.notNull(userInfoDto, "userInfoDto 不能为空");
        Assert.notNull(userId, "mobile 不能为空");

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setNickname(userInfoDto.getNickname());
        userInfo.setSex(userInfoDto.getSex());
        return userInfo.updateById();
    }

    @Override
    public void setSignature(String newSignature, Long userId) {
        Assert.notNull(userId, "userId 不能为空");
        if (StrUtil.length(newSignature) > 30) {
            throw new AppException("签名长度不能超过字符限制30位");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setSignature(newSignature);
        userInfo.updateById();
    }

    @Override
    public UserInfo getUserInfo(Long userId) {
        Assert.notNull(userId, "userId 不能为空");
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        return userInfo.selectById();
    }

    @Override
    public boolean checkVerifyCode(String mobile, String verifyCode) {
        Assert.notEmpty(mobile, "mobile 不能为空");
        Assert.notEmpty(verifyCode, "verifyCode 不能为空");
        Object savedVerifyCode = redisRepository.get(mobile);
        //验证码正确，注册用户
        if (verifyCode.equals(savedVerifyCode)) {
            return true;
        }
        return false;
    }

    @Override
    public List<CfgArea> getRegions() {
        List<CfgArea> areaList = cfgAreaService.list();
        return areaList;
    }

    @Override
    public UserRegionDto getUserRegion(long userId) {
        Map<String, String> areaMap = cfgAreaService.selectAreaMap();
        UserInfo userInfo = userInfoMapper.selectById(userId);
        UserRegionDto userRegionDto = null;
        if (userInfo != null) {
            userRegionDto = new UserRegionDto();
            userRegionDto.setProvinceCode(userInfo.getProvince());
            userRegionDto.setCityCode(userInfo.getCity());
            userRegionDto.setDistrictCode(userInfo.getDistrict());

            String provinceName = "";
            String cityName = "";
            String districtName = "";
            if (StrUtil.isNotBlank(userInfo.getProvince())) {
                provinceName = StrUtil.trimToEmpty(areaMap.get(userInfo.getProvince()));
                userRegionDto.setProvinceName(provinceName);
            }
            if (StrUtil.isNotBlank(userInfo.getCity())) {
                cityName = StrUtil.trimToEmpty(areaMap.get(userInfo.getCity()));
                userRegionDto.setCityName(cityName);
            }
            if (StrUtil.isNotBlank(userInfo.getDistrict())) {
                districtName = StrUtil.trimToEmpty(areaMap.get(userInfo.getDistrict()));
                userRegionDto.setDistrictName(districtName);
            }

            StringBuilder regionName = new StringBuilder(16);
            if (StrUtil.isNotBlank(provinceName)) {
                regionName.append(regionName.length() > 0 ? "," : "");
                regionName.append(provinceName);
            }
            if (StrUtil.isNotBlank(cityName)) {
                regionName.append(regionName.length() > 0 ? "," : "");
                regionName.append(cityName);
            }
            if (StrUtil.isNotBlank(districtName)) {
                regionName.append(regionName.length() > 0 ? "," : "");
                regionName.append(districtName);
            }
            userRegionDto.setAreaNames(regionName.toString());
        }
        return userRegionDto;
    }

    @Override
    public void setUserRegion(UserRegionDto dto) {
        Assert.notNull(dto, "regionDto 不能为空");
        Assert.notEmpty(String.valueOf(dto.getUserId()), "userId 不能为空");
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(Long.parseLong(dto.getUserId()));
        userInfo.setCountry(dto.getState());
        userInfo.setProvince(dto.getProvinceCode());
        userInfo.setCity(dto.getCityCode());
        userInfo.setDistrict(dto.getDistrictCode());
        userInfo.updateById();
    }

    @Override
    public List<UserAddr> getUserInfoAddresses(String userId) {
        Assert.notEmpty(userId, "userId 不能为空");

        List<UserAddr> addressList = userAddrMapper.listUserAddr(Long.parseLong(userId));
        return addressList;
    }

    @Override
    public boolean updateNickname(String newNickname, Long userId) {
        Assert.notNull(userId, "userId 不能为空");
        if (StrUtil.isBlank(newNickname)) {
            newNickname = "";
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setNickname(newNickname);
        return userInfo.updateById();
    }

    /**
     * 更新用户性别
     * @date 2020/2/17
     * @param newSex
     * @param userId
     * @return boolean
     */
    @Override
    public boolean updateSex(String newSex, Long userId) {
        Assert.notNull(userId, "userId 不能为空");
        if (StrUtil.isBlank(newSex)) {
            newSex = "";
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setSex(newSex);
        return userInfo.updateById();
    }

    @Override
    public UserInfo getUserInfoByMobile(String mobile) {
        Assert.notEmpty(mobile, "mobile 不能为空");
        return this.getOne(new QueryWrapper<UserInfo>().eq("mobile", mobile), true);
    }

    @Override
    public String getFaceImgBigBase64(Long faceImgBig) {
        if (faceImgBig == null || faceImgBig == 0) {
            return "";
        }
        ResponseEntity<byte[]> bytes = fileFeignService.getImgByteArrayByFileId(faceImgBig);
        return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(bytes.getBody());
    }

    @Override
    public Map<Long, String> mapUserIdAndMobileByUserIdList(List<Long> userIdList) {
        Map<Long, String> map = new HashMap<>();
        if (userIdList == null || userIdList.size() <= 0) {
            return map;
        }
        List<UserInfo> list = this.list(new QueryWrapper<UserInfo>().in("userid", userIdList));
        if (list != null || list.size() > 0) {
            for (UserInfo userInfo : list) {
                map.put(userInfo.getUserId(), userInfo.getMobile());
            }
        }
        return map;
    }

    /**
     * 删除用户基本信息
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2019/11/28 10:35
     **/
    @Override
    public void delUserInfo(Long userId) {
        this.removeById(userId);
    }


    @Override
    public boolean updateByMobile(CorpUserDto corpUserDto) {
        UserInfo userinfo = this.getUserInfo(corpUserDto.getUserId());

        UserReal userReal = userRealService.getOne(new QueryWrapper<UserReal>()
                .eq("userid", corpUserDto.getUserId()));


        // 已实名认证，mobile不一致
        if (userinfo != null && userReal != null && userReal.getVerified() == 1
                && !StrUtil.trimToEmpty(corpUserDto.getMobile()).equals(StrUtil.trimToEmpty(userinfo.getMobile()))) {
            throw new AppException("人员已注册，电话与本次修改的不一致，电话只能由人员自行在个人中心中修改！");
        }

        UserInfo userinfoMobile = this.getUserInfoByMobile(corpUserDto.getMobile());

        if (userinfoMobile != null && !userinfo.getUserId().equals(userinfoMobile.getUserId())
                && StrUtil.trimToEmpty(userinfoMobile.getMobile()).equals(StrUtil.trimToEmpty(corpUserDto.getMobile()))) {
            throw new AppException("该电话号码" + corpUserDto.getMobile() + "已经存在，请查证！");
        }
        userinfo.setMobile(corpUserDto.getMobile());
        return this.saveOrUpdate(userinfo);
    }

    /**
     * 模糊查询有效用户
     *
     * @param userInfoFilter
     * @return
     * @author zgpi
     * @date 2020/6/29 18:37
     **/
    @Override
    public List<UserInfoDto> matchUser(UserInfoFilter userInfoFilter) {
        return this.baseMapper.matchUser(userInfoFilter);
    }
}
