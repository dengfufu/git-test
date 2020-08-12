package com.zjft.usp.uas.user.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.baseinfo.model.CfgArea;
import com.zjft.usp.uas.baseinfo.service.CfgAreaService;
import com.zjft.usp.uas.corp.service.CorpRegistryService;
import com.zjft.usp.uas.corp.service.CorpUserService;
import com.zjft.usp.uas.user.composite.UserCompoService;
import com.zjft.usp.uas.user.dto.UserInfoDto;
import com.zjft.usp.uas.user.filter.UserInfoFilter;
import com.zjft.usp.uas.user.model.UserMobile;
import com.zjft.usp.uas.user.service.UserAccountService;
import com.zjft.usp.uas.user.service.UserInfoService;
import com.zjft.usp.uas.user.service.UserMobileService;
import com.zjft.usp.uas.user.service.UserRealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zgpi
 * @version 1.0
 * @date 2019/12/19 11:01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserCompoServiceImpl implements UserCompoService {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserRealService userRealService;
    @Autowired
    private CorpUserService corpUserService;
    @Autowired
    private CorpRegistryService corpRegistryService;
    @Autowired
    private CfgAreaService cfgAreaService;
    @Autowired
    private UserMobileService userMobileService;

    /**
     * 根据条件分页查询用户基本信息
     *
     * @param userInfoFilter
     * @param reqParam
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.uas.user.dto.UserLogonLogDto>
     * @date 2020/5/26
     */
    @Override
    public ListWrapper<UserInfoDto> queryUserInfo(UserInfoFilter userInfoFilter, ReqParam reqParam) {
        IPage<UserInfo> page = new Page(userInfoFilter.getPageNum(), userInfoFilter.getPageSize());
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper();
        List<UserInfoDto> userInfoDtoList = new ArrayList<>();
        if (LongUtil.isNotZero(userInfoFilter.getUserId())) {
            queryWrapper.eq("userid", userInfoFilter.getUserId());
        }
        if (StrUtil.isNotBlank(userInfoFilter.getMobile())) {
            queryWrapper.like("mobile", userInfoFilter.getMobile());
        }
        if (StrUtil.isNotBlank(userInfoFilter.getNickname())) {
            queryWrapper.like("nickname", userInfoFilter.getNickname());
        }
        if (StrUtil.isNotBlank(userInfoFilter.getSex())) {
            queryWrapper.eq("sex", userInfoFilter.getSex());
        }
        if (StrUtil.isNotBlank(userInfoFilter.getCountry())) {
            queryWrapper.eq("country", userInfoFilter.getCountry());
        }
        if (StrUtil.isNotBlank(userInfoFilter.getDistrict())) {
            queryWrapper.likeRight("district", userInfoFilter.getDistrict());
        }
        if (StrUtil.isNotBlank(userInfoFilter.getStatus())) {
            queryWrapper.eq("status", userInfoFilter.getStatus());
        }
        if (userInfoFilter.getRegTimeStart() != null) {
            queryWrapper.ge("regtime", userInfoFilter.getRegTimeStart());
        }
        if (userInfoFilter.getRegTimeTimeEnd() != null) {
            queryWrapper.le("regtime", userInfoFilter.getRegTimeTimeEnd());
        }
        if (LongUtil.isNotZero(userInfoFilter.getCorpId())) {
            queryWrapper.inSql("userid", "select userid from uas_corp_user where corpid=" + userInfoFilter.getCorpId());
        }

        IPage<UserInfo> iPage = userInfoService.page(page, queryWrapper.orderByDesc("regtime"));
        Set<Long> userIdSet = new HashSet<>();
        List<Long> codeList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(iPage.getRecords())) {
            for (UserInfo userInfo : iPage.getRecords()) {
                if (LongUtil.isNotZero(userInfo.getUserId())) {
                    userIdSet.add(userInfo.getUserId());
                }
                if (StrUtil.isNotBlank(userInfo.getDistrict())) {
                    if (StrUtil.isNotBlank(userInfo.getDistrict()) && userInfo.getDistrict().length() >= 2) {
                        codeList.add(Long.parseLong(userInfo.getDistrict().substring(0, 2)));
                    }
                    if (StrUtil.isNotBlank(userInfo.getDistrict()) && userInfo.getDistrict().length() >= 4) {
                        codeList.add(Long.parseLong(userInfo.getDistrict().substring(0, 4)));
                    }
                    if (StrUtil.isNotBlank(userInfo.getDistrict()) && userInfo.getDistrict().length() >= 6) {
                        codeList.add(Long.parseLong(userInfo.getDistrict().substring(0, 6)));
                    }
                }
            }
        }

        List<Long> userIdList = new ArrayList<>(userIdSet);
        List<Long> corpList = new ArrayList<>();
        Map<Long, String> userMap = userRealService.mapIdAndName(userIdList);
        Map<Long, List<Long>> corpMap = corpUserService.listCorpIdListByUserId(userIdList);
        for (Long userId : userIdList) {
            if (CollectionUtil.isNotEmpty(corpMap.get(userId))) {
                for (Long corp : corpMap.get(userId)) {
                    corpList.add(corp);
                }
            }
        }
        Map<Long, String> corpAndNameMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(corpList)) {
            corpAndNameMap = corpRegistryService.getCorpNameList(corpList);
        }

        // 重庆市特殊处理
        codeList.add(5001L);
        List<CfgArea> list = (List<CfgArea>) cfgAreaService.listByIds(codeList);
        Map<String, String> areaMap = new HashMap<>();
        for (CfgArea cfgArea : list) {
            areaMap.put(cfgArea.getCode(), cfgArea.getName());
        }
        UserInfoDto userInfoDto;
        for (UserInfo userInfo : iPage.getRecords()) {
            userInfoDto = new UserInfoDto();
            BeanUtils.copyProperties(userInfo, userInfoDto);
            if (LongUtil.isNotZero(userInfo.getUserId())) {
                if (CollectionUtil.isNotEmpty(userMap)) {
                    userInfoDto.setUserName(userMap.get(userInfo.getUserId()));
                }
            }
            if (StrUtil.isNotBlank(userInfoDto.getDistrict())) {
                String province = "";
                String city = "";
                String district = "";
                if (userInfoDto.getDistrict().length() >= 2) {
                    province = StrUtil.trimToEmpty(areaMap.get(userInfoDto.getDistrict().substring(0, 2)));
                }
                if (userInfoDto.getDistrict().length() >= 4) {
                    // 如果是省直辖市
                    if (userInfoDto.getDistrict().matches("\\d{2}9\\d{3}")) {
                        city = StrUtil.trimToEmpty(areaMap.get(userInfoDto.getDistrict()));
                    } else if (userInfoDto.getDistrict().matches("5002\\d{2}")) {
                        city = StrUtil.trimToEmpty(areaMap.get("5001"));
                    } else {
                        city = StrUtil.trimToEmpty(areaMap.get(userInfoDto.getDistrict().substring(0, 4)));
                    }
                }
                // 非省直辖市
                if (userInfoDto.getDistrict().length() >= 6 && !userInfoDto.getDistrict().matches("\\d{2}9\\d{3}")) {
                    district = StrUtil.trimToEmpty(areaMap.get(userInfoDto.getDistrict().substring(0, 6)));
                }
                userInfoDto.setCityName(city);
                userInfoDto.setProvinceName(province.replace("省", "").replace("自治区", "").replace("特别行政区", "")
                        .replace("回族", "").replace("壮族", "").replace("维吾尔", ""));
                userInfoDto.setDistrictName(district);
            }
            // 企业id
            if (CollectionUtil.isNotEmpty(corpMap)) {
                userInfoDto.setCorpIdList(corpMap.get(userInfo.getUserId()));
                List<Long> corpIdList = corpMap.get(userInfo.getUserId());
                //企业名称
                String corpName = null;
                for (int i = 0; i < corpIdList.size(); i++) {
                    if (LongUtil.isNotZero(corpIdList.get(i))) {
                        if (corpName == null) {
                            corpName = corpAndNameMap.get(corpIdList.get(i));
                        } else if (i == 1) {
                            /* corpName = corpName + ',' + corpAndNameMap.get(corpIdList.get(i));*/
                            corpName = corpName + '等';
                        }
                    }
                }
                userInfoDto.setCorpName(corpName);
            }
            userInfoDto.setFaceImgBig(String.valueOf(userInfo.getFaceImgBig()));
            userInfoDto.setUserId(String.valueOf(userInfo.getUserId()));
            userInfoDtoList.add(userInfoDto);
        }
        return ListWrapper.<UserInfoDto>builder()
                .list(userInfoDtoList)
                .total(iPage.getTotal())
                .build();
    }

    /**
     * 模糊查询平台有效用户
     *
     * @param userInfoFilter
     * @param reqParam
     * @return
     * @date 2020/5/26
     */
    @Override
    public List<UserInfoDto> matchUser(UserInfoFilter userInfoFilter, ReqParam reqParam) {
        List<UserInfoDto> userInfoDtoList = userInfoService.matchUser(userInfoFilter);
        userInfoDtoList.forEach(obj -> obj.setUserName(StrUtil.trimToEmpty(obj.getUserName())));
        return userInfoDtoList;
    }


    @Override
    public UserInfoDto findUserDetail(Long userId, ReqParam reqParam) {
        UserInfoDto userInfoDto = userAccountService.getUserInfoDtoById(userId);
        return userInfoDto;
    }

    /**
     * 用户编号与手机号映射
     *
     * @param userIdList
     * @return
     * @author zgpi
     * @date 2019/12/19 11:01
     **/
    @Override
    public Map<Long, String> mapUserIdAndMobile(List<Long> userIdList) {
        if (userIdList != null && userIdList.size() > 0) {
            List<UserMobile> userMobileList = userMobileService.list(new QueryWrapper<UserMobile>().in("userid", userIdList));
            return userMobileList.stream().collect(Collectors.toMap(UserMobile::getUserId, UserMobile::getMobile));
        }
        return new HashMap<>();
    }
}
