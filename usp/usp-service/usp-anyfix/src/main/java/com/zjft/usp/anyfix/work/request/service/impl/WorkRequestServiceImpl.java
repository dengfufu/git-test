package com.zjft.usp.anyfix.work.request.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.anyfix.work.request.mapper.WorkRequestMapper;
import com.zjft.usp.anyfix.work.request.model.WorkRequest;
import com.zjft.usp.anyfix.work.request.service.WorkRequestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 工单服务请求表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkRequestServiceImpl extends ServiceImpl<WorkRequestMapper, WorkRequest> implements WorkRequestService {

    /**
     * 根据服务商和超时时间获取应自动确认服务的工单
     *
     * @param serviceCorp
     * @return
     */
    @Override
    public List<WorkDeal> listAutoServiceConfirm(Long serviceCorp) {
        return this.baseMapper.listAutoServiceConfirm(serviceCorp);
    }

    /**
     * 根据服务商和超时时间获取应自动确认费用的工单
     *
     * @param serviceCorp
     * @return
     */
    @Override
    public List<WorkDeal> listAutoFeeConfirm(Long serviceCorp) {
        return this.baseMapper.listAutoFeeConfirm(serviceCorp);
    }

    /**
     * 根据工单编号列表获取工单编号和服务请求的映射
     *
     * @param workIdList
     * @return
     */
    @Override
    public Map<Long, WorkRequest> mapByWorkIdList(List<Long> workIdList) {
        Map<Long, WorkRequest> map = new HashMap<>();
        if (CollectionUtil.isEmpty(workIdList)) {
            return map;
        }
        List<WorkRequest> workRequestList = this.list(new QueryWrapper<WorkRequest>().in("work_id", workIdList));
        if (CollectionUtil.isNotEmpty(workRequestList)) {
            map = workRequestList.stream().collect(Collectors.toMap(WorkRequest::getWorkId, workRequest -> workRequest));
        }
        return map;
    }

}
