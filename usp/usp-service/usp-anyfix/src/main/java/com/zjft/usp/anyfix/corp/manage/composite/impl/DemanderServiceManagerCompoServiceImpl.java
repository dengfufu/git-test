package com.zjft.usp.anyfix.corp.manage.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjft.usp.anyfix.corp.manage.composite.DemanderServiceManagerCompoService;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderServiceDto;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderServiceManagerDto;
import com.zjft.usp.anyfix.corp.manage.model.DemanderService;
import com.zjft.usp.anyfix.corp.manage.model.DemanderServiceManager;
import com.zjft.usp.anyfix.corp.manage.service.DemanderServiceManagerService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderServiceService;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.uas.service.UasFeignService;
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
 * 委托商与服务商客户经理关系聚合实现类
 *
 * @author zgpi
 * @date 2020/6/15 13:42
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DemanderServiceManagerCompoServiceImpl implements DemanderServiceManagerCompoService {

    @Autowired
    private DemanderServiceService demanderServiceService;
    @Autowired
    private DemanderServiceManagerService demanderServiceManagerService;

    @Resource
    private UasFeignService uasFeignService;

    /**
     * 编辑客户经理
     *
     * @param demanderServiceManagerDto
     * @return
     * @author zgpi
     * @date 2020/6/15 13:41
     **/
    @Override
    public void editDemanderServiceManager(DemanderServiceManagerDto demanderServiceManagerDto) {
        Long referId = demanderServiceManagerDto.getReferId();
        if (LongUtil.isZero(referId)) {
            return;
        }
        DemanderService demanderService = demanderServiceService.getById(referId);
        if (demanderService == null) {
            return;
        }
        Long demanderCorp = demanderService.getDemanderCorp();
        Long serviceCorp = demanderService.getServiceCorp();
        demanderServiceManagerService.remove(new UpdateWrapper<DemanderServiceManager>()
                .eq("demander_corp", demanderCorp)
                .eq("service_corp", serviceCorp));
        if (CollectionUtil.isNotEmpty(demanderServiceManagerDto.getDemanderServiceManagerList())) {
            for (DemanderServiceManager demanderServiceManager : demanderServiceManagerDto.getDemanderServiceManagerList()) {
                demanderServiceManager.setId(KeyUtil.getId());
                demanderServiceManager.setReferId(referId);
                demanderServiceManager.setDemanderCorp(demanderCorp);
                demanderServiceManager.setServiceCorp(serviceCorp);
            }
            demanderServiceManagerService.saveBatch(demanderServiceManagerDto.getDemanderServiceManagerList());
        }
    }

    /**
     * 获得客户经理
     *
     * @param demanderServiceManagerDto
     * @return
     * @author zgpi
     * @date 2020/6/15 14:36
     **/
    @Override
    public DemanderServiceManagerDto findDemanderServiceManager(DemanderServiceManagerDto demanderServiceManagerDto) {
        DemanderServiceManagerDto entity = new DemanderServiceManagerDto();
        Long referId = demanderServiceManagerDto.getReferId();
        entity.setReferId(referId);
        DemanderService demanderService = demanderServiceService.getById(referId);
        if (demanderService == null) {
            return entity;
        }
        Long demanderCorp = demanderService.getDemanderCorp();
        Long serviceCorp = demanderService.getServiceCorp();
        if (LongUtil.isZero(demanderCorp) || LongUtil.isZero(serviceCorp)) {
            return entity;
        }
        List<DemanderServiceManager> demanderServiceManagerList = demanderServiceManagerService.list(
                new QueryWrapper<DemanderServiceManager>().eq("demander_corp", demanderCorp)
                        .eq("service_corp", serviceCorp));
        if (CollectionUtil.isNotEmpty(demanderServiceManagerList)) {
            List<Long> userList = demanderServiceManagerList.stream().map(e -> e.getManager()).collect(Collectors.toList());
            Map<Long, String> userMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(userList)) {
                Result<Map<Long, String>> userMapResult = uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userList));
                if (Result.isSucceed(userMapResult)) {
                    userMap = userMapResult.getData();
                }
            }
            List<Long> managerList = new ArrayList<>();
            List<String> managerNameList = new ArrayList<>();
            for (DemanderServiceManager demanderServiceManager : demanderServiceManagerList) {
                String name = StrUtil.trimToEmpty(userMap.get(demanderServiceManager.getManager()));
                managerNameList.add(name);
                managerList.add(demanderServiceManager.getManager());
            }
            entity.setManagerList(managerList);
            entity.setManagerNameList(managerNameList);
        }
        return entity;
    }

    /**
     * 客户经理对应的委托商编号列表
     *
     * @param corpId
     * @param manager
     * @return
     * @author zgpi
     * @date 2020/6/22 14:58
     **/
    @Override
    public List<Long> listDemanderCorpByManager(Long corpId, Long manager) {
        List<DemanderServiceManager> demanderServiceManagerList = demanderServiceManagerService.list(
                new QueryWrapper<DemanderServiceManager>().eq("service_corp", corpId)
                        .eq("manager", manager));
        return demanderServiceManagerList.stream().map(e -> e.getDemanderCorp()).collect(Collectors.toList());
    }

    /**
     * 根据客户经理获取委托商列表
     *
     * @param corpId
     * @param manager
     * @return
     */
    @Override
    public List<DemanderServiceDto> listDemanderByManager(Long corpId, Long manager) {
        if (LongUtil.isZero(corpId) || LongUtil.isZero(manager)) {
            return null;
        }
        List<Long> demanderCorpIdList = this.listDemanderCorpByManager(corpId, manager);
        List<DemanderServiceDto> list = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(demanderCorpIdList)) {
            Result<Map<Long, String>> corpMapResult = this.uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(demanderCorpIdList));
            Map<Long, String> corpMap = new HashMap<>();
            if (corpMapResult != null && Result.SUCCESS == corpMapResult.getCode().intValue()) {
                corpMap = corpMapResult.getData() == null ? new HashMap<>() : corpMapResult.getData();
            }
            for (Long demanderCorp : demanderCorpIdList) {
                DemanderServiceDto demanderServiceDto = new DemanderServiceDto();
                demanderServiceDto.setDemanderCorp(demanderCorp);
                demanderServiceDto.setServiceCorp(corpId);
                demanderServiceDto.setDemanderCorpName(StrUtil.trimToEmpty(corpMap.get(demanderCorp)));
                list.add(demanderServiceDto);
            }
        }
        return list;
    }

}
