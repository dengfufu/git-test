package com.zjft.usp.anyfix.corp.manage.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.zjft.usp.anyfix.baseinfo.enums.EnabledEnum;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderCustomDto;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderDto;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderServiceDto;
import com.zjft.usp.anyfix.corp.manage.filter.DemanderCustomFilter;
import com.zjft.usp.anyfix.corp.manage.filter.DemanderServiceFilter;
import com.zjft.usp.anyfix.corp.manage.service.DemanderCustomService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderServiceService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 服务委托方服务实现类
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/11/1 10:56
 */
@Service
public class DemanderServiceImpl implements DemanderService {

    @Autowired
    private DemanderServiceService demanderServiceService;
    @Autowired
    private DemanderCustomService demanderCustomService;

    @Override
    public List<DemanderDto> listDemander(Long corpId) {
        DemanderServiceFilter demanderServiceFilter = new DemanderServiceFilter();
        demanderServiceFilter.setServiceCorp(corpId);
        demanderServiceFilter.setDemanderCorp(corpId);
        demanderServiceFilter.setEnabled(EnabledEnum.YES.getCode());
        List<DemanderServiceDto> demanderServiceDtoList =
                demanderServiceService.listDemanderByService(demanderServiceFilter);
        DemanderCustomFilter demanderCustomFilter = new DemanderCustomFilter();
        demanderCustomFilter.setCustomCorp(corpId);
        demanderCustomFilter.setDemanderCorp(corpId);
        demanderCustomFilter.setEnabled(EnabledEnum.YES.getCode());
        List<DemanderCustomDto> demanderCustomDtoList =
                demanderCustomService.listDemanderByCustom(demanderCustomFilter);
        List<DemanderDto> list = new ArrayList<>();
        DemanderDto demanderDto;
        if(CollectionUtil.isNotEmpty(demanderServiceDtoList)){
            for(DemanderServiceDto demanderServiceDto : demanderServiceDtoList){
                demanderDto = new DemanderDto();
                demanderDto.setId(demanderServiceDto.getId());
                demanderDto.setDemanderCorp(demanderServiceDto.getDemanderCorp());
                demanderDto.setDemanderCorpName(demanderServiceDto.getDemanderCorpName());
                list.add(demanderDto);
            }
        }
        if(CollectionUtil.isNotEmpty(demanderCustomDtoList)){
            for(DemanderCustomDto demanderCustomDto : demanderCustomDtoList){
                demanderDto = new DemanderDto();
                demanderDto.setDemanderCorp(demanderCustomDto.getDemanderCorp());
                demanderDto.setDemanderCorpName(demanderCustomDto.getDemanderCorpName());
                list.add(demanderDto);
            }
        }
//        Result corpResult = uasFeignService.findCorpById(corpId);
//        CorpDto corpDto = JsonUtil.parseObject(JsonUtil.toJson(corpResult.getData()), CorpDto.class);
//        if(corpDto != null && LongUtil.isNotZero(corpDto.getCorpId())){
//            demanderDto = new DemanderDto();
//            demanderDto.setDemanderCorp(corpId);
//            demanderDto.setDemanderCorpName(corpDto.getCorpName());
//            list.add(demanderDto);
//        }
        // 去重
        List<DemanderDto> newList =  list.stream().collect(
                Collectors.collectingAndThen(Collectors.toCollection(
                        () -> new TreeSet<>(
                                Comparator.comparing(DemanderDto::getDemanderCorp))), ArrayList::new)
        );
        return newList;
    }


    @Override
    public Map<Long, String> getDemanderIdNameMap(Long corpId) {
        List<DemanderDto> newList = this.listDemander(corpId);
        Map<Long, String> map = newList.stream().filter(t -> StringUtils.isNotBlank(t.getDemanderCorpName())).
                collect(Collectors.toMap(DemanderDto::getDemanderCorp,DemanderDto::getDemanderCorpName));
        return map;
    }

    @Override
    public List<Long> demanderCorpIdList(Long corpId) {
        return null;
    }
}
