package com.zjft.usp.uas.corp.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.mq.util.MqSenderUtil;
import com.zjft.usp.uas.corp.dto.CorpUserAppDto;
import com.zjft.usp.uas.corp.enums.CheckResultEnum;
import com.zjft.usp.uas.corp.enums.CorpOperationEnum;
import com.zjft.usp.uas.corp.enums.FlowStatusEnum;
import com.zjft.usp.uas.corp.filter.CorpUserAppFilter;
import com.zjft.usp.uas.corp.listener.CorpMqTopic;
import com.zjft.usp.uas.corp.mapper.CorpUserAppMapper;
import com.zjft.usp.uas.corp.model.CorpUser;
import com.zjft.usp.uas.corp.model.CorpUserApp;
import com.zjft.usp.uas.corp.model.CorpUserTrace;
import com.zjft.usp.uas.corp.service.CorpOperationService;
import com.zjft.usp.uas.corp.service.CorpUserAppService;
import com.zjft.usp.uas.corp.service.CorpUserService;
import com.zjft.usp.uas.corp.service.CorpUserTraceService;
import com.zjft.usp.uas.right.composite.SysRoleUserCompoService;
import com.zjft.usp.uas.user.model.UserReal;
import com.zjft.usp.uas.user.service.UserInfoService;
import com.zjft.usp.uas.user.service.UserRealService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 加入企业申请实现类
 *
 * @author user
 * @version 1.0
 * @date 2019-09-29 09:48
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class CorpUserAppServiceImpl extends ServiceImpl<CorpUserAppMapper, CorpUserApp> implements CorpUserAppService {

    @Autowired
    private CorpOperationService corpOperationService;
    @Autowired
    private CorpUserService corpUserService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserRealService userRealService;
    @Autowired
    private SysRoleUserCompoService sysRoleUserCompoService;
    @Autowired
    private MqSenderUtil mqSenderUtil;

    @Autowired
    private CorpUserTraceService corpUserTraceService;

    @Override
    public void joinCorpApply(CorpUserAppDto corpUserAppDto, ReqParam reqParam, Long curUserId, String clientId) {
        if (corpUserAppDto == null || corpUserAppDto.getCorpId() == null) {
            throw new AppException("企业编号不能为空！");
        }
        if (corpUserService.ifUserInCorp(curUserId, corpUserAppDto.getCorpId())) {
            throw new AppException("您已加入该公司！");
        }
        if (this.ifUserHasApply(curUserId, corpUserAppDto.getCorpId())) {
            throw new AppException("您已申请加入该公司！");
        }
        // account不能重复绑定
        if (!StringUtils.isEmpty(corpUserAppDto.getAccount()) &&
                corpUserService.ifAccountInCorp(corpUserAppDto.getAccount(), corpUserAppDto.getCorpId(), curUserId)) {
            throw new AppException("该企业账号已经被绑定！");
        }
        CorpUserApp corpUserApp = new CorpUserApp();
        BeanUtils.copyProperties(corpUserAppDto, corpUserApp);
        corpUserApp.setCorpId(corpUserAppDto.getCorpId());
        corpUserApp.setInviteUserId(corpUserAppDto.getInviteUserId() == null ? 0L : corpUserAppDto.getInviteUserId());
        corpUserApp.setApplyTime(DateUtil.date());
        corpUserApp.setApplyUserId(curUserId);
        corpUserApp.setAccount(corpUserAppDto.getAccount());
        //这里需要生成交易编号
        corpUserApp.setTxId(reqParam.getTxId());
        corpUserApp.setStatus(FlowStatusEnum.APPLY.getCode());
        corpUserApp.setApplyNote(StrUtil.trimToEmpty(corpUserApp.getApplyNote()));
        corpUserApp.insert();

        // 自动审核通过
        if (corpUserAppDto.getAutoJoin()) {
            BeanUtils.copyProperties(corpUserApp, corpUserAppDto);
            corpUserAppDto.setCheckResult("Y");
            this.joinCorpCheck(corpUserAppDto, reqParam, curUserId, clientId);
        } else {
            //生成操作记录
            corpOperationService.addCorpOperation(CorpOperationEnum.CO_USER_APPLY, corpUserApp.getCorpId(), reqParam, curUserId, clientId);
        }
    }

    /**
     * /**
     * 加入企业申请后提醒管理员审核
     *
     * @param userId
     * @param corpId
     */
    @Override
    public void corpUserJoinListener(long userId, long corpId) {
        Map<String, Object> msg = new HashMap<>(2);
        msg.put("userId", userId);
        msg.put("corpId", corpId);
        mqSenderUtil.sendMessage(CorpMqTopic.CORP_USER_JOIN, JsonUtil.toJson(msg));
    }

    @Override
    public void joinCorpCheck(CorpUserAppDto corpUserAppDto, ReqParam reqParam, Long curUserId, String clientId) {
        if (LongUtil.isZero(corpUserAppDto.getCorpId())) {
            throw new AppException("企业编号不能为空！");
        }
        CorpUserApp corpUserAppOld = this.lambdaQuery().eq(CorpUserApp::getCorpId, corpUserAppDto.getCorpId())
                .eq(CorpUserApp::getTxId, corpUserAppDto.getTxId()).one();
        if (corpUserAppOld == null) {
            throw new AppException("所审核的申请记录不存在！");
        }
        if (CheckResultEnum.CHECK_PASS.getCode().equals(corpUserAppDto.getCheckResult())) {
            // account不能重复绑定
            if (!StringUtils.isEmpty(corpUserAppOld.getAccount()) &&
                    corpUserService.ifAccountInCorp(corpUserAppOld.getAccount(), corpUserAppOld.getCorpId(), corpUserAppOld.getApplyUserId())) {
                throw new AppException("该企业账号已经被绑定！");
            }
            // 申请表修改记录为通过
            corpUserAppOld.setStatus(FlowStatusEnum.PASS.getCode());
            //保存到用户公司表
            CorpUser corpUser = new CorpUser();
            corpUser.setUserId(corpUserAppOld.getApplyUserId());
            corpUser.setCorpId(corpUserAppOld.getCorpId());
            corpUser.setAccount(corpUserAppOld.getAccount());
            corpUser.setAddTime(DateUtil.date());
            corpUserService.save(corpUser);

            // 修改姓名
            UserReal userReal = new UserReal();
            userReal.setUserId(corpUser.getUserId());
            userReal.setUserName(corpUserAppOld.getUserName());
            userRealService.saveOrUpdate(userReal);
            // 添加角色人员
            List<Long> roleIdList = corpUserAppDto.getRoleIdList();
            sysRoleUserCompoService.addRoleUser(corpUserAppDto.getCorpId(), corpUser.getUserId(), roleIdList);

            // 增加人员流动备份(入职)
            CorpUserTrace corpUserTrace = new CorpUserTrace();
            corpUserTrace
                    .setCorpId(corpUserAppOld.getCorpId())
                    .setUserId(corpUserAppOld.getApplyUserId())
                    .setOperate(1)
                    .setOperator(curUserId)
                    .setClientId(clientId)
                    .setOperateTime(DateUtil.date());
            this.corpUserTraceService.save(corpUserTrace);
        } else {
            corpUserAppOld.setStatus(FlowStatusEnum.REFUSE.getCode());
        }
        corpUserAppOld.setCheckTime(DateUtil.date());
        corpUserAppOld.setCheckUserId(curUserId);
        UpdateWrapper<CorpUserApp> updateWrapper = new UpdateWrapper<>();
        // 联合主键不能使用updateById()，使用update
        updateWrapper.eq("corpid", corpUserAppOld.getCorpId()).eq("txid", corpUserAppOld.getTxId());
        this.update(corpUserAppOld, updateWrapper);
        corpOperationService.addCorpOperation(CorpOperationEnum.CO_USER_CHECK, corpUserAppOld.getCorpId(), reqParam, curUserId, clientId);
    }

    @Override
    public boolean ifUserHasApply(long userId, long corpId) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("corpid", corpId);
        paramsMap.put("applyuserid", userId);
        paramsMap.put("status", FlowStatusEnum.APPLY.getCode());
        List<CorpUserApp> corpUserApps = this.baseMapper.selectByMap(paramsMap);
        return corpUserApps != null && corpUserApps.size() > 0;
    }

    @Override
    public CorpUserApp getApplyingApp(Long userId, Long corpId) {
        if (LongUtil.isZero(userId) || LongUtil.isZero(corpId)) {
            return null;
        }
        QueryWrapper<CorpUserApp> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("applyuserid", userId);
        queryWrapper.eq("corpid", corpId);
        queryWrapper.eq("status", FlowStatusEnum.APPLY.getCode());
        CorpUserApp corpUserApp = this.getOne(queryWrapper);
        return corpUserApp;
    }

    @Override
    public ListWrapper<CorpUserAppDto> pageCorpUserApp(CorpUserAppFilter corpUserAppFilter, ReqParam reqParam) {
        ListWrapper<CorpUserAppDto> listWrapper = new ListWrapper<>();
        if (corpUserAppFilter == null) {
            return listWrapper;
        }
        if (LongUtil.isZero(corpUserAppFilter.getCorpId())) {
            corpUserAppFilter.setCorpId(reqParam.getCorpId());
        }
        Page page = new Page(corpUserAppFilter.getPageNum(), corpUserAppFilter.getPageSize());
        QueryWrapper<CorpUserApp> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("corpid", corpUserAppFilter.getCorpId());
        if (!StringUtil.isNullOrEmpty(corpUserAppFilter.getUserName())) {
            queryWrapper.like("username", corpUserAppFilter.getUserName());
        }
        if (corpUserAppFilter.getApplyTimeRange() != null && corpUserAppFilter.getApplyTimeRange().size() > 1) {
            queryWrapper.ge("applytime", corpUserAppFilter.getApplyTimeRange().get(0));
            queryWrapper.le("applytime", corpUserAppFilter.getApplyTimeRange().get(1));
        }
        if (corpUserAppFilter.getStatusList() != null && corpUserAppFilter.getStatusList().size() > 0) {
            queryWrapper.in("status", corpUserAppFilter.getStatusList());
        }
        queryWrapper.orderByDesc("applytime");
        IPage<CorpUserApp> corpUserAppIPage = this.page(page, queryWrapper);
        if (corpUserAppIPage != null && corpUserAppIPage.getRecords() != null && corpUserAppIPage.getRecords().size() > 0) {
            List<CorpUserAppDto> dtoList = new ArrayList<>();
            List<Long> userIdList = null;
            HashSet<Long> set = new LinkedHashSet<>();
            for (CorpUserApp corpUserApp : corpUserAppIPage.getRecords()) {
                set.add(corpUserApp.getApplyUserId());
                set.add(corpUserApp.getCheckUserId());
            }
            userIdList = new ArrayList<>(set);
            Map<Long, String> userIdAndMobileMap = this.userInfoService.mapUserIdAndMobileByUserIdList(userIdList);
            Map<Long, String> userIdAndNameMap = this.userRealService.mapIdAndName(userIdList);
            for (CorpUserApp corpUserApp : corpUserAppIPage.getRecords()) {
                CorpUserAppDto dto = new CorpUserAppDto();
                BeanUtils.copyProperties(corpUserApp, dto);
                if (LongUtil.isNotZero(dto.getCheckUserId())) {
                    dto.setCheckUserName(userIdAndNameMap.get(dto.getCheckUserId()));
                }
                dto.setMobile(userIdAndMobileMap.get(dto.getApplyUserId()));
                dtoList.add(dto);
            }
            listWrapper.setList(dtoList);
            listWrapper.setTotal(corpUserAppIPage.getTotal());
        }
        return listWrapper;
    }

}
