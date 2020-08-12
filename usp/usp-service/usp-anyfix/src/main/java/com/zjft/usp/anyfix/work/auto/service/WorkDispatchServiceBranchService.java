package com.zjft.usp.anyfix.work.auto.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.work.auto.dto.WorkDispatchServiceBranchDto;
import com.zjft.usp.anyfix.work.auto.filter.WorkDispatchServiceBranchFilter;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.auto.model.WorkDispatchServiceBranch;
import com.zjft.usp.anyfix.work.request.model.WorkRequest;
import com.zjft.usp.common.model.ListWrapper;


/**
 * @author ljzhu
 * @Package com.zjft.anyfix.work.service
 * @date 2019-09-26 18:20
 * @note
 */
public interface WorkDispatchServiceBranchService extends IService<WorkDispatchServiceBranch> {

    /**
     * 分页查询自动分配服务商网点规则列表
     *
     * @param workDispatchServiceBranchFilter
     * @return
     * @author zgpi
     * @date 2019/11/14 09:43
     **/
    ListWrapper<WorkDispatchServiceBranchDto> query(WorkDispatchServiceBranchFilter workDispatchServiceBranchFilter);

    WorkDispatchServiceBranchDto getById(Long id);

    /**
     * 添加工单分配服务网点
     * @param workDispatchServiceBranch
     * @return
     */
    void add(WorkDispatchServiceBranchDto workDispatchServiceBranch);

    /**
     * 修改工单分配服务网点
     *
     * @param workDispatchServiceBranch
     * @return
     * @author zgpi
     * @date 2019/11/14 17:29
     **/
    void mod(WorkDispatchServiceBranchDto workDispatchServiceBranch);

    /**
     * 删除工单分配服务网点
     *
     * @param id
     * @return
     * @author zgpi
     * @date 2019/11/14 17:29
     **/
    void delById(Long id);

    /**
     * 自动分配服务商和服务商网点，成功后更新work_deal的服务商，服务商网点，分配时间，工单状态状态待受理
     * @param workRequest
     * @param workDeal
     * @return 返回受理方式
     */
    Integer autoDispatchServiceBranch(WorkRequest workRequest, WorkDeal workDeal);

}
