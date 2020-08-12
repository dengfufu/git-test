package com.zjft.usp.uas.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.corp.service.CorpRegistryService;
import com.zjft.usp.uas.corp.service.CorpUserService;
import com.zjft.usp.uas.user.dto.UserOperDto;
import com.zjft.usp.uas.user.enums.UserOperEnum;
import com.zjft.usp.uas.user.filter.UserOperFilter;
import com.zjft.usp.uas.user.mapper.UserOperMapper;
import com.zjft.usp.uas.user.model.UserOper;
import com.zjft.usp.uas.user.service.UserOperService;
import com.zjft.usp.uas.user.service.UserRealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * @author zphu
 * @date 2019/8/15 14:45
 * @Version 1.0
 **/
@Transactional(rollbackFor = Exception.class)
@Service
public class UserOperServiceImpl extends ServiceImpl<UserOperMapper, UserOper> implements UserOperService {
    @Autowired
    private UserRealService userRealService;
    @Autowired
    private CorpRegistryService corpRegistryService;
    @Autowired
    private CorpUserService corpUserService;

    /**
     * 根据条件分页查询用户操作
     *
     * @param userOperFilter
     * @param userInfo
     * @param reqParam
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.uas.user.dto.UserLogonLogDto>
     * @date 2020/5/26
     */
    @Override
    public ListWrapper<UserOperDto> queryUserOper(UserOperFilter userOperFilter, UserInfo userInfo, ReqParam reqParam) {
        IPage<UserOper> page = new Page(userOperFilter.getPageNum(), userOperFilter.getPageSize());
        QueryWrapper<UserOper> queryWrapper = new QueryWrapper();
        List<UserOperDto> userOperDtoList = new ArrayList<>();
        if (LongUtil.isNotZero(userOperFilter.getUserId())) {
            queryWrapper.eq("userid", userOperFilter.getUserId());
        }
        if (LongUtil.isNotZero(userOperFilter.getOperType())) {
            queryWrapper.eq("opertype", userOperFilter.getOperType());
        }
        if (userOperFilter.getOperateTimeStart() != null) {
            queryWrapper.ge("opertime", userOperFilter.getOperateTimeStart());
        }
        if (userOperFilter.getOperateTimeEnd() != null) {
            queryWrapper.le("opertime", userOperFilter.getOperateTimeEnd());
        }
        // 根据企业来查询
        if (LongUtil.isNotZero(userOperFilter.getCorpId())) {
            queryWrapper.inSql("userid", "select userid from uas_corp_user where corpid=" + userOperFilter.getCorpId());
        }
        IPage<UserOper> iPage = this.page(page, queryWrapper.orderByDesc("opertime"));
        Set<Long> userIdSet = new HashSet<>();
        if (CollectionUtil.isNotEmpty(iPage.getRecords())) {
            for (UserOper userLogonLog : iPage.getRecords()) {
                if (LongUtil.isNotZero(userLogonLog.getUserId())) {
                    userIdSet.add(userLogonLog.getUserId());
                }
            }
        }
        List<Long> userIdList = new ArrayList<>(userIdSet);
        List<Long> corpList = new ArrayList<>();
        // 获得处理人姓名集合
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

        UserOperDto userOperDto;
        for (UserOper userOper : iPage.getRecords()) {
            userOperDto = new UserOperDto();
            BeanUtils.copyProperties(userOper, userOperDto);
            if (LongUtil.isNotZero(userOper.getUserId())) {
                if (CollectionUtil.isNotEmpty(userMap)) {
                    userOperDto.setUserName(userMap.get(userOper.getUserId()));
                }
                // 企业id
                if (CollectionUtil.isNotEmpty(corpMap)) {
                    userOperDto.setCorpIdList(corpMap.get(userOper.getUserId()));
                    //企业名称
                    List<Long> corps = corpMap.get(userOper.getUserId());
                    String corpsName = null;
                    for (int i = 0; i < corps.size(); i++) {
                        if (LongUtil.isNotZero(corps.get(i))) {
                            if (corpsName == null) {
                                corpsName = corpAndNameMap.get(corps.get(i));
                            } else if (i == 1) {
                                /* corpName = corpName + ',' + corpAndNameMap.get(corpIdList.get(i));*/
                                corpsName = corpsName + '等';
                            }
                        }
                    }
                    userOperDto.setCorpName(corpsName);
                }
            }
            if (userOper.getOperType() != null) {
                userOperDto.setOperTypeName(UserOperEnum.getName(userOper.getOperType()));
            }
            userOperDtoList.add(userOperDto);
        }
        return ListWrapper.<UserOperDto>builder()
                .list(userOperDtoList)
                .total(iPage.getTotal())
                .build();
    }

    @Override
    public void insert(String changeInfo, String beforeInfo, UserOperEnum userOperEnum,
                       Long userId, ReqParam reqParam, String clientId) {
        UserOper userOper = new UserOper();
        userOper.setUserId(userId);
        userOper.setClientId(clientId);
        userOper.setLat(reqParam.getLat());
        userOper.setLon(reqParam.getLon());
        userOper.setTxId(reqParam.getTxId());
        userOper.setOperType(userOperEnum.getIndex());
        String details = userOperEnum.getName();
        if (StrUtil.isNotBlank(beforeInfo) || StrUtil.isNotBlank(changeInfo)) {
            details += "：" + beforeInfo + "' => '" + changeInfo + "'";
        }
        //防止details超长
        if (details.length() > 200) {
            details = details.substring(0, 200);
        }
        userOper.setDetails(details);
        userOper.insert();
    }
}
