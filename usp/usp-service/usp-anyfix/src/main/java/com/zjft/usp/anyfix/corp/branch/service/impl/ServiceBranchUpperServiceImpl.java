package com.zjft.usp.anyfix.corp.branch.service.impl;

import com.zjft.usp.anyfix.corp.branch.model.ServiceBranchUpper;
import com.zjft.usp.anyfix.corp.branch.mapper.ServiceBranchUpperMapper;
import com.zjft.usp.anyfix.corp.branch.service.ServiceBranchUpperService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>
 * 服务网点上级表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2020-03-09
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ServiceBranchUpperServiceImpl extends ServiceImpl<ServiceBranchUpperMapper, ServiceBranchUpper> implements ServiceBranchUpperService {

}
