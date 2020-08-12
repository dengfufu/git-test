package com.zjft.usp.anyfix.corp.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.corp.user.mapper.DeviceBranchUserTraceMapper;
import com.zjft.usp.anyfix.corp.user.model.DeviceBranchUserTrace;
import com.zjft.usp.anyfix.corp.user.service.DeviceBranchUserTraceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 设备网点人员表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-26
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceBranchUserTraceServiceImpl extends ServiceImpl<DeviceBranchUserTraceMapper, DeviceBranchUserTrace> implements DeviceBranchUserTraceService {

}
