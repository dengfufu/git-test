package com.zjft.usp.wms.business.outcome.strategy.other;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.outcome.dto.OutcomeWareCommonDto;

/**
 * 确认出库行为，此行为非公共部分，需要独立出来
 *
 * @Author: JFZOU
 * @Date: 2019-11-21 10:13
 */
public interface OutcomeConfirmBehavior {

    /**
     * 确认出库单
     *
     * @param outcomeWareCommonDto
     * @param userInfo
     * @param reqParam
     */
    void confirmReceive(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam);
}
