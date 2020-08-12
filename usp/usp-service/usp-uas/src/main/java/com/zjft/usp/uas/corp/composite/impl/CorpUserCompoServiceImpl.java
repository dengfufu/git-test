package com.zjft.usp.uas.corp.composite.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.anyfix.service.AnyfixFeignService;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.device.service.DeviceFeignService;
import com.zjft.usp.uas.common.feign.dto.SysRoleUserDto;
import com.zjft.usp.uas.corp.composite.CorpUserCompoService;
import com.zjft.usp.uas.corp.dto.CorpUserDto;
import com.zjft.usp.uas.corp.model.CorpUser;
import com.zjft.usp.uas.corp.model.CorpUserTrace;
import com.zjft.usp.uas.corp.service.CorpUserService;
import com.zjft.usp.uas.corp.service.CorpUserTraceService;
import com.zjft.usp.uas.right.composite.SysRoleCompoService;
import com.zjft.usp.uas.right.composite.SysRoleUserCompoService;
import com.zjft.usp.uas.right.model.SysRole;
import com.zjft.usp.uas.right.service.SysRoleUserService;
import com.zjft.usp.uas.user.model.UserReal;
import com.zjft.usp.uas.user.service.UserInfoService;
import com.zjft.usp.uas.user.service.UserRealService;
import com.zjft.usp.uas.user.service.UserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zgpi
 * @version 1.0
 * @date 2019/12/18 10:45
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CorpUserCompoServiceImpl implements CorpUserCompoService {

    @Autowired
    private UserRegisterService userRegisterService;
    @Autowired
    private CorpUserService corpUserService;
    @Autowired
    private SysRoleUserService sysRoleUserService;
    @Autowired
    private SysRoleCompoService sysRoleCompoService;
    @Autowired
    private SysRoleUserCompoService sysRoleUserCompoService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserRealService userRealService;
    @Resource
    private AnyfixFeignService anyfixFeignService;
    @Resource
    private DeviceFeignService deviceFeignService;

    @Autowired
    private CorpUserTraceService corpUserTraceService;


    /**
     * 获得企业人员详情
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/4 10:03
     **/
    @Override
    public CorpUserDto findCorpUserDetail(Long userId, Long corpId) {
        CorpUser corpUser = corpUserService.getOne(new QueryWrapper<CorpUser>()
                .eq("userid", userId).eq("corpid", corpId));
        CorpUserDto corpUserDto = new CorpUserDto();
        corpUserDto.setUserId(userId);
        corpUserDto.setCorpId(corpId);
        if (corpUser != null) {
            List<Long> userIdList = new ArrayList<>();
            userIdList.add(userId);
            UserReal userReal = userRealService.getById(userId);
            if (userReal != null) {
                corpUserDto.setUserName(userReal.getUserName());
            }

            Map<Long, String> userIdAndMobileMap = this.userInfoService.mapUserIdAndMobileByUserIdList(userIdList);
            corpUserDto.setMobile(userIdAndMobileMap.get(corpUserDto.getUserId()));

            Map<Long, List<SysRole>> userAndRoleListMap = sysRoleCompoService.mapUserIdAndRoleList(corpId, userIdList);
            Object sysRoleListObj = userAndRoleListMap.get(corpUserDto.getUserId());
            List<SysRoleUserDto> sysRoleList = JsonUtil.parseArray(JsonUtil.toJson(sysRoleListObj), SysRoleUserDto.class);
            if (CollectionUtil.isNotEmpty(sysRoleList)) {
                List<String> roleNameList = sysRoleList.stream()
                        .map(e -> e.getRoleName()).collect(Collectors.toList());
                corpUserDto.setRoleNames(CollectionUtil.join(roleNameList, ","));
            }

            Result<Map<Long, String>> userAndBranchNamesMapResult = anyfixFeignService.mapUserIdAndServiceBranchNames(JsonUtil.toJson(userIdList));
            if (Result.isSucceed(userAndBranchNamesMapResult)) {
                Map<Long, String> userAndBranchNamesMap = userAndBranchNamesMapResult.getData();
                userAndBranchNamesMap = userAndBranchNamesMap == null ? new HashMap<>() : userAndBranchNamesMap;
                String branchNames = StrUtil.trimToEmpty(userAndBranchNamesMap.get(corpUserDto.getUserId()));
                int pos = branchNames.indexOf(" ");
                if (pos > 0) {
                    corpUserDto.setServiceBranchNames(branchNames.substring(pos + 1));
                } else {
                    corpUserDto.setServiceBranchNames(branchNames);
                }
            }
        }
        return corpUserDto;
    }

    /**
     * 添加企业人员
     *
     * @param corpUserDto
     * @return
     * @author zgpi
     * @date 2019/12/18 10:43
     **/
    @Override
    public void addCorpUser(CorpUserDto corpUserDto, Long currentUserId, String clientId, ReqParam reqParam) {
        Long userId = userRegisterService.registryByCorpAdmin(corpUserDto.getMobile(), corpUserDto.getUserName());
        corpUserDto.setUserId(userId);
        corpUserService.addCorpUser(corpUserDto);
        sysRoleUserCompoService.addRoleUser(reqParam.getCorpId(), corpUserDto.getUserId(), corpUserDto.getRoleIdList());

        // 增加人员流动备份(入职)
        CorpUserTrace corpUserTrace = new CorpUserTrace();
        corpUserTrace
                .setCorpId(corpUserDto.getCorpId())
                .setUserId(corpUserDto.getUserId())
                .setOperate(1)
                .setOperator(currentUserId)
                .setClientId(clientId)
                .setOperateTime(DateUtil.date());
        this.corpUserTraceService.save(corpUserTrace);
    }

    /**
     * 修改企业人员
     *
     * @param corpUserDto
     * @return
     * @author zgpi
     * @date 2019/12/18 10:44
     **/
    @Override
    public void updateCorpUser(CorpUserDto corpUserDto) {
        sysRoleUserCompoService.updateRoleUser(corpUserDto.getUserId(), corpUserDto.getCorpId(), corpUserDto.getRoleIdList());
        userRegisterService.updateByCorpAdmin(corpUserDto.getUserId(), corpUserDto.getUserName());
        userInfoService.updateByMobile(corpUserDto);
        corpUserService.updateCorpUser(corpUserDto);
    }

    /**
     * 删除企业人员
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/12/18 10:44
     **/
    @Override
    public void delCorpUser(Long userId, Long corpId, Long currentUserId, String clientId) {
        // 1. 删除角色形象(sys_role_user)
        sysRoleUserCompoService.delRoleUser(userId, corpId);
        // 2. 删除企业用户(uas_corp_user)
        corpUserService.delCorpUser(corpId, userId, currentUserId, clientId);
        // 3. 删除网点人员(service_branch_user,device_branch_user)
        anyfixFeignService.delBranchUser(userId, corpId, currentUserId, clientId);
        // 4. 删除设备服务信息(device_service)
        deviceFeignService.delDeviceService(userId, corpId, currentUserId, clientId);
    }
}
