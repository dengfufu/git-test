package com.zjft.usp.anyfix.work.transfer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.work.transfer.filter.WorkTransferFilter;
import com.zjft.usp.anyfix.work.transfer.mapper.WorkTransferMapper;
import com.zjft.usp.anyfix.work.transfer.model.WorkTransfer;
import com.zjft.usp.anyfix.work.transfer.service.WorkTransferService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


/**
 * <p>
 * 工单流转表 服务实现类
 * </p>
 *
 * @author zphu
 * @since 2019-09-25
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkTransferServiceImpl extends ServiceImpl<WorkTransferMapper, WorkTransfer> implements WorkTransferService {

    @Override
    public List<WorkTransfer> queryWorkTransferForCost(WorkTransferFilter workTransferFilter) {
        QueryWrapper queryWrapper  = new QueryWrapper<WorkTransfer>();

        if(CollectionUtil.isNotEmpty(workTransferFilter.getModeList())){
            queryWrapper.in("mode",workTransferFilter.getModeList());
        }

        if(StrUtil.isNotEmpty(workTransferFilter.getOperateMonth())){
            queryWrapper.eq("left(operate_time, 7)",workTransferFilter.getOperateMonth());
        }

        /**必须升序排序*/
        queryWrapper.orderByAsc("work_id","operate_time");

        List<WorkTransfer> workTransferList = list(queryWrapper);

        return workTransferList;
    }

    /**
     * workIdList
     * @date 2020/3/15
     * @param workIdSet
     * @return java.util.List<com.zjft.usp.anyfix.work.transfer.model.WorkTransfer>
     */
    @Override
    public List<WorkTransfer> queryWorkTransfer(Set<Long> workIdSet) {
        return this.list(new QueryWrapper<WorkTransfer>().in("work_id",workIdSet)
                .orderByAsc("work_id","operate_time"));
    }
}
