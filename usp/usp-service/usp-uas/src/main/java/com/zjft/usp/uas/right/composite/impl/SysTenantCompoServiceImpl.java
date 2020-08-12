package com.zjft.usp.uas.right.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.zjft.usp.common.constant.RedisRightConstants;
import com.zjft.usp.common.model.Right;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.redis.template.RedisRepository;
import com.zjft.usp.uas.corp.model.CorpRegistry;
import com.zjft.usp.uas.corp.service.CorpRegistryService;
import com.zjft.usp.uas.right.composite.SysTenantCompoService;
import com.zjft.usp.uas.right.dto.SysRoleRightDto;
import com.zjft.usp.uas.right.enums.TenantEnum;
import com.zjft.usp.uas.right.filter.SysRightFilter;
import com.zjft.usp.uas.right.model.SysTenant;
import com.zjft.usp.uas.right.service.SysRoleRightService;
import com.zjft.usp.uas.right.service.SysTenantService;
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
 * 系统租户聚合实现类
 *
 * @author zgpi
 * @date 2020/6/17 16:37
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysTenantCompoServiceImpl implements SysTenantCompoService {

    @Autowired
    private SysTenantService sysTenantService;
    @Autowired
    private SysRoleRightService sysRoleRightService;
    @Autowired
    private CorpRegistryService corpRegistryService;

    @Resource
    private RedisRepository redisRepository;


    /**
     * 初始化租户redis
     *
     * @return
     * @author zgpi
     * @date 2020/6/17 16:37
     **/
    @Override
    public void initSysTenantRedis() {
        String flag = (String) redisRepository.get(RedisRightConstants.getCorpTenantInit());
        if ("Y".equalsIgnoreCase(StrUtil.trimToEmpty(flag))) {
            return;
        }
        List<CorpRegistry> corpRegistryList = corpRegistryService.list();
        List<String> keyList = corpRegistryList.stream().map(e -> RedisRightConstants.getCorpTenantKey(e.getCorpId()))
                .collect(Collectors.toList());
        redisRepository.del(CollectionUtil.join(keyList, ","));
        List<SysTenant> sysTenantList = sysTenantService.list();
        for (SysTenant sysTenant : sysTenantList) {
            redisRepository.set(RedisRightConstants.getCorpTenantKey(sysTenant.getCorpId()), JsonUtil.toJson(sysTenant));
        }
        redisRepository.set(RedisRightConstants.getCorpTenantInit(), "Y");
    }

    /**
     * 初始化租户权限redis
     *
     * @return
     * @author zgpi
     * @date 2020/6/17 16:44
     **/
    @Override
    public void initTenantRightRedis() {
        String flag = (String) redisRepository.get(RedisRightConstants.getTenantRightInit());
        if ("Y".equalsIgnoreCase(StrUtil.trimToEmpty(flag))) {
            return;
        }
        List<SysRoleRightDto> sysRoleRightDtoList;
        Map<String, List<Right>> rightMap;
        SysRightFilter sysRightFilter;
        sysRightFilter = new SysRightFilter();
        // 委托商
        sysRightFilter.setServiceDemander("Y");
        sysRoleRightDtoList = sysRoleRightService.listSysAuthRight(sysRightFilter);
        rightMap = this.findUriAndRightListMap(sysRoleRightDtoList);
        redisRepository.set(RedisRightConstants.getTenantRightKey(TenantEnum.SERVICE_DEMANDER.getCode()),
                rightMap);

        // 服务商
        sysRightFilter = new SysRightFilter();
        sysRightFilter.setServiceProvider("Y");
        sysRoleRightDtoList = sysRoleRightService.listSysAuthRight(sysRightFilter);
        rightMap = this.findUriAndRightListMap(sysRoleRightDtoList);
        redisRepository.set(RedisRightConstants.getTenantRightKey(TenantEnum.SERVICE_PROVIDER.getCode()),
                rightMap);

        // 设备使用商
        sysRightFilter = new SysRightFilter();
        sysRightFilter.setDeviceUser("Y");
        sysRoleRightDtoList = sysRoleRightService.listSysAuthRight(sysRightFilter);
        rightMap = this.findUriAndRightListMap(sysRoleRightDtoList);
        redisRepository.set(RedisRightConstants.getTenantRightKey(TenantEnum.DEVICE_USER.getCode()),
                rightMap);

        // 平台管理
        sysRightFilter = new SysRightFilter();
        sysRightFilter.setCloudManager("Y");
        sysRoleRightDtoList = sysRoleRightService.listSysAuthRight(sysRightFilter);
        rightMap = this.findUriAndRightListMap(sysRoleRightDtoList);
        redisRepository.set(RedisRightConstants.getTenantRightKey(TenantEnum.CLOUD_MANAGER.getCode()),
                rightMap);
        redisRepository.set(RedisRightConstants.getTenantRightInit(), "Y");
    }

    /**
     * 新增租户redis
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/19 14:24
     **/
    @Override
    public void addSysTenantRedis(Long corpId) {
        SysTenant sysTenant = sysTenantService.getById(corpId);
        redisRepository.set(RedisRightConstants.getCorpTenantKey(sysTenant.getCorpId()), JsonUtil.toJson(sysTenant));
    }

    /**
     * 修改租户redis
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/19 14:25
     **/
    @Override
    public void updateSysTenantRedis(Long corpId) {
        SysTenant sysTenant = sysTenantService.getById(corpId);
        redisRepository.set(RedisRightConstants.getCorpTenantKey(sysTenant.getCorpId()), JsonUtil.toJson(sysTenant));
    }

    /**
     * 删除租户redis
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/18 08:59
     **/
    @Override
    public void delSysTenantRedis(Long corpId) {
        redisRepository.del(RedisRightConstants.getCorpTenantKey(corpId));
    }

    private Map<String, List<Right>> findUriAndRightListMap(List<SysRoleRightDto> sysRoleRightDtoList) {
        Map<String, List<Right>> rightMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(sysRoleRightDtoList)) {
            Right right;
            for (SysRoleRightDto sysRoleRightDto : sysRoleRightDtoList) {
                String uri = StrUtil.trimToEmpty(sysRoleRightDto.getUri());
                right = new Right();
                right.setRightId(sysRoleRightDto.getRightId());
                right.setRightType(sysRoleRightDto.getRightType());
                right.setPathMethod(sysRoleRightDto.getPathMethod());
                List<Right> rightList = new ArrayList<>();
                if (rightMap.containsKey(uri)) {
                    rightList = rightMap.get(uri);
                }
                rightList.add(right);
                rightMap.put(uri, rightList);
            }
        }
        return rightMap;
    }
}
