package com.zjft.usp.uas.right.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.anyfix.service.AnyfixFeignService;
import com.zjft.usp.common.enums.RightScopeTypeEnum;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.uas.baseinfo.service.CfgAreaService;
import com.zjft.usp.uas.corp.service.CorpRegistryService;
import com.zjft.usp.uas.right.composite.SysRoleRightScopeCompoService;
import com.zjft.usp.uas.right.dto.SysRoleRightScopeDto;
import com.zjft.usp.uas.right.model.SysRoleRightScope;
import com.zjft.usp.uas.right.model.SysRoleRightScopeDetail;
import com.zjft.usp.uas.right.service.SysRoleRightScopeDetailService;
import com.zjft.usp.uas.right.service.SysRoleRightScopeService;
import org.springframework.beans.BeanUtils;
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
 * 角色范围权限聚合实现类
 *
 * @author zgpi
 * @date 2020/6/3 16:43
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleRightScopeCompoServiceImpl implements SysRoleRightScopeCompoService {

    @Autowired
    private SysRoleRightScopeService sysRoleRightScopeService;
    @Autowired
    private SysRoleRightScopeDetailService sysRoleRightScopeDetailService;
    @Autowired
    private CfgAreaService cfgAreaService;
    @Autowired
    private CorpRegistryService corpRegistryService;

    @Resource
    private AnyfixFeignService anyfixFeignService;

    /**
     * 角色范围权限列表
     *
     * @param roleId
     * @return
     * @author zgpi
     * @date 2020/6/3 16:44
     **/
    @Override
    public List<SysRoleRightScopeDto> listRoleRightScope(Long roleId) {
        List<SysRoleRightScopeDto> sysRoleRightScopeDtoList = new ArrayList<>();
        List<SysRoleRightScope> sysRoleRightScopeList = sysRoleRightScopeService.list(new QueryWrapper<SysRoleRightScope>()
                .eq("role_id", roleId));
        List<Long> idList = sysRoleRightScopeList.stream().map(e -> e.getId()).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(idList)) {
            List<SysRoleRightScopeDetail> sysRoleRightScopeDetailList = sysRoleRightScopeDetailService.list(
                    new QueryWrapper<SysRoleRightScopeDetail>().in("id", idList));
            Map<Long, List<SysRoleRightScopeDetail>> map = this.findIdAndScopeDetailMap(sysRoleRightScopeDetailList);
            SysRoleRightScopeDto sysRoleRightScopeDto;
            for (SysRoleRightScope entity : sysRoleRightScopeList) {
                sysRoleRightScopeDto = new SysRoleRightScopeDto();
                BeanUtils.copyProperties(entity, sysRoleRightScopeDto);
                List<SysRoleRightScopeDetail> list = map.get(entity.getId());
                if (CollectionUtil.isNotEmpty(list)) {
                    List<String> orgIdList = list.stream().map(e -> e.getOrgId()).collect(Collectors.toList());
                    sysRoleRightScopeDto.setOrgIdList(orgIdList);
                    sysRoleRightScopeDto.setOrgNames(this.findOrgNames(orgIdList, sysRoleRightScopeDto.getScopeType()));
                    sysRoleRightScopeDto.setContainLower(list.get(0).getContainLower());
                }
                sysRoleRightScopeDtoList.add(sysRoleRightScopeDto);
            }
        }
        return sysRoleRightScopeDtoList;
    }

    /**
     * 获得指定组织名称
     *
     * @param orgIdList
     * @param scopeType
     * @return
     * @author zgpi
     * @date 2020/6/3 19:54
     **/
    private String findOrgNames(List<String> orgIdList, Integer scopeType) {
        List<String> nameList = new ArrayList<>();
        if (RightScopeTypeEnum.SERVICE_BRANCH.getCode().equals(scopeType)) {
            Result<Map<Long, String>> serviceBranchMapResult = anyfixFeignService
                    .mapServiceBranchIdAndName(JsonUtil.toJson(orgIdList));
            Map<Long, String> serviceBranchMap = new HashMap<>();
            if (Result.isSucceed(serviceBranchMapResult)) {
                serviceBranchMap = serviceBranchMapResult.getData();
            }
            for (String orgId : orgIdList) {
                nameList.add(StrUtil.trimToEmpty(serviceBranchMap.get(Long.parseLong(orgId))));
            }
        } else if (RightScopeTypeEnum.PROVINCE.getCode().equals(scopeType)) {
            Map<String, String> areaMap = cfgAreaService.mapCodeAndNameByCodeList(orgIdList);
            for (String orgId : orgIdList) {
                nameList.add(StrUtil.trimToEmpty(areaMap.get(orgId)));
            }
        } else if (RightScopeTypeEnum.DEMANDER.getCode().equals(scopeType)) {
            List<Long> corpIdList = orgIdList.stream().map(e -> Long.parseLong(e)).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(corpIdList)) {
                Map<Long, String> corpMap = corpRegistryService.mapCorpIdAndShortName(corpIdList);
                for (String orgId : orgIdList) {
                    nameList.add(StrUtil.trimToEmpty(corpMap.get(Long.parseLong(orgId))));
                }
            }
        }
        return CollectionUtil.join(nameList, ",");
    }

    private Map<Long, List<SysRoleRightScopeDetail>> findIdAndScopeDetailMap(List<SysRoleRightScopeDetail> sysRoleRightScopeDetailList) {
        Map<Long, List<SysRoleRightScopeDetail>> map = new HashMap<>();
        if (CollectionUtil.isNotEmpty(sysRoleRightScopeDetailList)) {
            for (SysRoleRightScopeDetail sysRoleRightScopeDetail : sysRoleRightScopeDetailList) {
                List<SysRoleRightScopeDetail> list = new ArrayList<>();
                if (map.containsKey(sysRoleRightScopeDetail.getId())) {
                    list = map.get(sysRoleRightScopeDetail.getId());
                }
                list.add(sysRoleRightScopeDetail);
                map.put(sysRoleRightScopeDetail.getId(), list);
            }
        }
        return map;
    }
}
