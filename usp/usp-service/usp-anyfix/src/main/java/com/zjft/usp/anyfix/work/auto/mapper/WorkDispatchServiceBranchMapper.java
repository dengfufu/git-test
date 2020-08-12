package com.zjft.usp.anyfix.work.auto.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.work.auto.dto.WorkDispatchServiceBranchDto;
import com.zjft.usp.anyfix.work.auto.filter.WorkDispatchServiceBranchFilter;
import com.zjft.usp.anyfix.work.auto.model.WorkDispatchServiceBranch;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ljzhu
 * @Package com.zjft.anyfix.work.mapper
 * @date 2019-09-27 08:46
 * @note 工单分配服务网点表 work_dispatch_service_branch
 */
public interface WorkDispatchServiceBranchMapper extends BaseMapper<WorkDispatchServiceBranch> {

    /**
     * 分页查询自动分配服务网点规则
     *
     * @param workDispatchServiceBranchFilter
     * @param page
     * @return
     * @author zgpi
     * @date 2019/11/14 09:39
     **/
    List<WorkDispatchServiceBranchDto> query(@Param("filter") WorkDispatchServiceBranchFilter workDispatchServiceBranchFilter,
                                             Page page);

    /**
     * 根据条件匹配服务网点
     *
     * @param workDispatchServiceBranchDto
     * @return
     * @author zgpi
     * @date 2019/11/14 15:44
     **/
    List<WorkDispatchServiceBranchDto> matchServiceBranch(WorkDispatchServiceBranchDto workDispatchServiceBranchDto);
}
