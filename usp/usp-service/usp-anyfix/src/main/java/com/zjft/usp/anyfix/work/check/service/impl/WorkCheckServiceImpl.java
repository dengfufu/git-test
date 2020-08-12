package com.zjft.usp.anyfix.work.check.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.work.check.mapper.WorkCheckMapper;
import com.zjft.usp.anyfix.work.check.model.WorkCheck;
import com.zjft.usp.anyfix.work.check.service.WorkCheckService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 工单审核信息表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2020-05-11
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkCheckServiceImpl extends ServiceImpl<WorkCheckMapper, WorkCheck> implements WorkCheckService {

}
