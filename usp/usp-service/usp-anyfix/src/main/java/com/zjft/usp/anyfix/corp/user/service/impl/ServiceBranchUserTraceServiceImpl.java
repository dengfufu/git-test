package com.zjft.usp.anyfix.corp.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.corp.user.mapper.ServiceBranchUserTraceMapper;
import com.zjft.usp.anyfix.corp.user.model.ServiceBranchUserTrace;
import com.zjft.usp.anyfix.corp.user.service.ServiceBranchUserTraceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: CK
 * @create: 2020-02-28 15:28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ServiceBranchUserTraceServiceImpl extends ServiceImpl<ServiceBranchUserTraceMapper, ServiceBranchUserTrace> implements ServiceBranchUserTraceService {
}
