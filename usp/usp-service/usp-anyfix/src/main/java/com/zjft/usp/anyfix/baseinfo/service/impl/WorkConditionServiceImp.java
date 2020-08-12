package com.zjft.usp.anyfix.baseinfo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.dto.WorkConditionDto;
import com.zjft.usp.anyfix.work.auto.dto.WorkAssignModeDto;
import com.zjft.usp.anyfix.work.auto.dto.WorkDispatchServiceBranchDto;
import com.zjft.usp.anyfix.work.auto.dto.WorkDispatchServiceCorpDto;
import com.zjft.usp.anyfix.baseinfo.mapper.WorkConditionMapper;
import com.zjft.usp.anyfix.baseinfo.model.WorkCondition;
import com.zjft.usp.anyfix.baseinfo.service.WorkConditionService;
import com.zjft.usp.common.utils.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ljzhu
 * @Package com.zjft.anyfix.work.service.impl
 * @date 2019-09-26 18:21
 * @note
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkConditionServiceImp extends ServiceImpl<WorkConditionMapper, WorkCondition> implements WorkConditionService {

    @Resource
    private WorkConditionMapper workConditionMapper;


    @Override
    public List<WorkCondition> list(WorkConditionDto workConditionDto) {
        return  workConditionMapper.listWorkCondition(workConditionDto.getCustomCorp(),workConditionDto.getWorkType(),workConditionDto.getLargeClassId(),
                workConditionDto.getSmallClassId(),workConditionDto.getBrandId(),workConditionDto.getModelId(),workConditionDto.getDistrict(),
                workConditionDto.getDeviceBranch(),workConditionDto.getDeviceId());
    }

    @Override
    public int add(WorkConditionDto workCondition) {
        workCondition.setId(KeyUtil.getId());
        return workConditionMapper.insert(workCondition);
    }

    @Override
    public int mod(WorkConditionDto workCondition) {
        return workConditionMapper.updateById(workCondition);
    }

    @Override
    public void delById(Long id) {
        workConditionMapper.deleteById(id);
    }


    @Override
    public int addWorkCondition(WorkDispatchServiceCorpDto dto, long conditionId) {
        WorkCondition workCondition = new WorkCondition();
        BeanUtils.copyProperties(dto, workCondition);
        workCondition.setId(conditionId);
        return workConditionMapper.insert(workCondition);
    }

    @Override
    public int addWorkCondition(WorkDispatchServiceBranchDto dto, long conditionId) {
        WorkCondition workCondition = new WorkCondition();
        BeanUtils.copyProperties(dto, workCondition);
        workCondition.setId(conditionId);
        return workConditionMapper.insert(workCondition);
    }

    @Override
    public int addWorkCondition(WorkAssignModeDto dto, long conditionId) {
        WorkCondition workCondition = new WorkCondition();
        BeanUtils.copyProperties(dto, workCondition);
        workCondition.setId(conditionId);
        return workConditionMapper.insert(workCondition);
    }
}
