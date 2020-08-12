package com.zjft.usp.anyfix.corp.manage.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.enums.EnabledEnum;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderServiceDto;
import com.zjft.usp.anyfix.corp.manage.filter.DemanderServiceFilter;
import com.zjft.usp.anyfix.corp.manage.mapper.DemanderServiceMapper;
import com.zjft.usp.anyfix.corp.manage.model.DemanderCustom;
import com.zjft.usp.anyfix.corp.manage.model.DemanderService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderAutoConfigService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderCustomService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderServiceService;
import com.zjft.usp.anyfix.work.auto.dto.WorkDispatchServiceCorpDto;
import com.zjft.usp.anyfix.work.auto.filter.WorkDispatchServiceCorpFilter;
import com.zjft.usp.anyfix.work.auto.service.WorkDispatchServiceCorpService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.file.service.FileFeignService;
import com.zjft.usp.uas.dto.CorpDto;
import com.zjft.usp.uas.dto.CorpRegistry;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务委托方与服务商关系表 服务实现类
 * </p>
 *
 * @author canlei
 * @since 2019-10-23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DemanderServiceServiceImpl extends ServiceImpl<DemanderServiceMapper, DemanderService> implements DemanderServiceService {

    @Autowired
    private DemanderCustomService demanderCustomService;
    @Autowired
    private DemanderAutoConfigService demanderAutoConfigService;

    @Resource
    private UasFeignService uasFeignService;

    @Resource
    private FileFeignService fileFeignService;

    @Resource
    private WorkDispatchServiceCorpService workDispatchServiceCorpService;

    /**
     * 根据服务委托方查询服务商
     *
     * @param demanderServiceFilter
     * @return
     * @author zgpi
     * @date 2019/12/10 15:43
     **/
    @Override
    public List<DemanderServiceDto> listServiceByDemander(DemanderServiceFilter demanderServiceFilter) {
        List<DemanderServiceDto> dtoList = new ArrayList<>();
        QueryWrapper<DemanderService> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("demander_corp", demanderServiceFilter.getDemanderCorp());
        if (StrUtil.isNotBlank(demanderServiceFilter.getEnabled())) {
            queryWrapper.eq("enabled", StrUtil.trimToEmpty(demanderServiceFilter.getEnabled()).toUpperCase());
        }
        List<DemanderService> list = this.list(queryWrapper);
        Map<Long, CorpDto> corpMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(list)) {
            List<Long> corpIdList = new ArrayList<>();
            for (DemanderService demanderService : list) {
                corpIdList.add(demanderService.getServiceCorp());
            }
            Result result = uasFeignService.listCorpByIdList(JsonUtil.toJson(corpIdList));
            List<CorpDto> dtos = JsonUtil.parseArray(JsonUtil.toJson(result.getData()), CorpDto.class);
            if (dtos != null && dtos.size() > 0) {
                for (CorpDto dto : dtos) {
                    corpMap.put(dto.getCorpId(), dto);
                }
            }
            for (DemanderService demanderService : list) {
                DemanderServiceDto demanderServiceDto = new DemanderServiceDto();
                BeanUtils.copyProperties(demanderService, demanderServiceDto);
                CorpDto corpDto = corpMap.get(demanderService.getServiceCorp());
                demanderServiceDto.setServiceCorpName(corpDto == null ? "" : corpDto.getCorpName());
                demanderServiceDto.setRegion(corpDto == null ? "" : corpDto.getRegion());
                demanderServiceDto.setAddress(corpDto == null ? "" : corpDto.getAddress());
                demanderServiceDto.setTelephone(corpDto == null ? "" : corpDto.getTelephone());
                dtoList.add(demanderServiceDto);
            }
        }
//        Result corpResult = uasFeignService.findCorpById(demanderServiceFilter.getDemanderCorp());
//        if (corpResult != null && corpResult.getCode() == Result.SUCCESS) {
//            CorpDto corpDto = JsonUtil.parseObject(JsonUtil.toJson(corpResult.getData()), CorpDto.class);
//            if(corpDto != null) {
//                DemanderServiceDto demanderServiceDto = new DemanderServiceDto();
//                demanderServiceDto.setDemanderCorp(corpDto.getCorpId());
//                demanderServiceDto.setDemanderCorpName(corpDto.getCorpName());
//                demanderServiceDto.setServiceCorp(corpDto.getCorpId());
//                demanderServiceDto.setServiceCorpName(corpDto.getCorpName());
//                demanderServiceDto.setEnabled(EnabledEnum.YES.getCode());
//                demanderServiceDto.setRegion(corpDto.getRegion());
//                demanderServiceDto.setAddress(corpDto.getAddress());
//                demanderServiceDto.setTelephone(corpDto.getTelephone());
//                if (!corpMap.containsKey(corpDto.getCorpId())) {
//                    dtoList.add(demanderServiceDto);
//                }
//            }
//        }
        return dtoList;
    }

    /**
     * 根据服务商查询供应商
     *
     * @param demanderServiceFilter
     * @return
     * @author zgpi
     * @date 2019/12/10 15:43
     **/
    @Override
    public List<DemanderServiceDto> listDemanderByService(DemanderServiceFilter demanderServiceFilter) {
        List<DemanderServiceDto> dtoList = new ArrayList<>();
        QueryWrapper<DemanderService> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("service_corp", demanderServiceFilter.getServiceCorp())
                .or().eq("demander_corp", demanderServiceFilter.getDemanderCorp()));
        if (StrUtil.isNotBlank(demanderServiceFilter.getEnabled())) {
            queryWrapper.eq("enabled", StrUtil.trimToEmpty(demanderServiceFilter.getEnabled()).toUpperCase());
        }
        queryWrapper.orderByAsc("demander_corp");
        List<DemanderService> list = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            List<Long> corpIdList = list.stream().map(e -> e.getDemanderCorp()).collect(Collectors.toList());
            Map<Long, String> corpMap = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList)).getData();
            DemanderServiceDto demanderServiceDto;
            for (DemanderService demanderService : list) {
                demanderServiceDto = new DemanderServiceDto();
                BeanUtils.copyProperties(demanderService, demanderServiceDto);
                demanderServiceDto.setDemanderCorpName(StrUtil.trimToEmpty(corpMap.get(demanderService.getDemanderCorp())));
                dtoList.add(demanderServiceDto);
            }
        }
        return dtoList;
    }

    @Override
    public ListWrapper<DemanderServiceDto> pageByFilter(DemanderServiceFilter demanderServiceFilter) {
        ListWrapper<DemanderServiceDto> listWrapper = new ListWrapper<>();
        if (demanderServiceFilter == null || LongUtil.isZero(demanderServiceFilter.getDemanderCorp())) {
            return listWrapper;
        }
        QueryWrapper<DemanderService> wrapper = new QueryWrapper<>();
        wrapper.eq("demander_corp", demanderServiceFilter.getDemanderCorp());
        if (StrUtil.isNotBlank(demanderServiceFilter.getEnabled())) {
            wrapper.eq("enabled", StrUtil.trimToEmpty(demanderServiceFilter.getEnabled()));
        }
        if (LongUtil.isNotZero(demanderServiceFilter.getServiceCorp())) {
            wrapper.eq("service_corp", demanderServiceFilter.getServiceCorp());
        }
        if (!StrUtil.isBlank(demanderServiceFilter.getServiceCorpName())) {
            demanderServiceFilter.setCorpName(demanderServiceFilter.getServiceCorpName());
        }
        String jsonFilter = JsonUtil.toJson(demanderServiceFilter);
        List<Long> serviceCorpList = uasFeignService.listCorpIdByFilter(jsonFilter).getData();
        if (serviceCorpList != null && serviceCorpList.size() > 0) {
            wrapper.in("service_corp", serviceCorpList);
        }
        Page page = new Page(demanderServiceFilter.getPageNum(), demanderServiceFilter.getPageSize());
        IPage<DemanderService> demanderServiceIPage = this.page(page, wrapper);
        if (demanderServiceIPage != null && demanderServiceIPage.getRecords().size() > 0) {
            List<Long> corpIdList = null;
            Set<Long> set = new HashSet<>();
            for (DemanderService demanderService : demanderServiceIPage.getRecords()) {
                set.add(demanderService.getServiceCorp());
            }
            corpIdList = new ArrayList<>(set);
            Result result = uasFeignService.listCorpByIdList(JsonUtil.toJson(corpIdList));
            List<CorpDto> dtos = JSON.parseArray(JsonUtil.toJson(result.getData()), CorpDto.class);
            Map<Long, CorpDto> corpMap = new HashMap<>();
            if (dtos != null && dtos.size() > 0) {
                for (CorpDto dto : dtos) {
                    corpMap.put(dto.getCorpId(), dto);
                }
            }
            List<DemanderServiceDto> dtoList = new ArrayList<>();
            for (DemanderService demanderService : demanderServiceIPage.getRecords()) {
                CorpDto corpDto = corpMap.get(demanderService.getServiceCorp());
                DemanderServiceDto demanderServiceDto = new DemanderServiceDto();
                BeanUtils.copyProperties(demanderService, demanderServiceDto);
                if (corpDto != null) {
                    demanderServiceDto.setServiceCorpName(corpDto.getCorpName());
                    demanderServiceDto.setRegion(corpDto.getRegion());
                    demanderServiceDto.setAddress(corpDto.getAddress());
                    demanderServiceDto.setTelephone(corpDto.getTelephone());
                    dtoList.add(demanderServiceDto);
                }
            }
            listWrapper.setList(dtoList);
            listWrapper.setTotal(demanderServiceIPage.getTotal());
        }

        return listWrapper;
    }

    @Override
    public void addDemanderService(DemanderServiceDto demanderServiceDto, Long curUserId) {
        if (demanderServiceDto == null || LongUtil.isZero(demanderServiceDto.getDemanderCorp()) || LongUtil.isZero(demanderServiceDto.getServiceCorp())) {
            throw new AppException("服务委托方企业编号和服务商企业编号均不能为空！");
        }
        List<DemanderService> list = this.list(new QueryWrapper<DemanderService>().eq("demander_corp", demanderServiceDto.getDemanderCorp())
                .eq("service_corp", demanderServiceDto.getServiceCorp()));
        if (list != null && list.size() > 0) {
            throw new AppException("该服务商已存在，请勿重复添加！");
        }
        Long demanderCorp = demanderServiceDto.getDemanderCorp();
        Result result = uasFeignService.addSysTenantDemander(demanderCorp, curUserId);
        if (result == null) {
            throw new AppException("添加委托商失败");
        } else if (result.getCode() != Result.SUCCESS) {
            throw new AppException(result.getMsg());
        }
        DemanderService demanderService = new DemanderService();
        BeanUtils.copyProperties(demanderServiceDto, demanderService);
        demanderService.setId(KeyUtil.getId());
        demanderService.setOperator(curUserId);
        demanderService.setOperateTime(DateTime.now().toTimestamp());
        this.save(demanderService);

        // 委托商认证直接成租户，自动认证
        uasFeignService.addVerifyByCorpId(demanderService.getDemanderCorp());
        //  增加自动提单
        WorkDispatchServiceCorpFilter workDispatchServiceCorpFilter = new WorkDispatchServiceCorpFilter();
        workDispatchServiceCorpFilter.setDemanderCorp(demanderServiceDto.getDemanderCorp());
        Long total = workDispatchServiceCorpService.query(workDispatchServiceCorpFilter).getTotal();
        if (LongUtil.isZero(total)) {
            WorkDispatchServiceCorpDto workDispatchServiceCorp = new WorkDispatchServiceCorpDto();
            workDispatchServiceCorp.setDemanderCorp(demanderServiceDto.getDemanderCorp());
            workDispatchServiceCorp.setServiceCorp(demanderServiceDto.getServiceCorp());
            workDispatchServiceCorpService.add(workDispatchServiceCorp);
        }
    }

    @Override
    public void createDemanderService(CorpRegistry corpRegistry, ReqParam reqParam, Long curUserId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("registry", corpRegistry);
        jsonObject.put("reqParams", reqParam);
        jsonObject.put("curUserId", curUserId);
        Result result = uasFeignService.feignRegisterCorp(jsonObject.toJSONString());
        if (result != null && result.getCode() == Result.SUCCESS) {
            Long corpUserId = Long.parseLong((String) result.getData());
            DemanderServiceDto demanderServiceDto = new DemanderServiceDto();
            demanderServiceDto.setServiceCorp(reqParam.getCorpId());
            demanderServiceDto.setDemanderCorp(corpUserId);
            demanderServiceDto.setDescription(corpRegistry.getDescription());
            this.addDemanderService(demanderServiceDto, curUserId);

            // 委托商认证直接成租户，自动认证
            uasFeignService.addVerifyByCorpId(demanderServiceDto.getDemanderCorp());
        } else {
            if (result == null) {
                throw new AppException("新建委托商失败");
            } else {
                throw new AppException(result.getMsg());
            }
        }
    }

    @Override
    public void updateDemanderService(DemanderServiceDto demanderServiceDto, Long curUserId) {
        if (demanderServiceDto == null || LongUtil.isZero(demanderServiceDto.getId())) {
            throw new AppException("主键不能为空！");
        }
        DemanderService demanderService = this.getById(demanderServiceDto.getId());
        Assert.notNull(demanderService, "服务商不存在！");
        BeanUtils.copyProperties(demanderServiceDto, demanderService);
        demanderService.setOperator(curUserId);
        demanderService.setOperateTime(DateTime.now().toTimestamp());
        this.updateById(demanderService);
    }

    @Override
    public List<Long> listRelatedCorpIdsByDemander(Long demanderCorp) {
        List<Long> list = null;
        HashSet<Long> set = new LinkedHashSet<>();
        if (LongUtil.isZero(demanderCorp)) {
            return list;
        }
        set.add(demanderCorp);
        List<DemanderService> demanderServiceList = this.list(new QueryWrapper<DemanderService>().eq("demander_corp", demanderCorp));
        List<DemanderCustom> demanderCustomList = this.demanderCustomService.list(new QueryWrapper<DemanderCustom>().eq("demander_corp", demanderCorp));
        if (demanderServiceList != null && demanderServiceList.size() > 0) {
            for (DemanderService demanderService : demanderServiceList) {
                if (LongUtil.isNotZero(demanderService.getServiceCorp())) {
                    set.add(demanderService.getServiceCorp());
                }
            }
        }
        if (demanderCustomList != null && demanderCustomList.size() > 0) {
            for (DemanderCustom demanderCustom : demanderCustomList) {
                if (LongUtil.isNotZero(demanderCustom.getCustomCorp())) {
                    set.add(demanderCustom.getCustomCorp());
                }
            }
        }
        list = new ArrayList<>(set);
        return list;
    }

    @Override
    public List<Long> listRelatedCorpIdsByService(Long serviceCorp) {
        List<Long> list = null;
        HashSet<Long> set = new LinkedHashSet<>();
        if (LongUtil.isZero(serviceCorp)) {
            return list;
        }
        List<DemanderService> demanderServiceList = this.list(new QueryWrapper<DemanderService>().eq("service_corp", serviceCorp));
        if (demanderServiceList != null && demanderServiceList.size() > 0) {
            for (DemanderService demanderService : demanderServiceList) {
                if (LongUtil.isNotZero(demanderService.getDemanderCorp())) {
                    set.add(demanderService.getDemanderCorp());
                }
            }
        }
        if (set.size() > 0) {
            List<DemanderCustom> demanderCustomList = this.demanderCustomService.list(new QueryWrapper<DemanderCustom>().in("demander_corp", set));
            if (demanderCustomList != null && demanderCustomList.size() > 0) {
                for (DemanderCustom demanderCustom : demanderCustomList) {
                    if (LongUtil.isNotZero(demanderCustom.getCustomCorp())) {
                        set.add(demanderCustom.getCustomCorp());
                    }
                }
            }
        }
        set.add(serviceCorp);
        list = new ArrayList<>(set);
        return list;
    }

    @Override
    public List<Long> listAllRelatedCorpIds(Long corpId) {
        List<Long> list = null;
        HashSet<Long> set = new LinkedHashSet<>();
        if (LongUtil.isZero(corpId)) {
            return list;
        }
        List<Long> ifDemanderList = this.listRelatedCorpIdsByDemander(corpId);
        List<Long> ifServiceList = this.listRelatedCorpIdsByService(corpId);
        List<Long> ifCustomList = this.demanderCustomService.listRelatedCorpIdsByCustom(corpId);
        set.addAll(ifDemanderList);
        set.addAll(ifServiceList);
        set.addAll(ifCustomList);
        list = new ArrayList<>(set);
        return list;
    }

    /**
     * 根据企业编号查询所有相关企业列表
     *
     * @param corpId
     * @return
     */
    @Override
    public List<CorpDto> listRelatesCorp(Long corpId, Long excludeCorpId) {
        List<DemanderServiceDto> demanderServiceDtoList = this.baseMapper.listRelatesCorp(corpId);
        List<Long> demanderCorpIdList = demanderServiceDtoList.stream()
                .map(e -> e.getDemanderCorp()).collect(Collectors.toList());
        demanderCorpIdList.stream().distinct().collect(Collectors.toList());
        List<Long> serviceCorpIdList = demanderServiceDtoList.stream()
                .map(e -> e.getServiceCorp()).collect(Collectors.toList());
        serviceCorpIdList.stream().distinct().collect(Collectors.toList());
        demanderCorpIdList.addAll(serviceCorpIdList);
        demanderCorpIdList.removeIf(s -> s.equals(excludeCorpId));
        Result<Map<Long, String>> corpMapResult = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(demanderCorpIdList));
        List<CorpDto> corpDtoList = new ArrayList<>();
        if (corpMapResult != null && corpMapResult.getCode() == Result.SUCCESS) {
            Map<Long, String> corpMap = corpMapResult.getData();
            corpMap = corpMap == null ? new HashMap<>() : corpMap;
            CorpDto corpDto;
            for (Long corpIdKey : corpMap.keySet()) {
                String corpName = StrUtil.trimToEmpty(corpMap.get(corpIdKey));
                corpDto = new CorpDto();
                corpDto.setCorpId(corpIdKey);
                corpDto.setCorpName(corpName);
                corpDtoList.add(corpDto);
            }
        }
        corpDtoList.sort(Comparator.comparing(CorpDto::getCorpName));
        corpDtoList.stream().distinct().collect(Collectors.toList());
        return corpDtoList;
    }

    @Override
    public List<DemanderServiceDto> listServiceByCorpId(Long corpId) {
        List<DemanderServiceDto> list = this.baseMapper.listServiceByCorpId(corpId);
        if (CollectionUtil.isNotEmpty(list)) {
            List<Long> corpIdList = new ArrayList<>();
            for (DemanderService demanderService : list) {
                corpIdList.add(demanderService.getServiceCorp());
            }
            Map<Long, String> corpMap = this.uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList)).getData();
            for (DemanderServiceDto demanderServiceDto : list) {
                corpIdList.add(demanderServiceDto.getServiceCorp());
                demanderServiceDto.setServiceCorpName(corpMap.get(demanderServiceDto.getServiceCorp()));
            }
        }
        List<DemanderServiceDto> dtoListNew = list.stream().collect(
                Collectors.collectingAndThen(Collectors.toCollection(
                        () -> new TreeSet<>(
                                Comparator.comparing(DemanderServiceDto::getServiceCorp))), ArrayList::new));
        return dtoListNew;
    }

    @Override
    public List<CorpDto> listServiceAvailableByDemander(DemanderServiceFilter demanderServiceFilter) {
        List<CorpDto> corpDtoList = new ArrayList<>();
        QueryWrapper<DemanderService> queryWrapper = new QueryWrapper<>();
        if (demanderServiceFilter.isForDemander()) {
            queryWrapper.eq("service_corp", demanderServiceFilter.getServiceCorp());
        } else {
            queryWrapper.eq("demander_corp", demanderServiceFilter.getDemanderCorp());
        }
        queryWrapper.eq("enabled", "Y");
        List<DemanderService> serviceList = this.list(queryWrapper);
        List<Long> userIdList;
        JSONObject dataJson = new JSONObject();
        Result result;
        if (demanderServiceFilter.isForDemander()) {
            userIdList = serviceList.stream()
                    .map(e -> e.getDemanderCorp()).collect(Collectors.toList());
            dataJson.put("excludeCorpIdList", userIdList);
            dataJson.put("matchFilter", demanderServiceFilter.getMatchFilter());
            result = uasFeignService.matchCorp(dataJson.toJSONString());
        } else {
            userIdList = serviceList.stream()
                    .map(e -> e.getServiceCorp()).collect(Collectors.toList());
            dataJson.put("matchFilter", demanderServiceFilter.getServiceCorpName());
            // 为服务提供商类型
            dataJson.put("serviceProvider", "Y");
            dataJson.put("excludeCorpIdList", userIdList);
            result = uasFeignService.listTenant(dataJson.toJSONString());
        }
        if (result != null && result.getCode() == Result.SUCCESS) {
            corpDtoList = JsonUtil.parseArray(JsonUtil.toJson(result.getData()), CorpDto.class);
        }
        return corpDtoList;
    }

    @Override
    public ListWrapper<DemanderServiceDto> pageDemander(DemanderServiceFilter demanderServiceFilter) {
        ListWrapper<DemanderServiceDto> listWrapper = new ListWrapper<>();
        IPage<DemanderService> page = new Page(demanderServiceFilter.getPageNum(),
                demanderServiceFilter.getPageSize());
        QueryWrapper<DemanderService> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("service_corp", demanderServiceFilter.getServiceCorp());
        queryWrapper.orderByDesc("operate_time");
        if (StrUtil.isNotBlank(demanderServiceFilter.getEnabled())) {
            queryWrapper.eq("enabled", StrUtil.trimToEmpty(demanderServiceFilter.getEnabled()).toUpperCase());
        }
        if (LongUtil.isNotZero(demanderServiceFilter.getDemanderCorp())) {
            queryWrapper.eq("demander_corp", demanderServiceFilter.getDemanderCorp());
        }
        IPage<DemanderService> pageRecord = this.page(page, queryWrapper);
        List<DemanderService> list = pageRecord.getRecords();
        if (pageRecord != null && CollectionUtil.isNotEmpty(list)) {
            List<DemanderServiceDto> dtoList = new ArrayList<>();
            List<Long> corpIdList = list.stream().map(e -> e.getDemanderCorp()).collect(Collectors.toList());
            Result result = uasFeignService.listCorpByIdList(JsonUtil.toJson(corpIdList));
            Map<Long, CorpDto> corpMap = new HashMap<>();
            if (result != null && result.getCode() == Result.SUCCESS) {
                List<CorpDto> dtos = JsonUtil.parseArray(JsonUtil.toJson(result.getData()), CorpDto.class);
                if (dtos != null && dtos.size() > 0) {
                    for (CorpDto dto : dtos) {
                        corpMap.put(dto.getCorpId(), dto);
                    }
                }
            }
            DemanderServiceDto demanderServiceDto;
            for (DemanderService demanderService : list) {
                demanderServiceDto = new DemanderServiceDto();
                BeanUtils.copyProperties(demanderService, demanderServiceDto);
                CorpDto corpDto = corpMap.get(demanderService.getDemanderCorp());
                if (corpDto != null) {
                    demanderServiceDto.setDemanderCorpName(StrUtil.trimToEmpty(corpDto.getCorpName()));
                    demanderServiceDto.setServiceCorpName(corpDto.getCorpName());
                    demanderServiceDto.setDemanderShortCorpName(StrUtil.trimToEmpty(corpDto.getShortName()));
                    demanderServiceDto.setRegion(corpDto.getRegion());
                    demanderServiceDto.setAddress(corpDto.getAddress());
                    demanderServiceDto.setTelephone(corpDto.getTelephone());
                    demanderServiceDto.setRegUserName(corpDto.getRegUserName());
                    demanderServiceDto.setRegUserId(corpDto.getRegUserId());
                }
                dtoList.add(demanderServiceDto);
            }
            listWrapper.setList(dtoList);
            listWrapper.setTotal(pageRecord.getTotal());
        }
        return listWrapper;
    }

    /**
     * 根据委托商获取服务商企业编号列表
     *
     * @param demanderCorp
     * @return
     */
    @Override
    public List<Long> listServiceCorpIdsByDemander(Long demanderCorp) {
        List<Long> list = new ArrayList<>();
        if (LongUtil.isZero(demanderCorp)) {
            return list;
        }
        List<DemanderService> demanderServiceList = this.list(new QueryWrapper<DemanderService>().eq("demander_corp", demanderCorp));
        if (CollectionUtil.isNotEmpty(demanderServiceList)) {
            list = demanderServiceList.stream().map(demanderService -> demanderService.getServiceCorp()).collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public List<Long> listDemanderCorpId(Long corpId) {
        List<Long> list = new ArrayList<>();
        if (LongUtil.isZero(corpId)) {
            return list;
        }
        QueryWrapper<DemanderService> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("service_corp", corpId)
                .or().eq("demander_corp", corpId));
        queryWrapper.eq("enabled", "Y");
        queryWrapper.orderByAsc("demander_corp");
        queryWrapper.select("demander_corp");
        List<DemanderService> demanderServiceList = this.list(queryWrapper);
        list = demanderServiceList.stream()
                .map(demanderService -> demanderService.getDemanderCorp()).collect(Collectors.toList());
        return list;
    }

    @Override
    public DemanderServiceDto getDemanderDetailById(Long id) {
        DemanderService demanderService = this.getById(id);
        DemanderServiceDto demanderServiceDto = new DemanderServiceDto();
        BeanUtils.copyProperties(demanderService, demanderServiceDto);
        Result<CorpDto> result = uasFeignService.getCropDetailByID(demanderService.getDemanderCorp());
        if (result != null && result.getCode() == Result.SUCCESS) {
            CorpDto corpDto = JsonUtil.parseObject(JsonUtil.toJson(result.getData()), CorpDto.class);
            if (corpDto != null) {
                BeanUtils.copyProperties(corpDto, demanderServiceDto);
                demanderServiceDto.setDemanderCorpName(StrUtil.trimToEmpty(corpDto.getCorpName()));
                demanderServiceDto.setServiceCorpName(corpDto.getCorpName());
                demanderServiceDto.setDemanderShortCorpName(corpDto.getShortName());
            }
        }

        Result tenantResult = uasFeignService.findSysTenant(demanderService.getDemanderCorp());
        if (Result.isSucceed(tenantResult)) {
            String data = JsonUtil.toJson(tenantResult.getData());
            Integer demanderLevel =  JsonUtil.parseInt(data, "demanderLevel");
            demanderServiceDto.setDemanderLevel(demanderLevel);
        }

        List<Long> list = null;
        if (StrUtil.isNotEmpty(demanderService.getFeeRuleFiles())) {
            list = Arrays.asList(demanderService.getFeeRuleFiles().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        }
        List<Long> serviceImageList = null;
        if (StrUtil.isNotEmpty(demanderService.getServiceStandardFiles())) {
            serviceImageList = Arrays.asList(demanderService.getServiceStandardFiles().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        }
        demanderServiceDto.setFeeRuleFileList(list);
        demanderServiceDto.setServiceStandardFileList(serviceImageList);
        // 设置自动化配置
        demanderServiceDto.setDemanderAutoConfigDto(demanderAutoConfigService.findById(id));
        return demanderServiceDto;
    }

    @Override
    public void addContConfig(DemanderServiceDto demanderServiceDto) {
        if (LongUtil.isZero(demanderServiceDto.getId())) {
            throw new AppException("委托商关系编号不能为空");
        }
        DemanderService demanderService = new DemanderService();
        demanderService.setId(demanderServiceDto.getId());
        String fileIds = "";
        if (CollectionUtil.isNotEmpty(demanderServiceDto.getFeeRuleFileList())) {
            fileIds = StrUtil.join(",", demanderServiceDto.getFeeRuleFileList());
        }
        String serviceFileIds = "";
        if (CollectionUtil.isNotEmpty(demanderServiceDto.getServiceStandardFileList())) {
            serviceFileIds = StrUtil.join(",", demanderServiceDto.getServiceStandardFileList());
        }
        demanderService.setContNo(demanderServiceDto.getContNo());
        demanderService.setFeeRuleFiles(fileIds);
        demanderService.setServiceStandardFiles(serviceFileIds);
        demanderService.setFeeRuleDescription(demanderServiceDto.getFeeRuleDescription());
        demanderService.setServiceStandardNote(demanderServiceDto.getServiceStandardNote());
        this.updateById(demanderService);

        // 删除临时文件表数据
        if (CollectionUtil.isNotEmpty(demanderServiceDto.getNewFileIdList())) {
            this.fileFeignService.deleteFileTemporaryByFileIdList(demanderServiceDto.getNewFileIdList());
        }
        //删除选择的文件
        if (CollectionUtil.isNotEmpty(demanderServiceDto.getDeleteFileIdList())) {
            this.fileFeignService.deleteFileList(demanderServiceDto.getDeleteFileIdList());
        }
    }

    @Override
    public DemanderServiceDto getContConfigById(Long id) {
        if (LongUtil.isZero(id)) {
            throw new AppException("关系不能为空");
        }
        QueryWrapper<DemanderService> wrapper = new QueryWrapper<>();
        wrapper.select("id", "cont_no", "fee_rule_description", "fee_rule_files",
                "service_standard_note", "service_standard_files")
                .eq("id", id);
        DemanderService demanderService = this.baseMapper.selectOne(wrapper);
        DemanderServiceDto demanderServiceDto = appendDemanderService(demanderService);
        return demanderServiceDto;
    }

    /**
     * 模糊查询委托商
     *
     * @param demanderServiceFilter
     * @return
     * @author zgpi
     * @date 2020/6/3 10:07
     **/
    @Override
    public List<DemanderServiceDto> matchDemander(DemanderServiceFilter demanderServiceFilter) {
        QueryWrapper<DemanderService> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("service_corp", demanderServiceFilter.getServiceCorp());
        List<DemanderService> demanderServiceList = this.list(queryWrapper);
        List<Long> corpIdList = demanderServiceList.stream().map(e -> e.getDemanderCorp()).collect(Collectors.toList());
        Map<Long, String> corpMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(corpIdList)) {
            Result<Map<Long, String>> corpMapResult = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList));
            if (Result.isSucceed(corpMapResult)) {
                corpMap = corpMapResult.getData();
            }
        }
        List<DemanderServiceDto> demanderServiceDtoList = new ArrayList<>();
        DemanderServiceDto demanderServiceDto;
        for (DemanderService demanderService : demanderServiceList) {
            demanderServiceDto = new DemanderServiceDto();
            BeanUtils.copyProperties(demanderService, demanderServiceDto);
            demanderServiceDto.setDemanderCorpName(StrUtil.trimToEmpty(corpMap.get(demanderService.getDemanderCorp())));
            demanderServiceDtoList.add(demanderServiceDto);
        }
        // 根据名称模糊查询
        String demanderCorpName = StrUtil.trimToEmpty(demanderServiceFilter.getDemanderCorpName());
        if (StrUtil.isNotBlank(demanderCorpName)) {
            demanderServiceDtoList = demanderServiceDtoList.stream().filter(item -> item.getDemanderCorpName()
                    .contains(demanderCorpName)).collect(Collectors.toList());
        }
        demanderServiceDtoList.sort(Comparator.comparing(DemanderServiceDto::getDemanderCorpName));
        if (CollectionUtil.isNotEmpty(demanderServiceDtoList) && demanderServiceDtoList.size() > 50) {
            demanderServiceDtoList = CollectionUtil.sub(demanderServiceDtoList, 0, 50);
        }
        return demanderServiceDtoList;
    }

    @Override
    public List<DemanderService> listDemanderService(Long serviceCorp) {
        if (LongUtil.isZero(serviceCorp)) {
            return null;
        }
        QueryWrapper<DemanderService> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("service_corp", serviceCorp);
        queryWrapper.eq("enabled", StrUtil.trimToEmpty(EnabledEnum.YES.getCode()));
        return this.list(queryWrapper);
    }

    @Override
    public DemanderServiceDto getContConfigByParams(DemanderService demanderService) {
        if (LongUtil.isZero(demanderService.getServiceCorp())) {
            throw new AppException("服务商编号不能为空");
        }
        if (LongUtil.isZero(demanderService.getDemanderCorp())) {
            throw new AppException("委托商编号不能为空");
        }
        QueryWrapper<DemanderService> wrapper = new QueryWrapper<>();
        wrapper.eq("service_corp", demanderService.getServiceCorp())
                .eq("demander_corp", demanderService.getDemanderCorp());
        DemanderService foundDemanderService = this.baseMapper.selectOne(wrapper);
        DemanderServiceDto demanderServiceDto = appendDemanderService(foundDemanderService);
        return demanderServiceDto;
    }

    public DemanderServiceDto appendDemanderService(DemanderService demanderService) {
        DemanderServiceDto demanderServiceDto = new DemanderServiceDto();
        BeanUtils.copyProperties(demanderService, demanderServiceDto);
        if (StrUtil.isNotBlank(demanderService.getFeeRuleFiles())) {
            List<Long> list = Arrays.asList(demanderService.getFeeRuleFiles().split(","))
                    .stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            demanderServiceDto.setFeeRuleFileList(list);
        }
        if (StrUtil.isNotBlank(demanderService.getServiceStandardFiles())) {
            List<Long> list = Arrays.asList(demanderService.getServiceStandardFiles().split(","))
                    .stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            demanderServiceDto.setServiceStandardFileList(list);
        }
        return demanderServiceDto;
    }

    /**
     * 根据委托商和服务商获取委托关系
     *
     * @param serviceCorp
     * @param demanderCorp
     * @return
     */
    @Override
    public DemanderService findByDemanderAndService(Long serviceCorp, Long demanderCorp) {
        if (LongUtil.isZero(serviceCorp) || LongUtil.isZero(demanderCorp)) {
            return null;
        }
        List<DemanderService> list = this.list(new QueryWrapper<DemanderService>().eq("demander_corp", demanderCorp)
                .eq("service_corp", serviceCorp));
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据委托商获取服务商企业编号与委托关系映射
     *
     * @param demanderCorp
     * @return
     */
    @Override
    public Map<Long, DemanderService> mapServiceAndDemanderService(Long demanderCorp) {
        Map<Long, DemanderService> map = new HashMap<>();
        if (LongUtil.isZero(demanderCorp)) {
            return map;
        }
        List<DemanderService> list = this.list(new QueryWrapper<DemanderService>().eq("demander_corp", demanderCorp));
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(demanderService -> {
                map.put(demanderService.getServiceCorp(), demanderService);
            });
        }
        return map;
    }

    /**
     * 根据服务商获取委托商企业编号与委托关系映射
     *
     * @param serviceCorp
     * @return
     */
    @Override
    public Map<Long, DemanderService> mapDemanderAndDemanderService(Long serviceCorp) {
        Map<Long, DemanderService> map = new HashMap<>();
        if (LongUtil.isZero(serviceCorp)) {
            return map;
        }
        List<DemanderService> list = this.list(new QueryWrapper<DemanderService>().eq("service_corp", serviceCorp));
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(demanderService -> {
                map.put(demanderService.getDemanderCorp(), demanderService);
            });
        }
        return map;
    }

}
