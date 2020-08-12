package com.zjft.usp.anyfix.baseinfo.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.anyfix.baseinfo.enums.EnabledEnum;
import com.zjft.usp.anyfix.baseinfo.model.ServiceEvaluateTag;
import com.zjft.usp.anyfix.baseinfo.mapper.ServiceEvaluateTagMapper;
import com.zjft.usp.anyfix.baseinfo.service.ServiceEvaluateTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.utils.LongUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务评价标签表 服务实现类
 * </p>
 *
 * @author zphu
 * @since 2019-09-24
 */
@Service
public class ServiceEvaluateTagServiceImpl extends ServiceImpl<ServiceEvaluateTagMapper, ServiceEvaluateTag> implements ServiceEvaluateTagService {

    @Override
    public List<ServiceEvaluateTag> listServiceEvaluateTag(Long serviceCrop) {
        Assert.notNull(serviceCrop,"服务商不能为空");
        return this.list(new QueryWrapper<ServiceEvaluateTag>().eq("enabled", EnabledEnum.YES.getCode()).eq("service_corp", serviceCrop));
    }

    @Override
    public Map<Integer, String> mapIdAndNameByCorp(Long serviceCorp) {
        Map<Integer, String> map = new HashMap<>();
        if(LongUtil.isZero(serviceCorp)){
            return map;
        }
        List<ServiceEvaluateTag> list = this.listServiceEvaluateTag(serviceCorp);
        if(list != null && list.size() > 0){
            for(ServiceEvaluateTag serviceEvaluateTag: list){
                map.put(serviceEvaluateTag.getTagId(), serviceEvaluateTag.getName());
            }
        }
        return map;
    }
}
