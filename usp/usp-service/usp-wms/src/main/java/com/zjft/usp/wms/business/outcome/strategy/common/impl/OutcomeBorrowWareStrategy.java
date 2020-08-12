package com.zjft.usp.wms.business.outcome.strategy.common.impl;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.outcome.dto.OutcomeWareCommonDto;
import com.zjft.usp.wms.business.outcome.model.OutcomeDetailCommonSave;
import com.zjft.usp.wms.business.outcome.model.OutcomeWareCommon;
import com.zjft.usp.wms.business.outcome.strategy.common.OutcomeStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 物料领用出库
 * @Author: JFZOU
 * @Date: 2019-11-21 9:57
 */
@Service(OutcomeStrategy.OUTCOME_BORROW_WARE)
public class OutcomeBorrowWareStrategy extends AbstractOutcomeStrategy {

    @Override
    public List<OutcomeDetailCommonSave> save(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        return super.save(outcomeWareCommonDto,userInfo,reqParam);
    }

    @Override
    public List<OutcomeWareCommon> add(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        return super.add(outcomeWareCommonDto,userInfo,reqParam);
    }

    @Override
    public void delete(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam){
        super.delete(outcomeWareCommonDto,userInfo,reqParam);
    }

    @Override
    public void update(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        super.update(outcomeWareCommonDto,userInfo,reqParam);
    }

    @Override
    public void batchUpdate(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        super.batchUpdate(outcomeWareCommonDto,userInfo,reqParam);
    }
}
