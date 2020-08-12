package com.zjft.usp.uas.corp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.uas.corp.mapper.CorpUserTraceMapper;
import com.zjft.usp.uas.corp.model.CorpUserTrace;
import com.zjft.usp.uas.corp.service.CorpUserTraceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author: CK
 * @create: 2020-02-28 14:55
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CorpUserTraceServiceImpl extends ServiceImpl<CorpUserTraceMapper, CorpUserTrace> implements CorpUserTraceService {


}