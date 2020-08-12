package com.zjft.usp.anyfix.work.auto.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.work.auto.dto.WorkAssignModeDto;
import com.zjft.usp.anyfix.work.auto.dto.WorkAssignRuleDto;
import com.zjft.usp.anyfix.work.auto.model.WorkAssignRule;

import java.util.List;

/**
 * <p>
 * 自动派单规则 服务类
 * </p>
 *
 * @author zphu
 * @since 2019-09-26
 */
public interface WorkAssignRuleService extends IService<WorkAssignRule> {

    List<WorkAssignRule> list(WorkAssignRuleDto workAssignRuleDto);

    /**
     * 智能派单规则
     * @param workAssignRule
     * @return
     */
    Integer add(WorkAssignRule workAssignRule);

    Integer mod(WorkAssignRule workAssignRule);

    Integer delById(Long id);

    Integer addByWorkAssignMode(WorkAssignModeDto workAssignMode);
}
