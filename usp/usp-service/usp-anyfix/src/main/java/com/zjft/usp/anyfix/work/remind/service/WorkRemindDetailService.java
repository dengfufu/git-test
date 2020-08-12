package com.zjft.usp.anyfix.work.remind.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.work.remind.dto.WorkRemindDetailDto;
import com.zjft.usp.anyfix.work.remind.model.WorkRemindDetail;

import java.util.List;

/**
 * @Author: JFZOU
 * @Date: 2020-04-20 10:39
 * @Version 1.0
 */
public interface WorkRemindDetailService extends IService<WorkRemindDetail> {

    List<WorkRemindDetailDto> listByRemindId(Long remindId);

    void deleteByRemindId(Long remindId);
}
