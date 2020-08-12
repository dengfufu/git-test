package com.zjft.usp.anyfix.work.auto.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.work.auto.dto.WorkAssignModeDto;
import com.zjft.usp.anyfix.work.auto.filter.WorkAssignModeFilter;
import com.zjft.usp.anyfix.work.auto.model.WorkAssignMode;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.request.model.WorkRequest;
import com.zjft.usp.common.model.ListWrapper;

/**
 * <p>
 * 派单模式配置 服务类
 * </p>
 *
 * @author zphu
 * @since 2019-09-26
 */
public interface WorkAssignModeService extends IService<WorkAssignMode> {

//    /**
//     *根据工单请求获取派单模式
//     *
//     * @param workId
//     * @return WorkAssignMode
//     * @author zphu
//     * @date 2019/9/26 10:25
//     * @throws
//    **/
//    WorkAssignMode getAssignMode(Long workId);
//
//    /**
//     * 根据实体类的非空值获取实体
//     *
//     * @param workAssignMode
//     * @return com.zjft.anyfix.work.model.WorkAssignMode
//     * @author zphu
//     * @date 2019/9/26 13:39
//     * @throws
//    **/
//    WorkAssignMode selectByEntity(WorkAssignMode workAssignMode);
//
//    /**
//     * 自动派单
//     *
//     * @param workId
//     * @param workAssignMode
//     * @return Integer
//     * @author zphu
//     * @date 2019/9/26 14:57
//     * @throws
//    **/
//    Integer autoMode(Long workId, WorkAssignMode workAssignMode);


    /**
     * 分页查询自动派单规则列表
     *
     * @param workAssignModeFilter
     * @return
     * @author zgpi
     * @date 2019/11/14 11:01
     **/
    ListWrapper<WorkAssignModeDto> query(WorkAssignModeFilter workAssignModeFilter);

    /**
     * 自动派单
     *
     * @param workRequest
     * @param workDeal
     * @return
     * @author zgpi
     * @date 2019/11/13 13:40
     **/
    Integer autoAssign(WorkRequest workRequest, WorkDeal workDeal);

    WorkAssignModeDto getWorkAssignMode(Long id);

    Integer add(WorkAssignModeDto workAssignMode);

    Integer mod(WorkAssignModeDto workAssignMode);

    void delById(Long id);
}
