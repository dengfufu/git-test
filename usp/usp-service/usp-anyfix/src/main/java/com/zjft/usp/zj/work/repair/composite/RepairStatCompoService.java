package com.zjft.usp.zj.work.repair.composite;

import com.zjft.usp.anyfix.work.request.dto.WorkStatDto;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.work.repair.filter.RepairFilter;

import java.util.List;

/**
 * 老平台报修统计聚合接口
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-04-01 16:12
 **/
public interface RepairStatCompoService {

    /**
     * 统计各个状态的工单数量
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-04-01
     */
    List<WorkStatDto> countRepairStatus(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam);
}
