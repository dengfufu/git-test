package com.zjft.usp.wms.business.consign.external.impl;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.wms.baseinfo.enums.SituationEnum;
import com.zjft.usp.wms.business.consign.external.ConsignStockExternalService;
import com.zjft.usp.wms.business.consign.model.ConsignDetail;
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
public class ConsignStockExternalServiceImpl implements ConsignStockExternalService {
    @Autowired
    private StockCompositeService stockCompositeService;

    @Override
    public void adjustStockConsign(ConsignDetail consignDetail, UserInfo userInfo) {
        StockNormalAdjustDto stockCommonUpdateDto = new StockNormalAdjustDto();
        stockCommonUpdateDto.setLargeClassId(consignDetail.getLargeClassId());
        stockCommonUpdateDto.setSmallClassId(consignDetail.getSmallClassId());
        stockCommonUpdateDto.setAdjustQty(consignDetail.getQuantity());
        stockCommonUpdateDto.setSourceStockId(consignDetail.getStockId());
        stockCommonUpdateDto.setDoBy(userInfo.getUserId());
        stockCommonUpdateDto.setFormDetailId(consignDetail.getFormDetailId());
        stockCommonUpdateDto.setNewSituation(SituationEnum.WAY.getCode());
        stockCommonUpdateDto.setNewDepotId(consignDetail.getFromDepotId());
        stockCommonUpdateDto.setOldDepotId(consignDetail.getFromDepotId());
        stockCommonUpdateDto.setSourceStockId(consignDetail.getStockId());
        this.stockCompositeService.adjustSituationQty(stockCommonUpdateDto);
    }

    @Override
    public void adjustStockReceive(ConsignDetail consignDetail, UserInfo userInfo) {
        StockNormalAdjustDto stockCommonUpdateDto = new StockNormalAdjustDto();
        stockCommonUpdateDto.setLargeClassId(consignDetail.getLargeClassId());
        stockCommonUpdateDto.setSmallClassId(consignDetail.getSmallClassId());
        stockCommonUpdateDto.setAdjustQty(consignDetail.getQuantity());
        stockCommonUpdateDto.setSourceStockId(consignDetail.getStockId());
        stockCommonUpdateDto.setDoBy(userInfo.getUserId());
        stockCommonUpdateDto.setFormDetailId(consignDetail.getId());
        stockCommonUpdateDto.setOldSituation(SituationEnum.WAY.getCode());
        stockCommonUpdateDto.setNewSituation(SituationEnum.STOCK.getCode());
        stockCommonUpdateDto.setNewDepotId(consignDetail.getToDepotId());
        stockCommonUpdateDto.setOldDepotId(consignDetail.getFromDepotId());
        stockCommonUpdateDto.setSourceStockId(consignDetail.getStockId());
        this.stockCompositeService.adjustSituationQty(stockCommonUpdateDto);
    }

}
