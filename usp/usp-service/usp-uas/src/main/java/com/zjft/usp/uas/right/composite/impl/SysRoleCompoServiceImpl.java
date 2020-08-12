package com.zjft.usp.uas.right.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.corp.service.CorpUserService;
import com.zjft.usp.uas.right.composite.RightRedisCompoService;
import com.zjft.usp.uas.right.composite.SysRoleCompoService;
import com.zjft.usp.uas.right.dto.SysRoleDto;
import com.zjft.usp.uas.right.dto.SysRoleRightScopeDto;
import com.zjft.usp.uas.right.dto.SysRoleUserDto;
import com.zjft.usp.uas.right.enums.AppEnum;
import com.zjft.usp.uas.right.filter.SysRoleFilter;
import com.zjft.usp.uas.right.mapper.SysRoleMapper;
import com.zjft.usp.uas.right.model.*;
import com.zjft.usp.uas.right.service.*;
import com.zjft.usp.uas.user.dto.UserRealDto;
import com.zjft.usp.uas.user.service.UserRealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色聚合服务实现类
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/12/13 15:44
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleCompoServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleCompoService {

    @Autowired
    private SysRoleUserService sysRoleUserService;
    @Autowired
    private SysRoleRightService sysRoleRightService;
    @Autowired
    private SysRoleAppService sysRoleAppService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRightService sysRightService;
    @Autowired
    private CorpUserService corpUserService;
    @Autowired
    private SysRoleRightScopeService sysRoleRightScopeService;
    @Autowired
    private SysRoleRightScopeDetailService sysRoleRightScopeDetailService;

    @Autowired
    private RightRedisCompoService rightRedisCompoService;
    @Autowired
    private UserRealService userRealService;

    /**
     * 分页查询角色
     *
     * @param sysRoleFilter
     * @return
     * @author zgpi
     * @date 2019/12/14 14:32
     **/
    @Override
    public ListWrapper<SysRoleDto> query(SysRoleFilter sysRoleFilter, ReqParam reqParam) {
        Page page = new Page(sysRoleFilter.getPageNum(), sysRoleFilter.getPageSize());
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("corp_id", sysRoleFilter.getCorpId());
        if (StrUtil.isNotBlank(sysRoleFilter.getRoleName())) {
            queryWrapper.like("role_name", StrUtil.trimToEmpty(sysRoleFilter.getRoleName()));
        }
        if (StrUtil.isNotBlank(sysRoleFilter.getEnabled())) {
            queryWrapper.eq("enabled", StrUtil.trimToEmpty(sysRoleFilter.getEnabled()).toUpperCase());
        }
//        // 临时去除颖网账号可以看到的系统管理员
//        Long tempCorpId = 1229327791726825473L;
//        if (tempCorpId.equals(reqParam.getCorpId())) {
//            queryWrapper.ne("sys_type", 1);
//        }

        IPage<SysRole> sysRoleIPage = sysRoleService.page(page, queryWrapper);
        List<SysRoleDto> sysRoleDtoList = new ArrayList<>();
        List<SysRole> sysRoleList = sysRoleIPage.getRecords();
        Map<Long, String> roleAndAppsMap = sysRoleAppService.mapRoleAndApps(sysRoleFilter.getCorpId());
        SysRoleDto sysRoleDto;
        StringBuilder appNames;
        for (SysRole sysRole : sysRoleList) {
            sysRoleDto = new SysRoleDto();
            BeanUtils.copyProperties(sysRole, sysRoleDto);
            String appIds = roleAndAppsMap.get(sysRole.getRoleId());
            if (StrUtil.isNotBlank(appIds)) {
                String[] appIdArray = StrUtil.trimToEmpty(appIds).split(",");
                appNames = new StringBuilder(32);
                for (String appId : appIdArray) {
                    if (appNames.length() > 0) {
                        appNames.append(",");
                    }
                    appNames.append(AppEnum.getNameByCode(Integer.parseInt(StrUtil.trim(appId))));
                }
                sysRoleDto.setAppNames(appNames.toString());
            }
            sysRoleDtoList.add(sysRoleDto);
        }
        return ListWrapper.<SysRoleDto>builder().list(sysRoleDtoList)
                .total(sysRoleIPage.getTotal()).build();
    }

    @Override
    public SysRoleDto findByRoleId(Long roleId) {
        SysRole sysRole = sysRoleService.getById(roleId);
        if (sysRole == null) {
            throw new AppException("查询的角色不存在！");
        }
        SysRoleDto sysRoleDto = new SysRoleDto();
        BeanUtils.copyProperties(sysRole, sysRoleDto);
        List<Long> rightIdList = this.sysRoleRightService.listRightIdByRoleId(roleId);
        sysRoleDto.setRightIdList(rightIdList);
        return sysRoleDto;
    }

    @Override
    public void addRole(SysRoleDto sysRoleDto, UserInfo userInfo, ReqParam reqParam) {
        if (StrUtil.isBlank(sysRoleDto.getRoleName())) {
            throw new AppException("角色名称不能为空！");
        }
        List<SysRole> sysRoleList = sysRoleService.list(new QueryWrapper<SysRole>()
                .eq("role_name", StrUtil.trimToEmpty(sysRoleDto.getRoleName()))
                .eq("corp_id", sysRoleDto.getCorpId()));
        if (CollectionUtil.isNotEmpty(sysRoleList)) {
            throw new AppException("角色名称已存在！");
        }
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(sysRoleDto, sysRole);
        sysRole.setRoleId(KeyUtil.getId());
        sysRole.setCorpId(sysRoleDto.getCorpId());
        sysRole.setOperator(userInfo.getUserId());
        sysRole.setOperatorTime(DateUtil.date());
        sysRoleService.save(sysRole);
        sysRoleDto.setRoleId(sysRole.getRoleId());
        this.addRoleRightAndApp(sysRoleDto);

        List<SysRoleRightScopeDto> rightScopeList = sysRoleDto.getRoleRightScopeList();
        if (CollectionUtil.isNotEmpty(rightScopeList)) {
            SysRoleRightScope sysRoleRightScope;
            List<SysRoleRightScope> sysRoleRightScopeList = new ArrayList<>();
            SysRoleRightScopeDetail sysRoleRightScopeDetail;
            List<SysRoleRightScopeDetail> sysRoleRightScopeDetailList = new ArrayList<>();
            for (SysRoleRightScopeDto sysRoleRightScopeDto : rightScopeList) {
                sysRoleRightScope = new SysRoleRightScope();
                sysRoleRightScopeDto.setId(KeyUtil.getId());
                sysRoleRightScopeDto.setRoleId(sysRole.getRoleId());
                BeanUtils.copyProperties(sysRoleRightScopeDto, sysRoleRightScope);
                sysRoleRightScopeList.add(sysRoleRightScope);
                // 指定组织
                if (CollectionUtil.isNotEmpty(sysRoleRightScopeDto.getOrgIdList())) {
                    for (String orgId : sysRoleRightScopeDto.getOrgIdList()) {
                        if (StrUtil.isNotBlank(orgId)) {
                            sysRoleRightScopeDetail = new SysRoleRightScopeDetail();
                            sysRoleRightScopeDetail.setId(sysRoleRightScope.getId());
                            sysRoleRightScopeDetail.setOrgId(StrUtil.trimToEmpty(orgId));
                            sysRoleRightScopeDetail.setContainLower(sysRoleRightScopeDto.getContainLower());
                            sysRoleRightScopeDetailList.add(sysRoleRightScopeDetail);
                        }
                    }
                }
            }
            sysRoleRightScopeService.saveBatch(sysRoleRightScopeList);
            sysRoleRightScopeDetailService.saveBatch(sysRoleRightScopeDetailList);
        }
        // 初始化角色权限redis
        rightRedisCompoService.initRoleRightRedis(sysRole.getRoleId());
    }

    @Override
    public SysRole updateRole(SysRoleDto sysRoleDto, UserInfo userInfo, ReqParam reqParam) {
        if (StrUtil.isBlank(sysRoleDto.getRoleName())) {
            throw new AppException("角色名称不能为空！");
        }
        SysRole sysRole = sysRoleService.getById(sysRoleDto.getRoleId());
        if (sysRole == null) {
            throw new AppException("该角色不存在！");
        }
        List<SysRole> sysRoleList = sysRoleService.list(new QueryWrapper<SysRole>()
                .eq("role_name", StrUtil.trimToEmpty(sysRoleDto.getRoleName()))
                .eq("corp_id", sysRoleDto.getCorpId())
                .ne("role_id", sysRoleDto.getRoleId()));
        if (CollectionUtil.isNotEmpty(sysRoleList)) {
            throw new AppException("角色名称已存在！");
        }
        BeanUtils.copyProperties(sysRoleDto, sysRole);
        sysRole.setOperator(userInfo.getUserId());
        sysRole.setOperatorTime(DateUtil.date());

        sysRoleService.updateById(sysRole);
        sysRoleRightService.deleteByRoleId(sysRoleDto.getRoleId());
        sysRoleAppService.deleteByRoleId(sysRoleDto.getRoleId());
        this.addRoleRightAndApp(sysRoleDto);

        sysRoleRightScopeDetailService.delRoleRightScopeByRoleId(sysRoleDto.getRoleId());
        sysRoleRightScopeService.delRoleRightScopeByRoleId(sysRoleDto.getRoleId());
        List<SysRoleRightScopeDto> rightScopeList = sysRoleDto.getRoleRightScopeList();
        if (CollectionUtil.isNotEmpty(rightScopeList)) {
            SysRoleRightScope sysRoleRightScope;
            List<SysRoleRightScope> sysRoleRightScopeList = new ArrayList<>();
            SysRoleRightScopeDetail sysRoleRightScopeDetail;
            List<SysRoleRightScopeDetail> sysRoleRightScopeDetailList = new ArrayList<>();
            for (SysRoleRightScopeDto sysRoleRightScopeDto : rightScopeList) {
                sysRoleRightScope = new SysRoleRightScope();
                sysRoleRightScopeDto.setId(KeyUtil.getId());
                sysRoleRightScopeDto.setRoleId(sysRole.getRoleId());
                BeanUtils.copyProperties(sysRoleRightScopeDto, sysRoleRightScope);
                sysRoleRightScopeList.add(sysRoleRightScope);
                // 指定组织
                if (CollectionUtil.isNotEmpty(sysRoleRightScopeDto.getOrgIdList())) {
                    for (String orgId : sysRoleRightScopeDto.getOrgIdList()) {
                        if (StrUtil.isNotBlank(orgId)) {
                            sysRoleRightScopeDetail = new SysRoleRightScopeDetail();
                            sysRoleRightScopeDetail.setId(sysRoleRightScope.getId());
                            sysRoleRightScopeDetail.setOrgId(StrUtil.trimToEmpty(orgId));
                            sysRoleRightScopeDetail.setContainLower(sysRoleRightScopeDto.getContainLower());
                            sysRoleRightScopeDetailList.add(sysRoleRightScopeDetail);
                        }
                    }
                }
            }
            sysRoleRightScopeService.saveBatch(sysRoleRightScopeList);
            sysRoleRightScopeDetailService.saveBatch(sysRoleRightScopeDetailList);
        }
        // 初始化角色权限redis
        rightRedisCompoService.initRoleRightRedis(sysRole.getRoleId());
        return sysRole;
    }

    /**
     * 删除角色
     *
     * @param roleId
     * @return
     * @author zgpi
     * @date 2019/11/27 09:29
     **/
    @Override
    public void delRole(Long roleId) {
        List<Long> userIdList = null;
        List<Long> rightIdList = null;
        SysRole sysRole = sysRoleService.getById(roleId);
        if (sysRole != null) {
            List<SysRoleUser> sysRoleUserList = sysRoleUserService.list(new QueryWrapper<SysRoleUser>()
                    .eq("role_id", roleId));
            List<SysRight> sysRightList = sysRightService.list(new QueryWrapper<SysRight>()
                    .eq("has_scope", "Y"));
            userIdList = sysRoleUserList.stream().map(e -> e.getUserId()).collect(Collectors.toList());
            rightIdList = sysRightList.stream().map(e -> e.getRightId()).collect(Collectors.toList());
        }
        sysRoleRightService.deleteByRoleId(roleId);
        sysRoleService.removeById(roleId);
        sysRoleAppService.deleteByRoleId(roleId);
        sysRoleRightScopeDetailService.delRoleRightScopeByRoleId(roleId);
        sysRoleRightScopeService.delRoleRightScopeByRoleId(roleId);
        sysRoleUserService.delRoleUser(roleId);

        // 异步删除redis中角色用户的范围权限key
        rightRedisCompoService.delRoleUserScopeRightKey(sysRole.getCorpId(), userIdList, rightIdList);
        // 异步删除redis中人员的角色
        rightRedisCompoService.delUserRoleRedis(sysRole.getCorpId(), sysRole.getRoleId(), userIdList);
        // 异步删除角色权限redis
        rightRedisCompoService.delRoleRightRedis(roleId);
    }

    /**
     * 人员编号与角色列表映射
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/12/16 09:26
     **/
    @Override
    public Map<Long, List<SysRole>> mapUserIdAndRoleList(Long corpId, List<Long> userIdList) {
        List<SysRoleUserDto> sysRoleUserDtoList;
        if (userIdList != null && userIdList.size() > 0) {
            sysRoleUserDtoList = sysRoleUserService.listRoleUserByUserIdList(corpId, userIdList);
        } else {
            sysRoleUserDtoList = sysRoleUserService.listRoleUser(corpId);
        }
        Map<Long, List<SysRole>> map = new HashMap<>();
        if (CollectionUtil.isNotEmpty(sysRoleUserDtoList)) {
            List<SysRole> list;
            SysRole sysRole;
            for (SysRoleUserDto sysRoleUserDto : sysRoleUserDtoList) {
                Long userId = sysRoleUserDto.getUserId();
                list = new ArrayList<>();
                if (map.containsKey(userId)) {
                    list = map.get(userId);
                }
                sysRole = new SysRole();
                sysRole.setRoleId(sysRoleUserDto.getRoleId());
                sysRole.setRoleName(sysRoleUserDto.getRoleName());
                list.add(sysRole);
                map.put(userId, list);
            }
        }
        return map;
    }

    /**
     * 人员编号与角色名称映射
     *
     * @param corpId
     * @param userIdList
     * @return
     * @author zgpi
     * @date 2019/12/17 14:57
     **/
    @Override
    public Map<Long, String> mapUserIdAndRoleNames(Long corpId, List<Long> userIdList) {
        List<SysRoleUserDto> sysRoleUserDtoList = sysRoleUserService.listRoleUserByUserIdList(corpId, userIdList);
        Map<Long, String> map = new HashMap<>();
        if (CollectionUtil.isNotEmpty(sysRoleUserDtoList)) {
            for (SysRoleUserDto sysRoleUserDto : sysRoleUserDtoList) {
                Long userId = sysRoleUserDto.getUserId();
                String roleNames = "";
                if (map.containsKey(userId)) {
                    roleNames = map.get(userId);
                    roleNames += "," + sysRoleUserDto.getRoleName();
                } else {
                    roleNames = sysRoleUserDto.getRoleName();
                }
                map.put(userId, roleNames);
            }
        }
        return map;
    }

    /**
     * 添加系统角色
     *
     * @param corpId
     * @param userId
     * @return
     * @author zgpi
     * @date 2019/12/24 10:58
     **/
    @Override
    public Long addSysRole(Long corpId, Long userId) {
        // 添加角色
        SysRole sysRole = new SysRole();
        sysRole.setCorpId(corpId);
        sysRole.setSysType(1);
        sysRole.setRoleName("系统管理员");
        sysRole.setEnabled("Y");
        sysRole.setDescription("系统管理员，不能编辑删除");
        sysRole.setOperator(userId);
        sysRole.setOperatorTime(DateUtil.date());
        Long roleId = KeyUtil.getId();
        sysRole.setRoleId(roleId);
        sysRoleService.save(sysRole);
        // 添加角色用户
        SysRoleUser sysRoleUser = new SysRoleUser();
        sysRoleUser.setUserId(userId);
        sysRoleUser.setRoleId(sysRole.getRoleId());
        sysRoleUserService.save(sysRoleUser);
        // 初始化系统管理员角色人员列表
        rightRedisCompoService.initCorpSysRoleUserListRedis(corpId);
        return roleId;
    }

    /**
     * 用户角色列表
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2019/12/24 18:06
     **/
    @Override
    public List<SysRole> listUserRole(Long userId) {
        return sysRoleService.listUserRole(userId);
    }

    @Override
    public int setAdmin(SysRoleDto sysRoleDto) {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        Long corpId = sysRoleDto.getCorpId();
        Long userId = sysRoleDto.getUserId();
        if (!corpUserService.ifUserInCorp(userId, corpId)) {
            UserRealDto userRealDto = userRealService.findUserRealDtoById(userId);
            if (userRealDto != null) {
                String userName = userRealDto.getUserName();
                if (userName != null && userName.length() > 0) {
                    corpUserService.addCorpUser(userId, corpId, userName);
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        }
        // 若是紫金，则该员工自动隐藏
        if ("isZiJin".equals(sysRoleDto.getKey())) {
            corpUserService.setAdmin(userId, corpId);
        }
        wrapper.eq("role_name", "系统管理员");
        wrapper.eq("corp_id", corpId);
        SysRole sysRole = this.getOne(wrapper);

        if (sysRole == null) {
            // 没有系统管理员角色
            Long roleId = this.addSysRole(corpId, userId);
            // 添加人员角色redis
            rightRedisCompoService.addUserRoleRedis(corpId, userId, roleId);
            // 初始化系统管理员角色人员列表
            rightRedisCompoService.initCorpSysRoleUserListRedis(sysRoleDto.getCorpId());
            //需要更新角色列表和人员列表
            return 2;

        } else {
            SysRoleUser queryRoleUser = sysRoleUserService.querySysRoleUser(sysRole.getRoleId(), userId);
            if (queryRoleUser == null) {
                // 添加角色用户
                SysRoleUser sysRoleUser = new SysRoleUser();
                sysRoleUser.setUserId(userId);
                sysRoleUser.setRoleId(sysRole.getRoleId());
                sysRoleUserService.save(sysRoleUser);
                // 添加人员角色redis
                rightRedisCompoService.addUserRoleRedis(corpId, userId, sysRole.getRoleId());
                // 初始化系统管理员角色人员列表
                rightRedisCompoService.initCorpSysRoleUserListRedis(sysRoleDto.getCorpId());
                // 需要更新人员列表
                return 1;
            }
            // 不用更新角色列表
            return 0;
        }
    }

    /**
     * 添加角色对应权限、对应应用
     *
     * @param sysRoleDto
     * @return
     * @author zgpi
     * @date 2019/12/14 14:01
     **/
    private void addRoleRightAndApp(SysRoleDto sysRoleDto) {
        if (CollectionUtil.isNotEmpty(sysRoleDto.getRightIdList())) {
            List<SysRoleRight> sysRoleRightList = new ArrayList<>();
            Set<SysRoleApp> sysRoleAppSet = new HashSet<>();
            SysRoleRight sysRoleRight;
            SysRoleApp sysRoleApp;
            Map<Long, Integer> rightAndAppMap = sysRightService.mapIdAndAppId(sysRoleDto.getRightIdList());
            for (Long rightId : sysRoleDto.getRightIdList()) {
                sysRoleRight = new SysRoleRight();
                sysRoleRight.setRoleId(sysRoleDto.getRoleId());
                sysRoleRight.setRightId(rightId);
                sysRoleRightList.add(sysRoleRight);
                sysRoleApp = new SysRoleApp();
                sysRoleApp.setRoleId(sysRoleDto.getRoleId());
                sysRoleApp.setAppId(rightAndAppMap.get(rightId));
                sysRoleAppSet.add(sysRoleApp);
            }
            sysRoleRightService.saveBatch(sysRoleRightList);
            sysRoleAppService.saveBatch(sysRoleAppSet);
        }
    }

}
