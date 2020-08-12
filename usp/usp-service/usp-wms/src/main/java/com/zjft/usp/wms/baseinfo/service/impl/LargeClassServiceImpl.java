package com.zjft.usp.wms.baseinfo.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.wms.baseinfo.dto.LargeClassDto;
import com.zjft.usp.wms.baseinfo.model.LargeClass;
import com.zjft.usp.wms.baseinfo.mapper.LargeClassMapper;
import com.zjft.usp.wms.baseinfo.service.LargeClassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 业务大类表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
public class LargeClassServiceImpl extends ServiceImpl<LargeClassMapper, LargeClass> implements LargeClassService {

    @Override
    public List<LargeClassDto> query(LargeClassDto largeClassDto) {
        QueryWrapper<LargeClass> queryWrapper = new QueryWrapper<>();
        if(StrUtil.isNotBlank(largeClassDto.getEnabled())){
            queryWrapper.eq("enabled",largeClassDto.getEnabled());
        }
        if(LongUtil.isNotZero(largeClassDto.getCorpId())){
            queryWrapper.eq("corp_id",largeClassDto.getCorpId());
        }
        queryWrapper.orderByAsc("sort_no");
        List<LargeClass> largeClassList = this.list(queryWrapper);
        List<LargeClassDto> largeClassDtoList = new ArrayList<>();
        if(largeClassList != null && largeClassList.size() > 0){
            for(LargeClass largeClass : largeClassList){
                LargeClassDto tmpDto = new LargeClassDto();
                BeanUtils.copyProperties(largeClass, tmpDto);
                largeClassDtoList.add(tmpDto);
            }
        }
        return largeClassDtoList;
    }

    @Override
    public Map<Integer, String> mapClassIdAndName(Long corpId) {
        Map<Integer, String> map = new HashMap<>(256);
        QueryWrapper<LargeClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("corp_id",corpId);
        List<LargeClass> largeClassList = this.list(queryWrapper);
        if(largeClassList != null && largeClassList.size() > 0){
            for (LargeClass largeClass: largeClassList){
                map.put(largeClass.getLargeClassId(), largeClass.getName());
            }
        }
        return map;
    }
}
