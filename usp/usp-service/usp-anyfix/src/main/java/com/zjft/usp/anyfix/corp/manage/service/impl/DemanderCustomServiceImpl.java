package com.zjft.usp.anyfix.corp.manage.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.enums.EnabledEnum;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderCustomDetailDto;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderCustomDto;
import com.zjft.usp.anyfix.corp.manage.filter.DemanderCustomFilter;
import com.zjft.usp.anyfix.corp.manage.mapper.DemanderCustomMapper;
import com.zjft.usp.anyfix.corp.manage.model.DemanderCustom;
import com.zjft.usp.anyfix.corp.manage.model.DemanderService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderCustomService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderServiceService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.dto.CorpDto;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务委托方与用户企业关系表 服务实现类
 * </p>
 *
 * @author canlei
 * @since 2019-10-23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DemanderCustomServiceImpl extends ServiceImpl<DemanderCustomMapper, DemanderCustom> implements DemanderCustomService {

    @Autowired
    private DemanderServiceService demanderServiceService;
    @Resource
    private UasFeignService uasFeignService;

    @Override
    public List<DemanderCustomDto> listCustomByDemander(DemanderCustomFilter demanderCustomFilter) {
        List<DemanderCustomDto> list = this.baseMapper.listCustomByDemander(demanderCustomFilter);
        List<Long> corpIdList = list.stream().map(e -> e.getCustomCorp()).collect(Collectors.toList());
        Map<Long, String> corpMap = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList)).getData();
        for (DemanderCustomDto entity : list) {
            if (LongUtil.isNotZero(entity.getCustomCorp())) {
                entity.setCustomCorpName(StrUtil.trimToEmpty(corpMap.get(entity.getCustomCorp())));
            }
        }
        return list;
    }

    /**
     * 根据委托商获得客户下拉列表
     *
     * @param demanderCorp
     * @param curCorpId
     * @return
     */
    @Override
    public List<DemanderCustom> selectCustomByDemander(Long demanderCorp, Long curCorpId) {
        List<DemanderCustom> customList = new ArrayList<>();
        List<DemanderCustom> list = this.baseMapper.selectCustomByDemander(demanderCorp);
        List<Long> corpIdList = list.stream().map(e -> e.getCustomCorp()).collect(Collectors.toList());
        boolean isContains = true;
        if (!corpIdList.contains(curCorpId)) {
            corpIdList.add(curCorpId);
            isContains = false;
        }
        Map<Long, String> corpMap = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList)).getData();
        corpMap = corpMap == null ? new HashMap<>() : corpMap;
        // 客户档案没有当前企业，则加入
        if (!isContains) {
            DemanderCustom currentCustom = new DemanderCustom();
            currentCustom.setDemanderCorp(demanderCorp);
            currentCustom.setCustomCorp(curCorpId);
            currentCustom.setCustomCorpName(StrUtil.trimToEmpty(corpMap.get(curCorpId)));
            currentCustom.setEnabled(EnabledEnum.YES.getCode());
            customList.add(currentCustom);
        } else { // 否则放在第一个
            for (DemanderCustom demanderCustom : list) {
                if (demanderCustom.getCustomCorp().equals(curCorpId)) {
                    demanderCustom.setCustomCorpName(StrUtil.trimToEmpty(corpMap.get(demanderCustom.getCustomCorp())));
                    customList.add(demanderCustom);
                    break;
                }
            }
        }
        for (DemanderCustom demanderCustom : list) {
            if (demanderCustom.getCustomCorp().equals(curCorpId)) {
                continue;
            }
            if (LongUtil.isNotZero(demanderCustom.getCustomCorp())) {
                demanderCustom.setCustomCorpName(StrUtil.trimToEmpty(corpMap.get(demanderCustom.getCustomCorp())));
            }
            customList.add(demanderCustom);
        }
        return customList;
    }


    @Override
    public List<DemanderCustomDto> listDemanderByCustom(DemanderCustomFilter demanderCustomFilter) {
        List<DemanderCustomDto> dtoList = new ArrayList<>();
        QueryWrapper<DemanderCustom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("custom_corp", demanderCustomFilter.getCustomCorp());
        queryWrapper.ne("custom_corp", 0);
        if (StrUtil.isNotBlank(demanderCustomFilter.getEnabled())) {
            queryWrapper.eq("enabled", StrUtil.trimToEmpty(demanderCustomFilter.getEnabled()).toUpperCase());
        }
        List<DemanderCustom> list = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            List<Long> corpIdList = list.stream().map(e -> e.getDemanderCorp()).collect(Collectors.toList());
            Map<Long, String> corpMap = this.uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList)).getData();
            for (DemanderCustom demanderCustom : list) {
                DemanderCustomDto dto = new DemanderCustomDto();
                BeanUtils.copyProperties(demanderCustom, dto);
                dto.setDemanderCorpName(corpMap.get(demanderCustom.getDemanderCorp()));
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    @Override
    public List<Long> listCustomCorpIdByDemander(Long demanderCorp) {
        List<DemanderCustom> demanderCustomList = this.list(new QueryWrapper<DemanderCustom>()
                .eq("demander_corp", demanderCorp));
        List<Long> list = new ArrayList<>();
        if (demanderCustomList != null && demanderCustomList.size() > 0) {
            list = demanderCustomList.stream().map(e -> e.getCustomCorp()).collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public ListWrapper<DemanderCustomDto> pageByFilter(DemanderCustomFilter demanderCustomFilter, ReqParam reqParam) {
        ListWrapper<DemanderCustomDto> listWrapper = new ListWrapper<>();
        if (LongUtil.isZero(demanderCustomFilter.getDemanderCorp())) {
            demanderCustomFilter.setDemanderCorp(reqParam.getCorpId());
        }
        QueryWrapper<DemanderCustom> wrapper = new QueryWrapper<>();
        wrapper.eq("demander_corp", demanderCustomFilter.getDemanderCorp());
        if (LongUtil.isNotZero(demanderCustomFilter.getCustomId())) {
            wrapper.eq("custom_id", demanderCustomFilter.getCustomId());
        }
        if (StrUtil.isNotBlank(demanderCustomFilter.getCustomCorpName())) {
            wrapper.like("custom_corp_name", demanderCustomFilter.getCustomCorpName());
        }
        if (StrUtil.isNotBlank(demanderCustomFilter.getEnabled())) {
            wrapper.eq("enabled", StrUtil.trimToEmpty(demanderCustomFilter.getEnabled()).toUpperCase());
        }
        Map<Long, CorpDto> corpMap = new HashMap<>();
        Page page = new Page(demanderCustomFilter.getPageNum(), demanderCustomFilter.getPageSize());
        IPage<DemanderCustom> demanderCustomIPage = this.page(page, wrapper);
        List<DemanderCustom> demanderCustomList = demanderCustomIPage.getRecords();
        if (CollectionUtil.isNotEmpty(demanderCustomList)) {
            List<Long> corpIdList = demanderCustomList.stream().map(e -> e.getCustomCorp()).collect(Collectors.toList());
            List<Long> corpIdList2 = demanderCustomList.stream().map(e -> e.getDemanderCorp()).collect(Collectors.toList());
            corpIdList.addAll(corpIdList2);
            Result result = uasFeignService.listCorpByIdList(JsonUtil.toJson(corpIdList));
            List<CorpDto> dtos = JsonUtil.parseArray(JsonUtil.toJson(result.getData()), CorpDto.class);
            if (CollectionUtil.isNotEmpty(dtos)) {
                corpMap = dtos.stream().collect(Collectors.toMap(CorpDto::getCorpId, corpDto -> corpDto));
            }
        }

        List<DemanderCustomDto> demanderCustomDtoList = new ArrayList<>();
        DemanderCustomDto demanderCustomDto;
        for (DemanderCustom demanderCustom : demanderCustomList) {
            demanderCustomDto = new DemanderCustomDto();
            BeanUtils.copyProperties(demanderCustom, demanderCustomDto);
            if (LongUtil.isNotZero(demanderCustom.getCustomCorp())) {
                CorpDto corpDto = corpMap.get(demanderCustom.getCustomCorp());
                if (corpDto != null) {
                    demanderCustomDto.setRegion(corpDto.getRegion());
                    demanderCustomDto.setAddress(corpDto.getAddress());
                    demanderCustomDto.setTelephone(corpDto.getTelephone());
                    demanderCustomDto.setCustomCorpName(corpDto.getCorpName());
                }
            }
            if (LongUtil.isNotZero(demanderCustom.getDemanderCorp())) {
                CorpDto corpDto = corpMap.get(demanderCustom.getDemanderCorp());
                demanderCustomDto.setDemanderCorpName(StrUtil.trimToEmpty(corpDto.getCorpName()));
            }
            demanderCustomDtoList.add(demanderCustomDto);
        }
        listWrapper.setList(demanderCustomDtoList);
        listWrapper.setTotal(demanderCustomIPage.getTotal());

        return ListWrapper.<DemanderCustomDto>builder()
                .list(demanderCustomDtoList)
                .total(page.getTotal())
                .build();
    }

    @Override
    public Long addDemanderCustom(DemanderCustomDto demanderCustomDto, Long curUserId) {
        if (LongUtil.isZero(demanderCustomDto.getDemanderCorp())) {
            throw new AppException("服务委托商不能为空！");
        }
        if (LongUtil.isZero(demanderCustomDto.getCustomCorp())
                && StrUtil.isBlank(demanderCustomDto.getCustomCorpName())) {
            throw new AppException("客户企业不能为空！");
        }
        List<DemanderCustom> list = null;
        if (LongUtil.isZero(demanderCustomDto.getCustomCorp())) {
            list = this.list(new QueryWrapper<DemanderCustom>().eq("demander_corp", demanderCustomDto.getDemanderCorp())
                    .eq("custom_corp_name", demanderCustomDto.getCustomCorpName()));
        } else {
            list = this.list(new QueryWrapper<DemanderCustom>().eq("demander_corp", demanderCustomDto.getDemanderCorp())
                    .eq("custom_corp", demanderCustomDto.getCustomCorp()));
        }
        if (CollectionUtil.isNotEmpty(list)) {
            throw new AppException("该客户企业已存在，请勿重复添加！");
        }
        DemanderCustom demanderCustom = new DemanderCustom();
        BeanUtils.copyProperties(demanderCustomDto, demanderCustom);
        demanderCustom.setCustomId(KeyUtil.getId());
        demanderCustom.setOperator(curUserId);
        demanderCustom.setOperateTime(DateUtil.date());
        this.save(demanderCustom);
        return demanderCustom.getCustomId();
    }

    @Override
    public void updateDemanderCustom(DemanderCustomDto demanderCustomDto, Long curUserId) {
        if (demanderCustomDto == null || LongUtil.isZero(demanderCustomDto.getCustomId())) {
            throw new AppException("主键不能为空！");
        }
        DemanderCustom demanderCustom = this.getById(demanderCustomDto.getCustomId());
        Assert.notNull(demanderCustom, "服务商不存在！");
        BeanUtils.copyProperties(demanderCustomDto, demanderCustom);
        if (demanderCustomDto.getCustomCorp() == null) {
            demanderCustom.setCustomCorp(0L);
        }
        demanderCustom.setOperator(curUserId);
        demanderCustom.setOperateTime(DateUtil.date());
        this.updateById(demanderCustom);
    }

    @Override
    public List<Long> listRelatedCorpIdsByCustom(Long customCorp) {
        List<Long> list = null;
        HashSet<Long> set = new LinkedHashSet<>();
        if (LongUtil.isZero(customCorp)) {
            return list;
        }
        List<DemanderCustom> demanderCustomList = this.list(new QueryWrapper<DemanderCustom>().eq("custom_corp", customCorp));
        if (demanderCustomList != null && demanderCustomList.size() > 0) {
            for (DemanderCustom demanderCustom : demanderCustomList) {
                if (LongUtil.isNotZero(demanderCustom.getDemanderCorp())) {
                    set.add(demanderCustom.getDemanderCorp());
                }
            }
        }
        if (set.size() > 0) {
            List<DemanderService> demanderServiceList = this.demanderServiceService.list(new QueryWrapper<DemanderService>().in("demander_corp", set));
            if (demanderServiceList != null && demanderServiceList.size() > 0) {
                for (DemanderService demanderService : demanderServiceList) {
                    if (LongUtil.isNotZero(demanderService.getServiceCorp())) {
                        set.add(demanderService.getServiceCorp());
                    }
                }
            }
        }
        set.add(customCorp);
        list = new ArrayList<>(set);
        return list;
    }

    @Override
    public List<DemanderCustom> listCustomByCorpId(Long corpId) {
        List<DemanderCustom> list = new ArrayList<>();
        if (LongUtil.isZero(corpId)) {
            return list;
        }
        return this.baseMapper.listCustomByCorpId(corpId);
    }

    @Override
    public List<DemanderCustomDto> matchCustomByCorpForBranch(DemanderCustomFilter demanderCustomFilter) {
        Long corpId = demanderCustomFilter.getCorpId();
        // 查询客户列表
        List<DemanderCustomDto> list = this.baseMapper.matchCustomByCorp(demanderCustomFilter);
        if(list == null) {
            list = new ArrayList<>();
        }
        List<Long> corpIdList = new ArrayList<>();
        List<DemanderCustomDto> removeIndex = new ArrayList<>();
        for( DemanderCustomDto demanderCustomDto: list ) {
            if( demanderCustomDto != null &&  LongUtil.isNotZero(demanderCustomDto.getCustomCorp()) ) {
                corpIdList.add(demanderCustomDto.getCustomCorp());
                removeIndex.add(demanderCustomDto);
            }
        }
        if(CollectionUtil.isNotEmpty(removeIndex)) {
            for(DemanderCustomDto demanderCustomDto : removeIndex) {
                list.remove(demanderCustomDto);
            }
        }
        // 添加自己信息
        corpIdList.add(demanderCustomFilter.getCorpId());
        // 列出委托商信息
        List<Long> demanderCorpIdList = demanderServiceService.listDemanderCorpId(corpId);
        if(CollectionUtil.isEmpty(demanderCorpIdList)) {
            corpIdList.addAll(demanderCorpIdList);
        }
        JSONObject dataJson = new JSONObject();
        dataJson.put("matchFilter", demanderCustomFilter.getCustomCorpName());
        dataJson.put("corpIdList", corpIdList);
        Result result = uasFeignService.matchCorp(dataJson.toJSONString());
        if(result != null && result.getCode() == Result.SUCCESS) {
            List<CorpDto> corpUserDtoList = JsonUtil.parseArray(JsonUtil.toJson(result.getData()), CorpDto.class);
            DemanderCustomDto demanderCustomDto;
            for(CorpDto corpDto: corpUserDtoList) {
                demanderCustomDto = new DemanderCustomDto();
                demanderCustomDto.setCustomCorp(corpDto.getCorpId());
                demanderCustomDto.setCustomCorpName(corpDto.getShortName());
                demanderCustomDto.setDemander(true);
                list.add(demanderCustomDto);
            }
        }
        // 去除重复
        list =  list.stream().distinct().collect(Collectors.toList());
        return list;
    }

    @Override
    public Map<Long, String> mapIdAndCustomNameByIdList(List<Long> customIdList) {
        Map<Long, String> map = new HashMap<>();
        if (CollectionUtil.isEmpty(customIdList)) {
            return map;
        }
        List<DemanderCustom> list = (List<DemanderCustom>) this.listByIds(customIdList);
        if (CollectionUtil.isNotEmpty(list)) {
            for (DemanderCustom demanderCustom : list) {
                map.put(demanderCustom.getCustomId(), demanderCustom.getCustomCorpName());
            }
        }
        return map;
    }

    /**
     * 根据主键获得详情
     *
     * @param customId
     * @return
     * @author zgpi
     * @date 2019/10/31 18:31
     **/
    @Override
    public DemanderCustomDto findDtoById(Long customId) {
        DemanderCustom demanderCustom = this.getById(customId);
        DemanderCustomDto demanderCustomDto = null;
        if (demanderCustom != null) {
            demanderCustomDto = new DemanderCustomDto();
            BeanUtils.copyProperties(demanderCustom, demanderCustomDto);
            List<Long> corpIdList = new ArrayList<>();
            corpIdList.add(demanderCustom.getCustomCorp());
            corpIdList.add(demanderCustom.getDemanderCorp());
            Result result = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList));
            Map<Long, String> corpMap = (Map<Long, String>) result.getData();
            if (corpMap != null) {
                demanderCustomDto.setDemanderCorpName(StrUtil.trimToEmpty(corpMap.get(demanderCustom.getDemanderCorp())));
                String customCorpName = StrUtil.trimToEmpty(corpMap.get(demanderCustom.getCustomCorp()));
                if (StrUtil.isNotBlank(customCorpName)) {
                    demanderCustomDto.setCustomCorpName(customCorpName);
                }
            }
        }
        return demanderCustomDto;
    }

    @Override
    public List<Long> listIdsByCustomCorp(Long customCorp) {
        List<Long> idList = new ArrayList<>();
        if (LongUtil.isZero(customCorp)) {
            return idList;
        }
        List<DemanderCustom> list = this.list(new QueryWrapper<DemanderCustom>().eq("custom_corp", customCorp));
        if (list != null && list.size() > 0) {
            for (DemanderCustom demanderCustom : list) {
                idList.add(demanderCustom.getCustomId());
            }
        }
        return idList;
    }

    @Override
    public DemanderCustomDetailDto getDetailDtoByIds(Long customId) {
        DemanderCustom demanderCustom = this.baseMapper.selectById(customId);
        DemanderCustomDetailDto detailDto = new DemanderCustomDetailDto();
        BeanUtils.copyProperties(demanderCustom, detailDto);
        if (LongUtil.isNotZero(demanderCustom.getCustomCorp())) {
            Result corpResult = uasFeignService.findCorpAddrById(demanderCustom.getCustomCorp());
            if (corpResult != null && corpResult.getCode() == Result.SUCCESS) {
                CorpDto corpDto = JsonUtil.parseObject(JsonUtil.toJson(corpResult.getData()), CorpDto.class);
                if (corpDto != null) {
                    detailDto.setAddress(corpDto.getAddress());
                    detailDto.setTelephone(corpDto.getTelephone());
                    detailDto.setRegion(corpDto.getRegion());
                }
            }
        }
        return detailDto;
    }

    @Override
    public List<CorpDto> listAvailableCorp(DemanderCustomFilter demanderCustomFilter) {
        List<DemanderCustomDto> list = this.baseMapper.listCustomByDemander(demanderCustomFilter);
        List<Long> corpIdList = list.stream().map(e -> e.getCustomCorp()).collect(Collectors.toList());
        JSONObject dataJson = new JSONObject();

        dataJson.put("matchFilter", demanderCustomFilter.getCorpName());
        dataJson.put("excludeCorpIdList", corpIdList);
        Result result = uasFeignService.matchCorp(dataJson.toJSONString());
        List<CorpDto> corpUserDtoList = new ArrayList<>();
        if (result != null && result.getCode() == Result.SUCCESS) {
            corpUserDtoList = JsonUtil.parseArray(JsonUtil.toJson(result.getData()), CorpDto.class);
        }
        return corpUserDtoList;
    }

    /**
     * 根据委托商获取客户编号和名称的映射
     *
     * @param demanderCorp
     * @return
     */
    @Override
    public Map<Long, String> mapCustomIdAndNameByDemander(Long demanderCorp) {
        Map<Long, String> map = new HashMap<>();
        if (LongUtil.isZero(demanderCorp)) {
            return map;
        }
        List<DemanderCustom> list = this.list(new QueryWrapper<DemanderCustom>().eq("demander_corp", demanderCorp));
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(demanderCustom -> {
                map.put(demanderCustom.getCustomId(), demanderCustom.getCustomCorpName());
            });
        }
        return map;
    }

    @Override
    public Map<String,Map<String,Long>> batchAddDemanderCustom(List<String> customCorpNameList, Long demanderCorp, Long curUserId) {
        if (LongUtil.isZero(demanderCorp)) {
            throw new AppException("服务委托商不能为空！");
        }
        if (CollectionUtil.isEmpty(customCorpNameList)) {
            for(String s : customCorpNameList){
                if(StringUtils.isEmpty(s)) {
                    throw new AppException("客户名称不能为空");
                }
            }
            throw new AppException("客户名称列表不能为空！");
        }
        customCorpNameList = customCorpNameList.stream().distinct().collect(Collectors.toList());
        QueryWrapper<DemanderCustom> queryWrapper = new QueryWrapper();
        queryWrapper.eq("demander_corp",demanderCorp).in("custom_corp_name",customCorpNameList)
                .select("custom_id", "custom_corp_name");
        List<DemanderCustom> queryList = this.list(queryWrapper);
        Map<String,DemanderCustom> nameCustomMap = new HashMap();
        if(CollectionUtil.isNotEmpty(queryList)) {
            for (DemanderCustom demanderCustom : queryList) {
                nameCustomMap.put(demanderCustom.getCustomCorpName(), demanderCustom);
            }
        }
        //删除多个
        customCorpNameList.removeIf(s -> nameCustomMap.get(s) != null);
        Map<String,Map<String,Long>>  returnMap = new HashMap<>();
        List<DemanderCustom> demanderCustomList = new ArrayList<>();
        for(String customCorpName : customCorpNameList) {
            DemanderCustom demanderCustom = new DemanderCustom();
            Map<String,Long> customMap = new HashMap<>();
            demanderCustom.setDemanderCorp(demanderCorp);
            demanderCustom.setCustomCorpName(customCorpName);
            demanderCustom.setOperator(curUserId);
            demanderCustom.setEnabled(EnabledEnum.YES.getCode());
            demanderCustom.setCustomId(KeyUtil.getId());
            demanderCustom.setOperator(curUserId);
            demanderCustom.setOperateTime(DateUtil.date());
            customMap.put("customId",demanderCustom.getCustomId());
            customMap.put("customCorp",0L);
            demanderCustomList.add(demanderCustom);
            returnMap.put(customCorpName, customMap);
        }
        this.saveBatch(demanderCustomList);
        return returnMap;
    }


}
