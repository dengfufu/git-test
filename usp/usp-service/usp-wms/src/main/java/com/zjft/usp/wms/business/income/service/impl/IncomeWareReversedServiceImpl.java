package com.zjft.usp.wms.business.income.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.wms.business.income.model.IncomeWareReversed;
import com.zjft.usp.wms.business.income.mapper.IncomeWareReversedMapper;
import com.zjft.usp.wms.business.income.service.IncomeWareReversedService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 入库明细通用销账表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-19
 */
@Service
public class IncomeWareReversedServiceImpl extends ServiceImpl<IncomeWareReversedMapper, IncomeWareReversed> implements IncomeWareReversedService {

    /**
     * 根据入库明细号获取销账明细号list
     *
     * @author canlei
     * @param detailId
     * @return
     */
    @Override
    public List<Long> listBookIdByDetailId(Long detailId) {
        List<Long> list = new ArrayList<>();
        if (LongUtil.isZero(detailId)) {
            return list;
        }
        List<IncomeWareReversed> incomeWareReversedList = this.list(new QueryWrapper<IncomeWareReversed>()
                .eq("detail_id", detailId));
        if (CollectionUtil.isNotEmpty(incomeWareReversedList)) {
            list = incomeWareReversedList.stream().map(incomeWareReversed -> incomeWareReversed.getBookId())
                    .collect(Collectors.toList());
        }
        return list;
    }

    /**
     *
     *
     * @author
     * @param detailId
     */
    @Override
    public void deleteByDetailId(Long detailId) {
        if (LongUtil.isZero(detailId)) {
            throw  new AppException("入库明细编号不能为空！");
        }
        QueryWrapper<IncomeWareReversed> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("detail_id", detailId);
        this.remove(queryWrapper);
    }
}
