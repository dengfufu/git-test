package com.zjft.usp.wms.business.outcome.external.impl;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.wms.baseinfo.enums.SituationEnum;
import com.zjft.usp.wms.business.outcome.external.OutcomeStockExternalService;
import com.zjft.usp.wms.business.outcome.model.OutcomeWareCommon;
import com.zjft.usp.wms.business.stock.composite.StockCompositeService;
import com.zjft.usp.wms.business.stock.composite.dto.StockNormalAdjustDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @Author: JFZOU
 * @Date: 2019-11-12 16:50
 */
@Transactional(rollbackFor = Exception.class)
@Component
public class OutcomeStockExternalServiceImpl implements OutcomeStockExternalService {
    @Autowired
    private StockCompositeService stockCompositeService;

    @Override
    public void adjustStock(OutcomeWareCommon outcomeWareCommon, UserInfo userInfo) {
        StockNormalAdjustDto stockCommonUpdateDto = new StockNormalAdjustDto();
        stockCommonUpdateDto.setFormDetailId(outcomeWareCommon.getId());
        stockCommonUpdateDto.setLargeClassId(outcomeWareCommon.getLargeClassId());
        stockCommonUpdateDto.setSmallClassId(outcomeWareCommon.getSmallClassId());
        stockCommonUpdateDto.setAdjustQty(outcomeWareCommon.getQuantity());
        stockCommonUpdateDto.setSourceStockId(outcomeWareCommon.getStockId());
        stockCommonUpdateDto.setDoBy(userInfo.getUserId());
        stockCommonUpdateDto.setOldDepotId(outcomeWareCommon.getDepotId());
        stockCommonUpdateDto.setNewDepotId(outcomeWareCommon.getDepotId());
        stockCommonUpdateDto.setNewSituation(SituationEnum.OUT.getCode());
        this.stockCompositeService.adjustSituationQty(stockCommonUpdateDto);
    }

}
