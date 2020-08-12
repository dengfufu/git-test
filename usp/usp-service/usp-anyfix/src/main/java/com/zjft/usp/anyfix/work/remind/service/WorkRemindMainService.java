package com.zjft.usp.anyfix.work.remind.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.work.remind.dto.WorkRemindMainDto;
import com.zjft.usp.anyfix.work.remind.filter.WorkRemindFilter;
import com.zjft.usp.anyfix.work.remind.model.WorkRemindMain;

import java.util.List;

/**
 * @Author: JFZOU
 * @Date: 2020-04-20 10:39
 * @Version 1.0
 */
public interface WorkRemindMainService extends IService<WorkRemindMain> {

    List<WorkRemindMainDto> queryWorkRemind(WorkRemindFilter workRemindFilter, Page page);
}
