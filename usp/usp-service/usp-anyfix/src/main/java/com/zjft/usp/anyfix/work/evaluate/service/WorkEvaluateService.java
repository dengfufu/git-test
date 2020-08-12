package com.zjft.usp.anyfix.work.evaluate.service;

import com.zjft.usp.anyfix.work.evaluate.dto.WorkEvaluateDto;
import com.zjft.usp.anyfix.work.evaluate.model.WorkEvaluate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户评价表 服务类
 * </p>
 *
 * @author zphu
 * @since 2019-09-23
 */
public interface WorkEvaluateService extends IService<WorkEvaluate> {

    /**
     * 添加工单评价
     *
     * @param workEvaluateDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author zphu
     * @date 2019/9/24 17:02
     * @throws
    **/
    void addWorkEvaluate(WorkEvaluateDto workEvaluateDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 根据提供的workId查询评价详细信息
     *
     * @param workId
     * @return
     * @author zphu
     * @date 2019/9/25 15:52
     * @throws
     **/
    WorkEvaluateDto selectByWorkId(Long workId);

    /**
     * 根据提供的workIdList查询评价详细信息
     * @param corpId
     * @param workIdList
     * @return java.util.List<com.zjft.usp.anyfix.work.evaluate.dto.WorkEvaluateDto>
     * @author zphu
     * @date 2019/11/1 10:22
     * @throws
    **/
    List<WorkEvaluateDto> selectByWorkIds(List<Long> workIdList,Long corpId);

    /**
     * 根据日期查询工程师收到的评价记录
     *
     * @param workEvaluateDto
     * @param userInfo
     * @param reqParam
     * @return java.util.List<com.zjft.usp.anyfix.work.evaluate.dto.WorkEvaluateDto>
     * @author zphu
     * @date 2019/10/30 14:18
     * @throws
    **/
    List<WorkEvaluateDto> listByDate(WorkEvaluateDto workEvaluateDto, UserInfo userInfo,ReqParam reqParam);

    /**
     * 根据指标获取工单列表
     *
     * @param workEvaluateDto
     * @param userInfo
     * @param reqParam
     * @return java.util.List<com.zjft.usp.anyfix.work.evaluate.dto.WorkEvaluateDto>
     * @author zphu
     * @date 2019/10/31 11:08
     * @throws
    **/
    List<WorkEvaluateDto> listByIndex(WorkEvaluateDto workEvaluateDto, UserInfo userInfo,ReqParam reqParam);

    /**
     * 统计公司某个指标的数量
     * @param workEvaluateDto
     * @param corpId
     * @return
     */
    Map<String,Object> listWorkEvaluateCountDto(WorkEvaluateDto workEvaluateDto, Long corpId);
}
