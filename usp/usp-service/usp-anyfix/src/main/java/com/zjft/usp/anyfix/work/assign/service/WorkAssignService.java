package com.zjft.usp.anyfix.work.assign.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.corp.user.dto.ServiceBranchUserDto;
import com.zjft.usp.anyfix.work.assign.dto.WorkAssignDto;
import com.zjft.usp.anyfix.work.assign.filter.WorkAssignFilter;
import com.zjft.usp.anyfix.work.assign.model.WorkAssign;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;

/**
 * @author zphu
 * @date 2019/9/23 9:21
 * @Version 1.0
 **/
public interface WorkAssignService extends IService<WorkAssign>{

    /**
     * 可派单工程师列表
     *
     * @author zgpi
     * @date 2019/10/12 2:19 下午
     * @param workAssignFilter
     * @return
     **/
    ListWrapper<ServiceBranchUserDto> listEngineer(WorkAssignFilter workAssignFilter);

    /**
     * 派单给工程师
     *
     * @param workAssignDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author zphu
     * @date 2019/9/29 11:06
     * @throws
    **/
    Long assignWork(WorkAssignDto workAssignDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 转派单给工程师
     *
     * @author zgpi
     * @date 2019/10/12 5:31 下午
     * @param workAssignDto
     * @param userInfo
     * @param reqParam
     * @return
     **/
    void turnAssignWork(WorkAssignDto workAssignDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 将工单之前的派工改为无效
     *
     * @author zgpi
     * @date 2019/10/14 11:00 上午
     * @param workId
     * @return
     **/
    void modAssignNotEnable(Long workId);

    /**
     * 派单给工程师后提醒工程师
     * @author ljzhu
     * @param workId
     */
    void assignWorkListener(long workId);

}
