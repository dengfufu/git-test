package com.zjft.usp.anyfix.work.support.service;

import com.zjft.usp.anyfix.work.support.dto.WorkSupportRecordDto;
import com.zjft.usp.anyfix.work.support.model.WorkSupportRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 技术支持跟踪记录表 服务类
 * </p>
 *
 * @author cxd
 * @since 2020-04-28
 */
public interface WorkSupportRecordService extends IService<WorkSupportRecord> {

    /**
     *  根据技术id查询所有工单技术支持列表
     * @date 2020/4/28
     * @param supportId
     * @return java.util.List<com.zjft.usp.anyfix.work.support.model.WorkSupportRecord>
     */
    List<WorkSupportRecord> queryBySupportId(Long supportId);

    /**
     * 添加跟踪记录
     * @date 2020/4/28
     * @param workSupportRecordDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    void addWorkSupportRecord(WorkSupportRecordDto workSupportRecordDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 根据supportId查询跟踪记录列表
     * @date 2020/4/28
     * @param supportIdList
     * @return java.util.List<com.zjft.usp.anyfix.work.support.model.WorkSupportRecord>
     */
    List<WorkSupportRecordDto> queryWorkSupportRecordList(List<Long> supportIdList);
}
