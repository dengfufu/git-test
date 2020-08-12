package com.zjft.usp.anyfix.baseinfo.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.dto.FaultTypeDto;
import com.zjft.usp.anyfix.baseinfo.enums.EnabledEnum;
import com.zjft.usp.anyfix.baseinfo.filter.FaultTypeFilter;
import com.zjft.usp.anyfix.baseinfo.mapper.FaultTypeMapper;
import com.zjft.usp.anyfix.baseinfo.model.FaultType;
import com.zjft.usp.anyfix.baseinfo.service.FaultTypeService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderServiceService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.service.UasFeignService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 故障现象表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Service
public class FaultTypeServiceImpl extends ServiceImpl<FaultTypeMapper, FaultType> implements FaultTypeService {

    @Resource
    private UasFeignService uasFeignService;

    @Autowired
    private DemanderServiceService demanderServiceService;
    @Override
    public List<FaultType> listEnableFaultTypeByCorp(Long customCorp) {
        QueryWrapper<FaultType> queryWrapper = new QueryWrapper();
        queryWrapper.eq("enabled", EnabledEnum.YES.getCode()).eq("demander_corp", customCorp);
        queryWrapper.orderByAsc("name");
        return this.list(queryWrapper);
    }

    @Override
    public ListWrapper<FaultTypeDto> query(FaultTypeFilter faultTypeFilter) {
        QueryWrapper<FaultType> queryWrapper = new QueryWrapper<>();
        if(LongUtil.isZero(faultTypeFilter.getDemanderCorp())) {
            List<Long> demanderCorpList =  demanderServiceService.listDemanderCorpId(faultTypeFilter.getCorpId());
            if(CollectionUtil.isNotEmpty(demanderCorpList)) {
                queryWrapper.in("demander_corp",demanderCorpList);
                queryWrapper.orderByAsc("demander_corp");
            }else {
                return new ListWrapper<>();
            }
        } else {
            queryWrapper.or().eq("demander_corp",faultTypeFilter.getDemanderCorp());
        }
        if (StrUtil.isNotBlank(faultTypeFilter.getName())) {
            queryWrapper.like("name", StrUtil.trimToEmpty(faultTypeFilter.getName()));
        }
        if (StrUtil.isNotBlank(faultTypeFilter.getEnabled())) {
            queryWrapper.eq("enabled", faultTypeFilter.getEnabled());
        }
        Page page = new Page(faultTypeFilter.getPageNum(), faultTypeFilter.getPageSize());
        IPage<FaultType> faultTypeIPage = this.page(page, queryWrapper);
        List<FaultTypeDto> faultTypeDtoList = new ArrayList<>();
        List<FaultType> faultTypeList = faultTypeIPage.getRecords();
        if (CollectionUtil.isNotEmpty(faultTypeList)) {
            List<Long> corpIdList = faultTypeList.stream().map(e -> e.getDemanderCorp()).distinct().collect(Collectors.toList());
            Result corpResult = this.uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList));
            Map<Long, String> corpMap;
            if(corpResult !=null && corpResult.getCode() == Result.SUCCESS) {
                corpMap = JsonUtil.parseObject(JsonUtil.toJson(corpResult.getData()), Map.class);
            } else {
                corpMap = new HashMap<>();
            }
            for (FaultType faultType: faultTypeIPage.getRecords()) {
                FaultTypeDto faultTypeDto = new FaultTypeDto();
                BeanUtils.copyProperties(faultType, faultTypeDto);
                faultTypeDto.setDemanderCorpName(corpMap.get(faultType.getDemanderCorp()));
                faultTypeDtoList.add(faultTypeDto);
            }
        }
        return ListWrapper.<FaultTypeDto>builder().list(faultTypeDtoList).total(faultTypeIPage.getTotal()).build();
    }

    /**
     * 故障现象映射
     *
     * @param demanderCorp
     * @return
     * @author zgpi
     * @date 2019/11/12 18:14
     **/
    @Override
    public Map<Integer, String> mapIdAndName(Long demanderCorp) {
        List<FaultType> faultTypeList = this.list(new QueryWrapper<FaultType>().eq("demander_corp", demanderCorp));
        Map<Integer, String> map = new HashMap<>();
        for (FaultType faultType : faultTypeList) {
            map.put(faultType.getId(), faultType.getName());
        }
        return map;
    }

    @Override
    public void save(FaultType faultType, UserInfo userInfo, ReqParam reqParam) {
        if(StringUtils.isEmpty(faultType.getName())) {
            throw new AppException("故障现象名称不能为空");
        }
        // 委托商为空时设置为自己
        if (LongUtil.isZero(faultType.getDemanderCorp())) {
            faultType.setDemanderCorp(reqParam.getCorpId());
        }
        QueryWrapper<FaultType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",faultType.getName());
        queryWrapper.eq("demander_corp",faultType.getDemanderCorp());
        List<FaultType> list = this.baseMapper.selectList(queryWrapper);
        if(CollectionUtil.isNotEmpty(list)) {
            throw new AppException("该故障现象名称已经存在");
        }
        faultType.setOperator(userInfo.getUserId());
        faultType.setOperateTime(DateUtil.date().toTimestamp());
        this.save(faultType);
    }

    @Override
    public void update(FaultType faultType, UserInfo userInfo) {
        StringBuilder builder = new StringBuilder();
        if(StringUtils.isEmpty(faultType.getName()) ) {
            builder.append("故障现象名称不能为空");
        }
        if(LongUtil.isZero(faultType.getDemanderCorp())) {
            builder.append("企业编号不能为空");
        }
        if(faultType.getId() == 0) {
            builder.append("故障现象编号不能为空");
        }
        if(builder.length() > 0 ){
           throw new AppException(builder.toString());
        }
        QueryWrapper<FaultType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",faultType.getName());
        queryWrapper.eq("demander_corp",faultType.getDemanderCorp());
        queryWrapper.ne("id", faultType.getId());
        List<FaultType> list = this.baseMapper.selectList(queryWrapper);
        if(CollectionUtil.isNotEmpty(list)) {
            throw new AppException("该故障现象名称已经存在");
        }
        faultType.setOperateTime(DateUtil.date().toTimestamp());
        faultType.setOperator(userInfo.getUserId());
        this.updateById(faultType);
    }
}
