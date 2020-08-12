package com.zjft.usp.anyfix.work.remind.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.work.remind.dto.WorkRemindMainDto;
import com.zjft.usp.anyfix.work.remind.filter.WorkRemindFilter;
import com.zjft.usp.anyfix.work.remind.mapper.WorkRemindMainMapper;
import com.zjft.usp.anyfix.work.remind.model.WorkRemindMain;
import com.zjft.usp.anyfix.work.remind.service.WorkRemindMainService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: JFZOU
 * @Date: 2020-04-20 11:20
 * @Version 1.0
 */
@Service
public class WorkRemindMainServiceImpl extends ServiceImpl<WorkRemindMainMapper, WorkRemindMain> implements
        WorkRemindMainService {
    @Resource
    private WorkRemindMainMapper workRemindMainMapper;

    @Override
    public List<WorkRemindMainDto> queryWorkRemind(WorkRemindFilter workRemindFilter, Page page) {
        return workRemindMainMapper.queryWorkRemind(workRemindFilter, page);
    }
}
