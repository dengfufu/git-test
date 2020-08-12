package com.zjft.usp.anyfix.work.evaluate.service;

import com.zjft.usp.anyfix.work.evaluate.dto.WorkEvaluateDto;
import com.zjft.usp.anyfix.work.evaluate.dto.WorkEvaluateIndexDto;
import com.zjft.usp.anyfix.work.evaluate.model.WorkEvaluateIndex;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.UserInfo;

import java.util.List;

/**
 * <p>
 * 客户评价指标表 服务类
 * </p>
 *
 * @author zphu
 * @since 2019-09-23
 */
public interface WorkEvaluateIndexService extends IService<WorkEvaluateIndex> {

    /**
     * 查询某月的评价指标信息
     *
     * @param workEvaluateDto
     * @param userInfo
     * @return
     * @author zphu
     * @date 2019/9/25 9:00
     * @throws
    **/
    List<WorkEvaluateIndex> selectByMonth(WorkEvaluateDto workEvaluateDto, UserInfo userInfo);

    /**
     * 根据工单编号获取评价指标列表
     * @param workId
     * @return
     */
    List<WorkEvaluateIndexDto> listByWorkId(Long workId);

}
