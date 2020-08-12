package com.zjft.usp.uas.corp.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.file.service.FileFeignService;
import com.zjft.usp.uas.baseinfo.service.CfgAreaService;
import com.zjft.usp.uas.corp.dto.CorpVerifyAppDto;
import com.zjft.usp.uas.corp.enums.CheckResultEnum;
import com.zjft.usp.uas.corp.enums.CorpOperationEnum;
import com.zjft.usp.uas.corp.enums.FlowStatusEnum;
import com.zjft.usp.uas.corp.filter.CorpVerifyAppFilter;
import com.zjft.usp.uas.corp.mapper.CorpVerifyAppMapper;
import com.zjft.usp.uas.corp.model.CorpRegistry;
import com.zjft.usp.uas.corp.model.CorpVerifyApp;
import com.zjft.usp.uas.corp.service.CorpOperationService;
import com.zjft.usp.uas.corp.service.CorpRegistryService;
import com.zjft.usp.uas.corp.service.CorpVerifyAppService;
import com.zjft.usp.uas.corp.service.CorpVerifyService;
import com.zjft.usp.uas.user.model.UserReal;
import com.zjft.usp.uas.user.service.UserRealService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 企业认证申请实现类
 *
 * @author user
 * @version 1.0
 * @date 2019-08-22 19:00
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class CorpVerifyAppServiceImpl extends ServiceImpl<CorpVerifyAppMapper, CorpVerifyApp> implements CorpVerifyAppService {

    @Autowired
    private CorpOperationService corpOperationService;
    @Autowired
    private CorpVerifyService corpVerifyService;
    @Autowired
    private UserRealService userRealService;
    @Autowired
    private CorpRegistryService corpRegistryService;
    @Autowired
    private CfgAreaService cfgAreaService;

    @Resource
    private FileFeignService fileFeignService;

    @Override
    public void corpVerifyApply(CorpVerifyAppDto corpVerifyAppDto, ReqParam reqParam, Long curUserId, String clientId) {
        if (LongUtil.isZero(corpVerifyAppDto.getCorpId())) {
            throw new AppException("企业编号不能为空！");
        }
        UserReal userReal = userRealService.lambdaQuery().eq(UserReal::getUserId, curUserId).one();
//        if (userReal == null) {
//            throw new AppException("请先进行个人实名认证！");
//        }
        CorpVerifyApp corpVerifyApp = new CorpVerifyApp();
        BeanUtils.copyProperties(corpVerifyAppDto, corpVerifyApp);
        corpVerifyApp.setCorpId(corpVerifyAppDto.getCorpId());
        corpVerifyApp.setApplyUserId(curUserId);
        if (userReal != null) {
            corpVerifyApp.setApplyIdCardNum(userReal.getIdCard());
        }
        corpVerifyApp.setApplyTime(Timestamp.valueOf(LocalDateTime.now()));
        if (StrUtil.isNotBlank(corpVerifyApp.getCity()) && corpVerifyApp.getCity().length() == 4) {
            corpVerifyApp.setProvince(corpVerifyApp.getCity().substring(1, 2));
        }
        corpVerifyApp.setStatus(FlowStatusEnum.APPLY.getCode());
        corpVerifyApp.setTxId(reqParam.getTxId());
        corpVerifyApp.insert();
        //生成操作记录
        corpOperationService.addCorpOperation(CorpOperationEnum.CO_VERIFY_APPLY, corpVerifyApp.getCorpId(), reqParam, curUserId, clientId);
        // 删除临时文件表数据
        if (LongUtil.isNotZero(corpVerifyAppDto.getLicenseFileId())) {
            this.fileFeignService.deleteFileTemporaryByID(corpVerifyAppDto.getLicenseFileId());
        }
    }

    @Override
    public Result corpVerifyCheck(CorpVerifyAppDto corpVerifyAppDto, ReqParam reqParam, Long curUserId, String clientId) {
        if (corpVerifyAppDto == null) {
            throw new AppException("参数解析失败！");
        }
        CorpVerifyApp corpVerifyAppOld = this.lambdaQuery().eq(CorpVerifyApp::getCorpId, corpVerifyAppDto.getCorpId())
                .eq(CorpVerifyApp::getTxId, corpVerifyAppDto.getTxId()).one();
        if (corpVerifyAppOld == null) {
            throw new AppException("出现异常！");
        }
        UpdateWrapper<CorpVerifyApp> updateWrapper = new UpdateWrapper<>();
        // 联合主键不能使用updateById
        updateWrapper.eq("corpid", corpVerifyAppOld.getCorpId()).eq("txid", corpVerifyAppOld.getTxId());
        //判断审核结果
        if (CheckResultEnum.CHECK_PASS.getCode().equals(corpVerifyAppDto.getCheckResult())) {
            //认证通过后添加到企业认证表
            corpVerifyService.createVerifyByCheck(corpVerifyAppOld);
            BeanUtils.copyProperties(corpVerifyAppDto, corpVerifyAppOld);
            corpVerifyAppOld.setStatus(FlowStatusEnum.PASS.getCode());
        } else {
            corpVerifyAppOld.setStatus(FlowStatusEnum.REFUSE.getCode());
        }
        corpVerifyAppOld.setCheckUserId(curUserId);
        corpVerifyAppOld.setCheckTime(Timestamp.valueOf(LocalDateTime.now()));
        this.update(corpVerifyAppOld, updateWrapper);
        corpOperationService.addCorpOperation(CorpOperationEnum.CO_VERIFY_CHECK, corpVerifyAppOld.getCorpId(), reqParam, curUserId, clientId);
        return Result.succeed("审核成功！");
    }

    @Override
    public CorpVerifyApp selectApplying(Long corpId) {
        //查询申请中的认证
        CorpVerifyApp corpVerifyApp = lambdaQuery().eq(CorpVerifyApp::getCorpId, corpId).eq(CorpVerifyApp::getStatus, FlowStatusEnum.APPLY.getCode()).one();
        return corpVerifyApp;
    }

    @Override
    public List<CorpVerifyApp> selectApplyingList(List<Long> corpIdList) {
        List<CorpVerifyApp> corpVerifyApps = new ArrayList<>();
        if (corpIdList != null && corpIdList.size() > 0) {
            corpVerifyApps = lambdaQuery().eq(CorpVerifyApp::getStatus, FlowStatusEnum.APPLY.getCode()).in(CorpVerifyApp::getCorpId, corpIdList).list();
        }
        return corpVerifyApps;
    }

    @Override
    public Map<Long, CorpVerifyApp> mapApplying(List<Long> corpIdList) {
        List<CorpVerifyApp> corpVerifyApps = selectApplyingList(corpIdList);
        Map<Long, CorpVerifyApp> map = new HashMap<>();
        if (corpVerifyApps != null && corpVerifyApps.size() > 0) {
            for (CorpVerifyApp corpVerifyApp : corpVerifyApps) {
                map.put(corpVerifyApp.getCorpId(), corpVerifyApp);
            }
        }
        return map;
    }

    @Override
    public ListWrapper<CorpVerifyAppDto> pageByFilter(CorpVerifyAppFilter corpVerifyAppFilter) {
        ListWrapper<CorpVerifyAppDto> listWrapper = new ListWrapper<>();
        if (corpVerifyAppFilter == null) {
            return listWrapper;
        }
        Page page = new Page(corpVerifyAppFilter.getPageNum(), corpVerifyAppFilter.getPageSize());
        QueryWrapper<CorpVerifyApp> queryWrapper = new QueryWrapper<>();
        if (LongUtil.isNotZero(corpVerifyAppFilter.getCorpId())) {
            queryWrapper.eq("corpid", corpVerifyAppFilter.getCorpId());
        }
        if (!StringUtil.isNullOrEmpty(corpVerifyAppFilter.getCorpName())) {
            queryWrapper.inSql("corpid", "select corpid from uas_corp_reg where corpname like '%" + corpVerifyAppFilter.getCorpName() + "%'");
        }
        if (corpVerifyAppFilter.getStatusList() != null && corpVerifyAppFilter.getStatusList().size() > 0) {
            queryWrapper.in("status", corpVerifyAppFilter.getStatusList());
        }
        if (!StringUtil.isNullOrEmpty(corpVerifyAppFilter.getLarName())) {
            queryWrapper.like("larname", corpVerifyAppFilter.getLarName());
        }
        if (corpVerifyAppFilter.getApplyUserId() != null && corpVerifyAppFilter.getApplyUserId() != 0L) {
            queryWrapper.eq("applyuserid", corpVerifyAppFilter.getApplyUserId());
        }
        if (!StrUtil.isBlank(corpVerifyAppFilter.getApplyUserName())) {
            queryWrapper.inSql("applyuserid", "select userid from uas_user_real where username like '%" + StrUtil.trim(corpVerifyAppFilter.getApplyUserName()) + "%'");
        }
        if (!StringUtil.isNullOrEmpty(corpVerifyAppFilter.getCity())) {
            queryWrapper.eq("city", corpVerifyAppFilter.getCity());
        } else if (!StringUtil.isNullOrEmpty(corpVerifyAppFilter.getProvince())) {
            queryWrapper.eq("province", corpVerifyAppFilter.getProvince());
        }
        if (corpVerifyAppFilter.getApplyTimeStart() != null) {
            queryWrapper.ge("applytime", corpVerifyAppFilter.getApplyTimeStart());
        }
        if (corpVerifyAppFilter.getApplyTimeEnd() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(corpVerifyAppFilter.getApplyTimeEnd());
            calendar.add(Calendar.DATE, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            queryWrapper.lt("applytime", calendar.getTime());
        }
        queryWrapper.orderByDesc("applytime");
        IPage<CorpVerifyApp> corpVerifyAppIPage = this.page(page, queryWrapper);
        Map<String, String> areaMap = this.cfgAreaService.selectAreaMap();
        List<Long> corpIdList = new ArrayList<>();
        List<CorpVerifyAppDto> dtoList = new ArrayList<>();
        if (corpVerifyAppIPage != null && corpVerifyAppIPage.getRecords() != null && corpVerifyAppIPage.getRecords().size() > 0) {
            List<Long> userIdList = new ArrayList<>();
            for (CorpVerifyApp corpVerifyApp : corpVerifyAppIPage.getRecords()) {
                corpIdList.add(corpVerifyApp.getCorpId());
                userIdList.add(corpVerifyApp.getApplyUserId());
            }
            Map<Long, CorpRegistry> registryMap = corpRegistryService.mapCorpIdAndRegistry(corpIdList);
            Map<Long, String> userIdAndNameMap = this.userRealService.mapIdAndName(userIdList);
            for (CorpVerifyApp corpVerifyApp : corpVerifyAppIPage.getRecords()) {
                String corpName = registryMap.get(corpVerifyApp.getCorpId()) == null ? "" : registryMap.get(corpVerifyApp.getCorpId()).getCorpName();
                String provinceName = areaMap.get(corpVerifyApp.getProvince()) == null ? "" : areaMap.get(corpVerifyApp.getProvince());
                String cityName = areaMap.get(corpVerifyApp.getCity()) == null ? "" : areaMap.get(corpVerifyApp.getCity());
                CorpVerifyAppDto corpVerifyAppDto = new CorpVerifyAppDto();
                BeanUtils.copyProperties(corpVerifyApp, corpVerifyAppDto);
                corpVerifyAppDto.setCorpName(corpName);
                corpVerifyAppDto.setRegion(provinceName + cityName);
                corpVerifyAppDto.setApplyUserName(userIdAndNameMap.get(corpVerifyApp.getApplyUserId()));
                dtoList.add(corpVerifyAppDto);
            }
        }
        return ListWrapper.<CorpVerifyAppDto>builder().list(dtoList).total(corpVerifyAppIPage.getTotal()).build();
    }

}
