package com.zjft.usp.anyfix.work.auto.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.work.auto.dto.WorkAssignModeDto;
import com.zjft.usp.anyfix.work.auto.dto.WorkAssignRuleDto;
import com.zjft.usp.anyfix.work.auto.mapper.WorkAssignRuleMapper;
import com.zjft.usp.anyfix.work.auto.model.WorkAssignRule;
import com.zjft.usp.anyfix.work.auto.service.WorkAssignRuleService;
import com.zjft.usp.common.utils.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p>
 * 自动派单规则 服务实现类
 * </p>
 *
 * @author zphu
 * @since 2019-09-26
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkAssignRuleServiceImpl extends ServiceImpl<WorkAssignRuleMapper, WorkAssignRule> implements WorkAssignRuleService {

    @Resource
    private WorkAssignRuleMapper workAssignRuleMapper;


    @Override
    public List<WorkAssignRule> list(WorkAssignRuleDto workAssignRuleDto) {

        return null;
    }

    @Override
    public Integer add(WorkAssignRule workAssignRule) {

        workAssignRule.setId(KeyUtil.getId());
        return workAssignRuleMapper.insert(workAssignRule);
    }

    @Override
    public Integer mod(WorkAssignRule workAssignRule) {

        return workAssignRuleMapper.updateById(workAssignRule);
    }

    @Override
    public Integer delById(Long id) {
        return workAssignRuleMapper.deleteById(id);
    }

    @Override
    public Integer addByWorkAssignMode(WorkAssignModeDto workAssignMode) {

        WorkAssignRule workAssignRule = new WorkAssignRule();

        BeanUtils.copyProperties(workAssignMode, workAssignRule);
        workAssignRule.setId(workAssignMode.getAssignRule());

        return this.workAssignRuleMapper.insert(workAssignRule);
    }
}
