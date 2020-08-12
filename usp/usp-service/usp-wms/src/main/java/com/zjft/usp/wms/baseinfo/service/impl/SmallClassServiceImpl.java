package com.zjft.usp.wms.baseinfo.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.wms.baseinfo.dto.LargeClassDto;
import com.zjft.usp.wms.baseinfo.dto.SmallClassDto;
import com.zjft.usp.wms.baseinfo.model.LargeClass;
import com.zjft.usp.wms.baseinfo.model.SmallClass;
import com.zjft.usp.wms.baseinfo.mapper.SmallClassMapper;
import com.zjft.usp.wms.baseinfo.service.SmallClassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 业务小类表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
public class SmallClassServiceImpl extends ServiceImpl<SmallClassMapper, SmallClass> implements SmallClassService {

    @Override
    public List<SmallClassDto> query(SmallClassDto smallClassDto) {
        QueryWrapper<SmallClass> queryWrapper = new QueryWrapper<>();

        if(StrUtil.isNotBlank(smallClassDto.getEnabled())){
            queryWrapper.eq("enabled",smallClassDto.getEnabled());
        }

        if(LongUtil.isNotZero(smallClassDto.getCorpId())){
            queryWrapper.eq("corp_id",smallClassDto.getCorpId());
        }
        queryWrapper.orderByAsc("sort_no");

        List<SmallClass> smallClassList = this.list(queryWrapper);
        List<SmallClassDto> smallClassDtoList = new ArrayList<>();
        if(smallClassList != null && smallClassList.size() > 0){
            for(SmallClass smallClass : smallClassList){
                SmallClassDto tmpDto = new SmallClassDto();
                BeanUtils.copyProperties(smallClass, tmpDto);
                smallClassDtoList.add(tmpDto);
            }
        }
        return smallClassDtoList;
    }

    @Override
    public Map<Integer, String> mapClassIdAndName(Long corpId) {
        Map<Integer, String> map = new HashMap<>(128);
        QueryWrapper<SmallClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("corp_id",corpId);
        List<SmallClass> smallClassList = this.list(queryWrapper);
        if(smallClassList != null && smallClassList.size() > 0){
            for (SmallClass smallClass: smallClassList){
                map.put(smallClass.getSmallClassId(), smallClass.getName());
            }
        }
        return map;
    }
}
