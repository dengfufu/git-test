package com.zjft.usp.uas.corp.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.service.AnyfixFeignService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.baseinfo.service.CfgAreaService;
import com.zjft.usp.uas.common.feign.dto.SysRoleUserDto;
import com.zjft.usp.uas.corp.dto.CorpDto;
import com.zjft.usp.uas.corp.dto.CorpUserDto;
import com.zjft.usp.uas.corp.dto.CorpUserInfoDto;
import com.zjft.usp.uas.corp.enums.FlowStatusEnum;
import com.zjft.usp.uas.corp.filter.CorpUserFilter;
import com.zjft.usp.uas.corp.mapper.CorpUserMapper;
import com.zjft.usp.uas.corp.model.*;
import com.zjft.usp.uas.corp.service.*;
import com.zjft.usp.uas.right.composite.SysRoleCompoService;
import com.zjft.usp.uas.right.model.SysRole;
import com.zjft.usp.uas.right.model.SysTenant;
import com.zjft.usp.uas.right.service.SysTenantService;
import com.zjft.usp.uas.user.model.UserReal;
import com.zjft.usp.uas.user.service.UserInfoService;
import com.zjft.usp.uas.user.service.UserRealService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 企业用户实现类
 *
 * @author canlei
 * @date 2019-08-04
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CorpUserServiceImpl extends ServiceImpl<CorpUserMapper, CorpUser> implements CorpUserService {

    @Autowired
    private CorpUserService corpUserService;
    @Autowired
    private CfgAreaService cfgAreaService;
    @Autowired
    private CorpRegistryService corpRegistryService;
    @Autowired
    private CorpVerifyService corpVerifyService;
    @Autowired
    private CorpVerifyAppService corpVerifyAppService;
//    @Autowired
//    private CorpAdminService corpAdminService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserRealService userRealService;
    @Autowired
    private CorpUserAppService corpUserAppService;
    @Autowired
    private SysRoleCompoService sysRoleCompoService;

    @Autowired
    private CorpUserTraceService corpUserTraceService;
    @Autowired
    private SysTenantService sysTenantService;

    @Resource
    private AnyfixFeignService anyfixFeignService;

    @Override
    public List<CorpDto> listCorpInfo(CorpUserDto corpUserDto) {
        List<CorpUser> corpUserList = corpUserService.lambdaQuery()
                .eq(CorpUser::getUserId, corpUserDto.getUserId()).list();
        List<CorpDto> corpList = new ArrayList<>();
        List<Long> corpIdList = new ArrayList<>();
        if (corpUserList != null && corpUserList.size() > 0) {
            for (CorpUser corpUser : corpUserList) {
                corpIdList.add(corpUser.getCorpId());
            }
        }
        Map<String, String> regionMap = cfgAreaService.selectAreaMap();
        if (corpIdList != null && corpIdList.size() > 0) {
            Map<Long, CorpRegistry> corpRegistryMap = corpRegistryService.mapCorpIdAndRegistry(corpIdList);
            Map<Long, CorpVerify> corpVerifyMap = corpVerifyService.mapCorpIdAndVerify(corpIdList);
            Map<Long, CorpVerifyApp> corpVerifyAppMap = corpVerifyAppService.mapApplying(corpIdList);
            for (Long corpId : corpIdList) {
                CorpDto corpDto = new CorpDto();
                CorpRegistry corpRegistry = corpRegistryMap.get(corpId);
                CorpVerify corpVerify = corpVerifyMap.get(corpId);
                CorpVerifyApp corpVerifyApp = corpVerifyAppMap.get(corpId);
                if (corpVerify != null) {
                    corpDto.setVerifyStatus(true);
                }
                if (corpVerifyApp != null) {
                    corpDto.setVerifyAppStatus(true);
                }
                if (corpRegistry != null) {
                    BeanUtils.copyProperties(corpRegistry, corpDto, "corpId", "txId");
                    corpDto.setCorpId(corpRegistry.getCorpId());
                    corpDto.setLogoImg(corpRegistry.getLogoImg());
                    corpDto.areaName(regionMap.get(corpDto.getProvince()), regionMap.get(corpDto.getCity()), regionMap.get(corpDto.getDistrict()));
                    corpDto.setVerify(corpRegistry.getVerify());
                    corpList.add(corpDto);
                }
            }
        }
        return corpList;
    }

    @Override
    public ListWrapper<CorpUserDto> query(CorpUserFilter corpUserFilter) {
        Page page = new Page(corpUserFilter.getPageNum(), corpUserFilter.getPageSize());
        SysTenant sysTenant = sysTenantService.getById(corpUserFilter.getCorpId());
        if (sysTenant != null && "Y".equalsIgnoreCase(sysTenant.getServiceProvider())) {
            if (LongUtil.isNotZero(corpUserFilter.getServiceBranch())
                    || StrUtil.isNotBlank(corpUserFilter.getProvince())) {
                String json = JsonUtil.toJsonString("province", corpUserFilter.getProvince(),
                        "branchId", corpUserFilter.getServiceBranch());
                Result<List<Long>> branchUserIdListResult = anyfixFeignService.listServiceBranchUserIdByFilter(json);
                if (branchUserIdListResult != null && branchUserIdListResult.getCode() == Result.SUCCESS) {
                    List<Long> branchUserIdList = branchUserIdListResult.getData();
                    if (CollectionUtil.isEmpty(branchUserIdList)) {
                        return ListWrapper.<CorpUserDto>builder()
                                .list(null).total(page.getTotal()).build();
                    }
                    corpUserFilter.setUserIdList(branchUserIdList);
                }
            }
        }
        List<CorpUserDto> corpUserInfoDtoList = this.baseMapper.queryCorpUser(page, corpUserFilter);
//        Map<Long, CorpAdmin> adminMap = corpAdminService.mapAdminByCorpId(corpUserFilter.getCorpId());
        List<CorpUserDto> corpUserDtoList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(corpUserInfoDtoList)) {
            List<Long> userIdList = corpUserInfoDtoList.stream().map(e -> e.getUserId()).collect(Collectors.toList());
            Map<Long, String> userIdAndMobileMap = this.userInfoService.mapUserIdAndMobileByUserIdList(userIdList);
            Map<Long, List<SysRole>> userAndRoleListMap = sysRoleCompoService.mapUserIdAndRoleList(corpUserFilter.getCorpId(), userIdList);
            Result<Map<Long, String>> userAndBranchNamesMapResult = anyfixFeignService.mapUserIdAndServiceBranchNames(JsonUtil.toJson(userIdList));
            Map<Long, String> userAndBranchNamesMap = new HashMap<>();
            if (userAndBranchNamesMapResult != null && userAndBranchNamesMapResult.getCode() == Result.SUCCESS) {
                userAndBranchNamesMap = userAndBranchNamesMapResult.getData();
            }
            userAndBranchNamesMap = userAndBranchNamesMap == null ? new HashMap<>() : userAndBranchNamesMap;
            for (CorpUserDto corpUserDto : corpUserInfoDtoList) {
//                corpUserDto.setAdmin(adminMap.get(corpUserDto.getUserId()) != null);
                corpUserDto.setMobile(userIdAndMobileMap.get(corpUserDto.getUserId()));
                String branchNames = StrUtil.trimToEmpty(userAndBranchNamesMap.get(corpUserDto.getUserId()));
                int pos = branchNames.indexOf(" ");
                if (pos > 0) {
                    corpUserDto.setProvinceName(branchNames.substring(0, pos));
                    corpUserDto.setServiceBranchNames(branchNames.substring(pos + 1));
                } else {
                    corpUserDto.setServiceBranchNames(branchNames);
                }

                Object sysRoleListObj = userAndRoleListMap.get(corpUserDto.getUserId());
                List<SysRoleUserDto> sysRoleList = JsonUtil.parseArray(JsonUtil.toJson(sysRoleListObj), SysRoleUserDto.class);
                if (CollectionUtil.isNotEmpty(sysRoleList)) {
                    List<Long> roleIdList = sysRoleList.stream()
                            .map(e -> e.getRoleId()).collect(Collectors.toList());
                    List<String> roleNameList = sysRoleList.stream()
                            .map(e -> e.getRoleName()).collect(Collectors.toList());
                    corpUserDto.setRoleIdList(roleIdList);
                    corpUserDto.setRoleNames(CollectionUtil.join(roleNameList, ","));
                }
                corpUserDtoList.add(corpUserDto);
            }
        }
        return ListWrapper.<CorpUserDto>builder().list(corpUserDtoList).total(page.getTotal()).build();
    }

    @Override
    public ListWrapper<CorpUserDto> queryCorpUser(CorpUserFilter corpUserFilter) {
        Page page = new Page(corpUserFilter.getPageNum(), corpUserFilter.getPageSize());
        List<CorpUserDto> corpUserInfoDtoList = this.baseMapper.selectCorpUser(page, corpUserFilter);
        List<CorpUserDto> corpUserDtoList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(corpUserInfoDtoList)) {
            List<Long> userIdList = corpUserInfoDtoList.stream().map(e -> e.getUserId()).collect(Collectors.toList());
            Map<Long, String> userIdAndMobileMap = this.userInfoService.mapUserIdAndMobileByUserIdList(userIdList);
            Map<Long, List<SysRole>> userAndRoleListMap = sysRoleCompoService.mapUserIdAndRoleList(corpUserFilter.getCorpId(), userIdList);
            Result<Map<Long, String>> userAndBranchNamesMapResult = anyfixFeignService.mapUserIdAndServiceBranchNames(JsonUtil.toJson(userIdList));
            Map<Long, String> userAndBranchNamesMap = new HashMap<>();
            if (userAndBranchNamesMapResult != null && userAndBranchNamesMapResult.getCode() == Result.SUCCESS) {
                userAndBranchNamesMap = userAndBranchNamesMapResult.getData();
            }
            userAndBranchNamesMap = userAndBranchNamesMap == null ? new HashMap<>() : userAndBranchNamesMap;
            for (CorpUserDto corpUserDto : corpUserInfoDtoList) {
                corpUserDto.setMobile(userIdAndMobileMap.get(corpUserDto.getUserId()));
                String branchNames = StrUtil.trimToEmpty(userAndBranchNamesMap.get(corpUserDto.getUserId()));
                int pos = branchNames.indexOf(" ");
                if (pos > 0) {
                    corpUserDto.setProvinceName(branchNames.substring(0, pos));
                    corpUserDto.setServiceBranchNames(branchNames.substring(pos + 1));
                } else {
                    corpUserDto.setServiceBranchNames(branchNames);
                }

                Object sysRoleListObj = userAndRoleListMap.get(corpUserDto.getUserId());
                List<SysRoleUserDto> sysRoleList = JsonUtil.parseArray(JsonUtil.toJson(sysRoleListObj), SysRoleUserDto.class);
                if (CollectionUtil.isNotEmpty(sysRoleList)) {
                    List<Long> roleIdList = sysRoleList.stream()
                            .map(e -> e.getRoleId()).collect(Collectors.toList());
                    List<String> roleNameList = sysRoleList.stream()
                            .map(e -> e.getRoleName()).collect(Collectors.toList());
                    corpUserDto.setRoleIdList(roleIdList);
                    corpUserDto.setRoleNames(CollectionUtil.join(roleNameList, ","));
                }
                corpUserDtoList.add(corpUserDto);
            }
        }
        return ListWrapper.<CorpUserDto>builder().list(corpUserDtoList).total(page.getTotal()).build();
    }

    @Override
    public boolean ifUserInCorp(long userId, long corpId) {
        CorpUser corpUserOld = lambdaQuery().eq(CorpUser::getCorpId, corpId).eq(CorpUser::getUserId, userId).one();
        return corpUserOld != null;
    }

    @Override
    public boolean ifAccountInCorp(String account, long corpId, long userId) {
        CorpUser corpUserOld = lambdaQuery().eq(CorpUser::getCorpId, corpId).eq(CorpUser::getAccount, account).one();
        if (corpUserOld != null && !corpUserOld.getUserId().equals(userId)) {
            return true;
        }
        return false;
    }

    @Override
    public void addCorpUser(long userId, long corpId, String userName) {
        CorpUser corpUser = new CorpUser();
        corpUser.setUserId(userId);
        corpUser.setCorpId(corpId);
        corpUser.setAddTime(DateUtil.date());
        save(corpUser);
    }

    @Override
    public Map<Long, CorpUser> mapUserIdAndCorpUser(Long corpId) {
        Map<Long, CorpUser> map = new HashMap<>();
        if (corpId == null || corpId == 0L) {
            return map;
        }
        List<CorpUser> corpUsers = this.list(new QueryWrapper<CorpUser>().eq("corpid", corpId));
        if (corpUsers != null && corpUsers.size() > 0) {
            for (CorpUser corpUser : corpUsers) {
                map.put(corpUser.getUserId(), corpUser);
            }
        }
        return map;
    }

    @Override
    public Map<Long, String> mapUserIdAndName(Long corpId) {
        Map<Long, String> map = new HashMap<>();
        if (corpId == null || corpId == 0L) {
            return map;
        }
        List<CorpUser> corpUsers = this.list(new QueryWrapper<CorpUser>().eq("corpid", corpId));
        if (CollectionUtil.isNotEmpty(corpUsers)) {
            List<Long> userIdList = corpUsers.stream().map(e -> e.getUserId()).collect(Collectors.toList());
            Map<Long, String> userMap = userRealService.mapIdAndName(userIdList);
            for (CorpUser corpUser : corpUsers) {
                map.put(corpUser.getUserId(), StrUtil.trimToEmpty(userMap.get(corpUser.getUserId())));
            }
        }
        return map;
    }

    @Override
    public Map<Long, CorpUser> mapCorpIdByUser(Long userId) {
        Map<Long, CorpUser> map = new HashMap<>();
        if (userId == null || userId == 0L) {
            return map;
        }
        List<CorpUser> corpUsers = this.list(new QueryWrapper<CorpUser>().eq("userid", userId));
        if (corpUsers != null && corpUsers.size() > 0) {
            for (CorpUser corpUser : corpUsers) {
                map.put(corpUser.getCorpId(), corpUser);
            }
        }
        return map;
    }

    @Override
    public List<Long> listCorpIdByUserId(Long userId) {
        List<Long> corpIdList = new ArrayList<>();
        if (LongUtil.isZero(userId)) {
            return corpIdList;
        }
        List<CorpUser> corpUsers = this.list(new QueryWrapper<CorpUser>().eq("userid", userId));
        if (corpUsers != null && corpUsers.size() > 0) {
            for (CorpUser corpUser : corpUsers) {
                corpIdList.add(corpUser.getCorpId());
            }
        }
        return corpIdList;
    }
    /**
     * 根据userId查询企业列表
     * @date 2020/5/27
     * @param userIdList
     * @return java.util.List<java.lang.Long>
     */
    @Override
    public Map<Long, List<Long>> listCorpIdListByUserId(List<Long> userIdList) {
        Map<Long, List<Long>> corpMap = new HashMap<>();
        List<Long>  corpIdList;
        if (CollectionUtil.isEmpty(userIdList)) {
            return corpMap;
        }
        List<CorpUser> corpUsers = this.list(new QueryWrapper<CorpUser>().in("userid", userIdList));
        if (CollectionUtil.isNotEmpty(corpUsers)) {
            for (Long userId : userIdList) {
                corpIdList = new ArrayList<>();
                for (CorpUser corpUser : corpUsers) {
                    if(corpUser.getUserId().equals(userId)){
                        corpIdList.add(corpUser.getCorpId());
                    }
                }
                corpMap.put(userId,corpIdList);
            }
        }


        return corpMap;
    }

    /**
     * 根据企业查询人员编号列表
     *
     * @param corpId
     * @return
     */
    @Override
    public List<Long> listUserIdByCorpId(Long corpId) {
        List<Long> userIdList = new ArrayList<>();
        if (LongUtil.isZero(corpId)) {
            return userIdList;
        }
        List<CorpUser> corpUserList = this.list(new QueryWrapper<CorpUser>().eq("corpid", corpId));
        if (CollectionUtil.isNotEmpty(corpUserList)) {
            for (CorpUser corpUser : corpUserList) {
                userIdList.add(corpUser.getUserId());
            }
        }
        return userIdList;
    }

    @Override
    public List<CorpUserDto> getCorpUserByCorpId(CorpUserDto corpUserDto, ReqParam reqParam) {
        List<CorpUserDto> corpUserDtoList = new ArrayList<>();
        QueryWrapper<CorpUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("corpid", corpUserDto.getCorpId());
        // 只查询显示的人员
        queryWrapper.eq("hidden", 0);
        List<CorpUser> list = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            List<Long> userIdList = list.stream().map(e -> e.getUserId()).collect(Collectors.toList());
            Map<Long, String> userMap = userRealService.mapIdAndName(userIdList);
            Map<Long, String> userIdAndMobileMap = userInfoService.mapUserIdAndMobileByUserIdList(userIdList);
            CorpUserDto entity;
            for (CorpUser corpUser : list) {
                entity = new CorpUserDto();
                BeanUtils.copyProperties(corpUser, entity);
                entity.setMobile(StrUtil.trimToEmpty(userIdAndMobileMap.get(entity.getUserId())));
                entity.setUserName(StrUtil.trimToEmpty(userMap.get(corpUser.getUserId())));
                corpUserDtoList.add(entity);
            }
        }
        return corpUserDtoList;
    }

    @Override
    public void addCorpUser(CorpUserDto corpUserDto) {
        if (LongUtil.isZero(corpUserDto.getUserId())) {
            throw new AppException("用户编号不能为空！");
        }
        if (LongUtil.isZero(corpUserDto.getCorpId())) {
            throw new AppException("企业编号不能为空！");
        }
        CorpUser corpUser = this.getOne(new QueryWrapper<CorpUser>().eq("corpid", corpUserDto.getCorpId())
                .eq("userid", corpUserDto.getUserId()));
        if (corpUser != null) {
            throw new AppException("该用户已属于企业，无需重复添加！");
        }
        if (!StringUtils.isEmpty(corpUserDto.getAccount()) &&
                corpUserService.ifAccountInCorp(corpUserDto.getAccount(), corpUserDto.getCorpId(), corpUserDto.getUserId())) {
            throw new AppException("该企业账号已经被绑定！");
        }
        CorpUser corpUserNew = new CorpUser();
        BeanUtils.copyProperties(corpUserDto, corpUserNew);
        corpUserNew.setAddTime(DateUtil.date());
        corpUserNew.insert();
        CorpUserApp corpUserApp = this.corpUserAppService.getApplyingApp(corpUserNew.getUserId(), corpUserNew.getCorpId());
        if (corpUserApp != null) {
            corpUserApp.setStatus(FlowStatusEnum.PASS.getCode());
            corpUserApp.setCheckNote("管理员添加人员，自动审核通过");
            //此处还需设置审核人
            this.corpUserAppService.update(corpUserApp, new UpdateWrapper<CorpUserApp>().eq("corpid", corpUserApp.getCorpId())
                    .eq("txid", corpUserApp.getTxId()));
        }
    }

    @Override
    public void updateCorpUser(CorpUserDto corpUserDto) {
        if (LongUtil.isZero(corpUserDto.getUserId())) {
            throw new AppException("用户编号不能为空！");
        }
        if (LongUtil.isZero(corpUserDto.getCorpId())) {
            throw new AppException("企业编号不能为空！");
        }
        if (!StringUtils.isEmpty(corpUserDto.getAccount()) &&
                corpUserService.ifAccountInCorp(corpUserDto.getAccount(), corpUserDto.getCorpId(), corpUserDto.getUserId())) {
            throw new AppException("该企业账号已经被绑定！");
        }
        CorpUser corpUser = this.getOne(new QueryWrapper<CorpUser>().eq("corpid", corpUserDto.getCorpId()).eq("userid", corpUserDto.getUserId()));
        corpUser.setAccount(corpUserDto.getAccount());
        this.update(corpUser, new UpdateWrapper<CorpUser>().eq("corpid", corpUserDto.getCorpId()).eq("userid", corpUserDto.getUserId()));
    }

    /**
     * 紫金设置管理员，自动隐藏
     * @date 2020/6/8
     * @param userId corpId
     * @return void
     */
    @Override
    public void setAdmin(long userId, long corpId) {
        if (LongUtil.isZero(userId)) {
            throw new AppException("用户编号不能为空！");
        }
        if (LongUtil.isZero(corpId)) {
            throw new AppException("企业编号不能为空！");
        }
        CorpUser corpUser = this.getOne(new QueryWrapper<CorpUser>().eq("corpid", corpId)
                .eq("userid", userId));
        corpUser.setHidden(1L);
        this.update(corpUser, new UpdateWrapper<CorpUser>().eq("corpid", corpId).eq("userid", userId));
    }

    /**
     * 隐藏企业员工
     * @date 2020/6/8
     * @param corpUserFilter
     * @return void
     */
    @Override
    public void hiddenCorpUser(CorpUserFilter corpUserFilter) {
        if (StringUtil.isNullOrEmpty(corpUserFilter.getUserId())) {
            throw new AppException("用户编号不能为空！");
        }
        if (LongUtil.isZero(corpUserFilter.getCorpId())) {
            throw new AppException("企业编号不能为空！");
        }
        // 修改只改部分属性
        UpdateWrapper<CorpUser> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("corpid", corpUserFilter.getCorpId());
        updateWrapper.eq("userid", corpUserFilter.getUserId());
        updateWrapper.set("hidden", corpUserFilter.getHidden());
        corpUserService.update(updateWrapper);
    }

    /**
     * 删除企业员工
     *
     * @param corpId
     * @param userId
     * @return
     * @author zgpi
     * @date 2019/11/28 09:59
     **/
    @Override
    public void delCorpUser(Long corpId, Long userId, Long currentUserId, String clientId) {
        if (LongUtil.isZero(userId)) {
            throw new AppException("用户编号不能为空！");
        }
        if (LongUtil.isZero(corpId)) {
            throw new AppException("企业编号不能为空！");
        }
        // 如果是企业管理员，不删除
//        boolean isAdmin = corpAdminService.isCorpAdmin(userId, corpId);
//        if (isAdmin) {
//            throw new AppException("是企业管理员，不能删除！");
//        }
//        boolean canDelUser = false;
//        // 该用户从未注册、登录过，且未实名认证，只在一家企业，同时删除用户信息
//        UserReal userReal = userRealService.getById(userId);
//        // 未认证
//        if (userReal == null || userReal != null && userReal.getVerified() != 1) {
//            canDelUser = true;
//        } else {
//            canDelUser = false;
//        }
//        List<CorpUser> corpUserList = this.list(new QueryWrapper<CorpUser>().eq("userid", userId));
//        if (CollectionUtil.isNotEmpty(corpUserList) && corpUserList.size() == 1
//                && corpUserList.get(0).getUserId().equals(userId)) {
//            canDelUser = true;
//        } else {
//            canDelUser = false;
//        }
//        boolean hasRecord = userDeviceService.findHasRecord(userId);
//        if (!hasRecord) {
//            canDelUser = true;
//        } else {
//            canDelUser = false;
//        }
//        if (canDelUser) {
//            userInfoService.delUserInfo(userId);
//            userMobileService.delUserMobile(userId);
//            userRealService.delUserReal(userId);
//        }
        CorpUser corpUser = this.getOne(new QueryWrapper<CorpUser>().eq("corpid", corpId)
                .eq("userid", userId));

        this.remove(new UpdateWrapper<CorpUser>().eq("corpid", corpId)
                .eq("userid", userId));

        if(corpUser.getHidden() != 1){
            // 增加人员流动备份
            CorpUserTrace corpUserTrace = new CorpUserTrace();
            corpUserTrace
                    .setCorpId(corpId)
                    .setUserId(userId)
                    .setOperate(2)
                    .setOperator(currentUserId)
                    .setClientId(clientId)
                    .setOperateTime(DateUtil.date());
            this.corpUserTraceService.save(corpUserTrace);
        }
    }

    @Override
    public Map<Long, String> mapUserIdAndNameByCorpIdList(List<Long> corpIdList) {
        Map<Long, String> map = new HashMap<>();
        if (corpIdList == null || corpIdList.size() == 0) {
            return map;
        }
        List<CorpUser> corpUsers = this.list(new QueryWrapper<CorpUser>().in("corpid", corpIdList));
        if (CollectionUtil.isNotEmpty(corpUsers)) {
            List<Long> userIdList = corpUsers.stream().map(e -> e.getUserId()).collect(Collectors.toList());
            Map<Long, String> userMap = userRealService.mapIdAndName(userIdList);
            for (CorpUser corpUser : corpUsers) {
                map.put(corpUser.getUserId(), StrUtil.trimToEmpty(userMap.get(corpUser.getUserId())));
            }
        }
        return map;
    }

    /**
     * 根据人员编号和企业编号获得企业人员信息
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/10/30 10:04
     **/
    @Override
    public CorpUser findCorpUserByUserIdAndCorpId(Long userId, Long corpId) {
        CorpUser corpUser = new CorpUser();
        if (LongUtil.isNotZero(corpId)) {
            corpUser = this.getOne(new QueryWrapper<CorpUser>()
                    .eq("userid", userId)
                    .eq("corpid", corpId));
        } else {
            UserReal userReal = userRealService.getById(userId);
            if (userReal != null) {
                corpUser.setUserId(userId);
            }
        }
        return corpUser;
    }

    @Override
    public Map<Long, CorpUserInfoDto> mapCorpUserInfoByUserIdList(List<Long> userIdList, Long corpId) {
        Map<Long, CorpUserInfoDto> corpUserInfoDtoMap = new HashMap<>();
        if (userIdList == null || userIdList.isEmpty()) {
            return corpUserInfoDtoMap;
        }
        List<CorpUserInfoDto> corpUserInfoDtoList = this.baseMapper.queryCorpUserInfoByUserIdList(userIdList, corpId);
        for (CorpUserInfoDto corpUserInfoDto : corpUserInfoDtoList) {
            corpUserInfoDtoMap.put(corpUserInfoDto.getUserId(), corpUserInfoDto);
        }
        return corpUserInfoDtoMap;
    }

    /**
     * 模糊查询企业人员列表
     *
     * @param corpUserFilter
     * @return
     * @author zgpi
     * @date 2019/11/19 08:40
     **/
    @Override
    public List<CorpUserDto> matchCorpUser(CorpUserFilter corpUserFilter) {
        return this.baseMapper.matchCorpUser(corpUserFilter);
    }

    /**
     * 模糊查询企业人员姓名列表
     *
     * @param corpUserFilter
     * @return
     **/
    @Override
    public List<CorpUserDto> matchCorpUserNames(CorpUserFilter corpUserFilter) {
        return this.baseMapper.matchCorpUserNames(corpUserFilter);
    }

    /**
     * 模糊查询企业人员列表（用于筛选）
     * @date 2020/6/9
     * @param corpUserFilter
     * @return java.util.List<com.zjft.usp.uas.corp.dto.CorpUserDto>
     */
    @Override
    public List<CorpUserDto> matchCorpUserByCorp(CorpUserFilter corpUserFilter) {
        return this.baseMapper.matchCorpUserByCorp(corpUserFilter);
    }


    @Override
    public CorpUserInfoDto getUserInfoByMobile(String mobile) {
        Assert.notEmpty(mobile, "手机号不能为空");
        CorpUserInfoDto userInfo = this.baseMapper.selectUserNameMobileByMobile(mobile);
        return userInfo;
    }

    /**
     * 根据企业编号和人员编号列表获得企业员工账号列表
     *
     * @param corpUserFilter
     * @return
     * @author zgpi
     * @date 2020/4/7 20:56
     */
    @Override
    public List<Long> listUserIdByAccountList(CorpUserFilter corpUserFilter) {
        if (CollectionUtil.isEmpty(corpUserFilter.getAccountList())
                || LongUtil.isZero(corpUserFilter.getCorpId())) {
            return new ArrayList<>();
        }
        List<CorpUser> corpUserList = this.list(new QueryWrapper<CorpUser>()
                .eq("corpid", corpUserFilter.getCorpId())
                .in("account", corpUserFilter.getAccountList()));
        if (CollectionUtil.isNotEmpty(corpUserList)) {
            return corpUserList.stream().map(e -> e.getUserId()).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * 用户是否是企业人员
     *
     * @param corpId
     * @param userId
     * @return
     * @author zgpi
     * @date 2020/5/13 09:59
     **/
    @Override
    public Boolean ifCorpUser(Long corpId, Long userId) {
        CorpUser corpUser = this.getOne(new QueryWrapper<CorpUser>().eq("corpid", corpId)
                .eq("userid", userId));
        if (corpUser != null) {
            return true;
        }
        return false;
    }

}
