package com.zjft.usp.uas.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.corp.service.CorpRegistryService;
import com.zjft.usp.uas.corp.service.CorpUserService;
import com.zjft.usp.uas.user.dto.UserLogonLogDto;
import com.zjft.usp.uas.user.enums.DeviceTypeEnum;
import com.zjft.usp.uas.user.enums.LogonResultEnum;
import com.zjft.usp.uas.user.enums.LogonTypeEnum;
import com.zjft.usp.uas.user.enums.UserLogonOperEnum;
import com.zjft.usp.uas.user.filter.UserLogonLogFilter;
import com.zjft.usp.uas.user.mapper.UserLogonLogMapper;
import com.zjft.usp.uas.user.model.UserLogonLog;
import com.zjft.usp.uas.user.service.UserLogonLogService;
import com.zjft.usp.uas.user.service.UserRealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 用户登录日志表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2020-05-25
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class UserLogonLogServiceImpl extends ServiceImpl<UserLogonLogMapper, UserLogonLog> implements UserLogonLogService {
    @Autowired
    private UserRealService userRealService;
    @Autowired
    private CorpRegistryService corpRegistryService;
    @Autowired
    private CorpUserService corpUserService;


    /**
     * 根据条件分页查询用户登录日志
     * @date 2020/5/26
     * @param userLogonLogFilter
     * @param userInfo
     * @param reqParam
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.uas.user.dto.UserLogonLogDto>
     */
    @Override
    public ListWrapper<UserLogonLogDto> queryUserLogonLog(UserLogonLogFilter userLogonLogFilter, UserInfo userInfo, ReqParam reqParam) {
        IPage<UserLogonLog> page = new Page(userLogonLogFilter.getPageNum(), userLogonLogFilter.getPageSize());
        QueryWrapper<UserLogonLog> queryWrapper = new QueryWrapper();
        List<UserLogonLogDto> userLogonLogDtoList = new ArrayList<>();
        if (LongUtil.isNotZero(userLogonLogFilter.getUserId())) {
            queryWrapper.eq("user_id", userLogonLogFilter.getUserId());
        }
        if (StrUtil.isNotBlank(userLogonLogFilter.getMobile())) {
            queryWrapper.like("mobile", userLogonLogFilter.getMobile());
        }
        if (LongUtil.isNotZero(userLogonLogFilter.getOperateType())) {
            queryWrapper.eq("operate_type", userLogonLogFilter.getOperateType());
        }
        if (LongUtil.isNotZero(userLogonLogFilter.getLogonType())) {
            queryWrapper.eq("logon_type", userLogonLogFilter.getLogonType());
        }
        if (LongUtil.isNotZero(userLogonLogFilter.getLogonResult())) {
            queryWrapper.eq("logon_result", userLogonLogFilter.getLogonResult());
        }
        if (LongUtil.isNotZero(userLogonLogFilter.getDeviceType())) {
            queryWrapper.eq("device_type", userLogonLogFilter.getDeviceType());
        }
        if (userLogonLogFilter.getOperateTimeStart() != null) {
            queryWrapper.ge("operate_time", userLogonLogFilter.getOperateTimeStart());
        }
        if (userLogonLogFilter.getOperateTimeEnd() != null) {
            queryWrapper.le("operate_time", userLogonLogFilter.getOperateTimeEnd());
        }
        // 根据企业来查询
        if (LongUtil.isNotZero(userLogonLogFilter.getCorpId())) {
            queryWrapper.inSql("user_id", "select userid from uas_corp_user where corpid=" + userLogonLogFilter.getCorpId());
        }
        IPage<UserLogonLog> iPage = this.page(page, queryWrapper.orderByDesc("operate_time"));
        UserLogonLogDto userLogonLogDto;
        Set<Long> userIdSet = new HashSet<>();
        if (CollectionUtil.isNotEmpty(iPage.getRecords())) {
            for (UserLogonLog userLogonLog : iPage.getRecords()) {
                if(LongUtil.isNotZero(userLogonLog.getUserId())) {
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
            if(CollectionUtil.isNotEmpty(corpMap.get(userId))){
                for(Long corp : corpMap.get(userId)){
                    corpList.add(corp);
                }
            }
        }
        Map<Long, String> corpAndNameMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(corpList)){
            corpAndNameMap = corpRegistryService.getCorpNameList(corpList);
        }

        for (UserLogonLog userLogonLog : iPage.getRecords()) {
            userLogonLogDto = new UserLogonLogDto();
            BeanUtils.copyProperties(userLogonLog, userLogonLogDto);
            if(LongUtil.isNotZero(userLogonLog.getUserId())) {
                userLogonLogDto.setUserName(userMap.get(userLogonLog.getUserId()));
                // 企业id
                if(CollectionUtil.isNotEmpty(corpMap)){
                    userLogonLogDto.setCorpIdList(corpMap.get(userLogonLog.getUserId()));
                    //企业名称
                    List<Long> corpIds= corpMap.get(userLogonLog.getUserId());
                    String corpName = null;
                    for (int i = 0; i < corpIds.size(); i++) {
                        if (LongUtil.isNotZero(corpIds.get(i))) {
                            if (corpName == null) {
                                corpName = corpAndNameMap.get(corpIds.get(i));
                            } else if(i == 1) {
                                corpName = corpName + ',' + corpAndNameMap.get(corpIds.get(i));
                            }else if(i == 2) {
                                /* corpName = corpName + ',' + corpAndNameMap.get(corpIdList.get(i));*/
                                corpName = corpName + '等';
                            }
                        }
                    }
                    userLogonLogDto.setCorpName(corpName);
                }
            }
            if(IntUtil.isNotZero(userLogonLog.getOperateType())){
                userLogonLogDto.setOperateTypeName(UserLogonOperEnum.getNameByCode(userLogonLog.getOperateType()));
            }
            if(IntUtil.isNotZero(userLogonLog.getLogonType())){
                userLogonLogDto.setLogonTypeName(LogonTypeEnum.getNameByCode(userLogonLog.getLogonType()));
            }
            if(IntUtil.isNotZero(userLogonLog.getLogonResult())){
                userLogonLogDto.setLogonResultName(LogonResultEnum.getNameByCode(userLogonLog.getLogonResult()));
            }
            if(IntUtil.isNotZero(userLogonLog.getDeviceType())){
                userLogonLogDto.setDeviceTypeName(DeviceTypeEnum.getNameByCode(userLogonLog.getDeviceType()));
            }
            userLogonLogDtoList.add(userLogonLogDto);
        }
        return ListWrapper.<UserLogonLogDto>builder()
                .list(userLogonLogDtoList)
                .total(iPage.getTotal())
                .build();
    }

    /**
     * 保存登录成功日志
     *
     * @param userLogonLogDto
     * @return
     * @author zgpi
     * @date 2020/5/25 15:12
     **/
    @Override
    public void saveLogonSuccessLog(UserLogonLogDto userLogonLogDto) {
        UserLogonLog userLogonLog = new UserLogonLog();
        BeanUtils.copyProperties(userLogonLogDto, userLogonLog);
        userLogonLog.setLogId(KeyUtil.getId());
        userLogonLog.setOperateType(UserLogonOperEnum.LOGON.getCode());
        userLogonLog.setLogonResult(LogonResultEnum.SUCCESS.getCode());
        userLogonLog.setOsVersion(StrUtil.sub(userLogonLog.getOsVersion(), 0, 300));
        this.save(userLogonLog);
    }

    /**
     * 保存自动登录成功日志
     *
     * @param userLogonLogDto
     * @return
     * @author zgpi
     * @date 2020/5/26 15:30
     **/
    @Override
    public void saveAutoLogonSuccessLog(UserLogonLogDto userLogonLogDto) {
        UserLogonLog userLogonLog = new UserLogonLog();
        BeanUtils.copyProperties(userLogonLogDto, userLogonLog);
        userLogonLog.setLogId(KeyUtil.getId());
        userLogonLog.setOperateType(UserLogonOperEnum.AUTO_LOGON.getCode());
        userLogonLog.setLogonResult(LogonResultEnum.SUCCESS.getCode());
        userLogonLog.setOsVersion(StrUtil.sub(userLogonLog.getOsVersion(), 0, 300));
        this.save(userLogonLog);
    }

    /**
     * 保存密码失败日志
     *
     * @param userLogonLogDto
     * @return
     * @author zgpi
     * @date 2020/5/25 15:12
     **/
    @Override
    public void saveLogonPwdFailLog(UserLogonLogDto userLogonLogDto) {
        UserLogonLog userLogonLog = new UserLogonLog();
        BeanUtils.copyProperties(userLogonLogDto, userLogonLog);
        userLogonLog.setLogId(KeyUtil.getId());
        userLogonLog.setOperateType(UserLogonOperEnum.LOGON.getCode());
        userLogonLog.setLogonResult(LogonResultEnum.PWD_FAIL.getCode());
        userLogonLog.setOsVersion(StrUtil.sub(userLogonLog.getOsVersion(), 0, 300));
        this.save(userLogonLog);
    }

    /**
     * 保存登出日志
     *
     * @param userLogonLogDto
     * @return
     * @author zgpi
     * @date 2020/5/25 20:01
     **/
    @Override
    public void saveLogoutLog(UserLogonLogDto userLogonLogDto) {
        UserLogonLog userLogonLog = new UserLogonLog();
        BeanUtils.copyProperties(userLogonLogDto, userLogonLog);
        userLogonLog.setLogId(KeyUtil.getId());
        userLogonLog.setOperateType(UserLogonOperEnum.LOGOUT.getCode());
        userLogonLog.setOsVersion(StrUtil.sub(userLogonLog.getOsVersion(), 0, 300));
        this.save(userLogonLog);
    }
}
