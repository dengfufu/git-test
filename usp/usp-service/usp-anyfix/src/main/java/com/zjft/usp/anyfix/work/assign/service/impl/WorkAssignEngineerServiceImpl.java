package com.zjft.usp.anyfix.work.assign.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.work.assign.mapper.WorkAssignEngineerMapper;
import com.zjft.usp.anyfix.work.assign.model.WorkAssign;
import com.zjft.usp.anyfix.work.assign.model.WorkAssignEngineer;
import com.zjft.usp.anyfix.work.assign.service.WorkAssignEngineerService;
import com.zjft.usp.anyfix.work.assign.service.WorkAssignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 派单工程师表 服务实现类
 * </p>
 *
 * @author zphu
 * @since 2019-09-23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkAssignEngineerServiceImpl extends ServiceImpl<WorkAssignEngineerMapper, WorkAssignEngineer> implements WorkAssignEngineerService {

    @Autowired
    private WorkAssignService workAssignService;
    @Resource
    private WorkAssignEngineerMapper workAssignEngineerMapper;

    @Override
    public Integer add(WorkAssignEngineer workAssignEngineer) {
        return workAssignEngineerMapper.insert(workAssignEngineer);
    }

    @Override
    public Integer add(Long assignId, Long engineerId) {
        return this.add(new WorkAssignEngineer(assignId, engineerId));
    }

    @Override
    public void delete(WorkAssignEngineer workAssignEngineer) {
        workAssignEngineerMapper.delete(new QueryWrapper<WorkAssignEngineer>()
                .eq("assign_id", workAssignEngineer.getAssignId())
                .eq("engineer_id", workAssignEngineer.getEngineerId()));
    }

    @Override
    public void delByAssignId(Long assignId) {
        workAssignEngineerMapper.delete(new QueryWrapper<WorkAssignEngineer>()
                .eq("assign_id", assignId));
    }

    /**
     * 根据工单ID获得派单工程师编号列表
     *
     * @param workId
     * @return
     * @author zgpi
     * @date 2020/2/17 16:58
     */
    @Override
    public List<Long> listEngineerByWorkId(Long workId) {
        WorkAssign workAssign = workAssignService.getOne(new QueryWrapper<WorkAssign>().eq("work_id", workId)
                .eq("enabled", "Y"));
        if (workAssign != null) {
            List<WorkAssignEngineer> list = this.list(new QueryWrapper<WorkAssignEngineer>()
                    .eq("assign_id", workAssign.getAssignId()));
            return list.stream().map(e -> e.getEngineerId()).collect(Collectors.toList());
        }
        return null;
    }
}
