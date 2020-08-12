package com.zjft.usp.anyfix.baseinfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.baseinfo.dto.WorkConditionDto;
import com.zjft.usp.anyfix.work.auto.dto.WorkAssignModeDto;
import com.zjft.usp.anyfix.work.auto.dto.WorkDispatchServiceBranchDto;
import com.zjft.usp.anyfix.work.auto.dto.WorkDispatchServiceCorpDto;
import com.zjft.usp.anyfix.baseinfo.model.WorkCondition;

import java.util.List;

/**
 * @author ljzhu
 * @Package com.zjft.anyfix.work.service
 * @date 2019-09-26 18:20
 * @note
 */
public interface WorkConditionService extends IService<WorkCondition> {

    List<WorkCondition> list(WorkConditionDto workConditionDto);

    int add(WorkConditionDto workCondition);

    int mod(WorkConditionDto workCondition);

    void delById(Long id);

    int addWorkCondition(WorkDispatchServiceCorpDto dto, long conditionId);
    int addWorkCondition(WorkDispatchServiceBranchDto dto, long conditionId);
    int addWorkCondition(WorkAssignModeDto dto, long conditionId);

}
