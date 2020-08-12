package com.zjft.usp.anyfix.work.remind.composite;

import com.zjft.usp.anyfix.work.remind.dto.WorkRemindDealDto;
import com.zjft.usp.anyfix.work.remind.dto.WorkRemindMainDto;
import com.zjft.usp.anyfix.work.remind.dto.WorkRemindTypeDto;
import com.zjft.usp.anyfix.work.remind.filter.WorkRemindFilter;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;

import java.util.List;

/**
 * 工单预警聚合接口
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-04-23 10:44
 **/
public interface WorkRemindCompoService {

    /**
     * 查询工单预警设置列表
     *
     * @param workRemindFilter
     * @return
     * @author Qiugm
     * @date 2020-04-24
     */
    ListWrapper<WorkRemindMainDto> queryWorkRemind(WorkRemindFilter workRemindFilter);

    /**
     * 查询工单预警设置详情
     *
     * @param remindId
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-04-24
     */
    WorkRemindMainDto findWorkRemind(Long remindId, ReqParam reqParam);

    /**
     * 获取工单预警类型列表
     *
     * @param
     * @return
     * @author Qiugm
     * @date 2020-04-24
     */
    List<WorkRemindTypeDto> listWorkRemindType();

    /**
     * 添加工单预警设置
     *
     * @param workRemindMainDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-04-24
     */
    void addWorkRemind(WorkRemindMainDto workRemindMainDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 修改工单预警设置
     *
     * @param workRemindMainDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-04-24
     */
    void modWorkRemind(WorkRemindMainDto workRemindMainDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 删除工单预警设置
     *
     * @param remindId
     * @return
     * @author Qiugm
     * @date 2020-04-24
     */
    void removeById(Long remindId);

    /**
     * 修改预警时间
     * 
     * @author Qiugm
     * @date 2020-05-13
     * @param workRemindDealDto
     * @param userInfo
     * @return
     */
    void modRemindTime(WorkRemindDealDto workRemindDealDto, UserInfo userInfo);
}
