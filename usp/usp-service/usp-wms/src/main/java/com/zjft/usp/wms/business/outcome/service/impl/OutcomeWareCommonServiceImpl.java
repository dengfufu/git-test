package com.zjft.usp.wms.business.outcome.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.outcome.dto.OutcomeStatDto;
import com.zjft.usp.wms.business.outcome.dto.OutcomeWareCommonDto;
import com.zjft.usp.wms.business.outcome.filter.OutcomeFilter;
import com.zjft.usp.wms.business.outcome.mapper.OutcomeWareCommonMapper;
import com.zjft.usp.wms.business.outcome.model.OutcomeWareCommon;
import com.zjft.usp.wms.business.outcome.service.OutcomeWareCommonService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 出库信息共用表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-13
 */
@Service
public class OutcomeWareCommonServiceImpl extends ServiceImpl<OutcomeWareCommonMapper, OutcomeWareCommon> implements OutcomeWareCommonService {

    @Override
    public void updateOutcomeStatus(OutcomeWareCommon outcomeWareCommon) {
        UpdateWrapper<OutcomeWareCommon> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id", outcomeWareCommon.getId());
        this.update(outcomeWareCommon, updateWrapper);
    }

    @Override
    public List<OutcomeWareCommonDto> listByPage(OutcomeFilter outcomeFilter, ReqParam reqParam, Page page) {
        return this.baseMapper.listByPage(outcomeFilter,page);
    }

    @Override
    public OutcomeWareCommonDto getById(OutcomeFilter outcomeFilter) {
        return this.baseMapper.getById(outcomeFilter);
    }

    @Override
    public List<OutcomeStatDto> countByStatus(OutcomeFilter outcomeFilter) {
        return this.baseMapper.countByStatus(outcomeFilter);
    }

    @Override
    public Map<Long, OutcomeWareCommon> mapIdAndObject(List<Long> idList) {
        Map<Long, OutcomeWareCommon> map = new HashMap<>();
        if(CollectionUtil.isEmpty(idList)) {
            return map;
        }
        QueryWrapper<OutcomeWareCommon> queryWrapper = new QueryWrapper<OutcomeWareCommon>().in("id", idList);
        List<OutcomeWareCommon> list = this.list(queryWrapper);
        list.forEach(outcomeWareCommon -> {
            map.put(outcomeWareCommon.getId(), outcomeWareCommon);
        });
        return map;
    }

}
