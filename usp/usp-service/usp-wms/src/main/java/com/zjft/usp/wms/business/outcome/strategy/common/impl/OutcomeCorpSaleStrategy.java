package com.zjft.usp.wms.business.outcome.strategy.common.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.outcome.dto.OutcomeWareCommonDto;
import com.zjft.usp.wms.business.outcome.model.OutcomeDetailCommonSave;
import com.zjft.usp.wms.business.outcome.model.OutcomeMainSaleSave;
import com.zjft.usp.wms.business.outcome.model.OutcomeWareCommon;
import com.zjft.usp.wms.business.outcome.model.OutcomeWareSale;
import com.zjft.usp.wms.business.outcome.service.OutcomeMainSaleSaveService;
import com.zjft.usp.wms.business.outcome.service.OutcomeWareSaleService;
import com.zjft.usp.wms.business.outcome.strategy.common.OutcomeStrategy;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 公司销售出库
 * @Author: JFZOU
 * @Date: 2019-11-21 9:53
 */
@Service(OutcomeStrategy.OUTCOME_CORP_SALE)
public class OutcomeCorpSaleStrategy extends AbstractOutcomeStrategy {

    @Autowired
    private OutcomeWareSaleService outcomeWareSaleService;
    @Autowired
    private OutcomeMainSaleSaveService outcomeMainSaleSaveService;

    @Override
    public List<OutcomeDetailCommonSave> save(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        List<OutcomeDetailCommonSave> outcomeDetailCommonSaveList = super.save(outcomeWareCommonDto,userInfo,reqParam);
        OutcomeMainSaleSave outcomeMainSaleSave = new OutcomeMainSaleSave();
        outcomeMainSaleSave.setAssistUserId(outcomeWareCommonDto.getAssistUserId());
        outcomeMainSaleSave.setId(outcomeWareCommonDto.getId());
        this.outcomeMainSaleSaveService.save(outcomeMainSaleSave);
        if(CollectionUtil.isNotEmpty(outcomeDetailCommonSaveList)){

        }
        return outcomeDetailCommonSaveList;
    }

    @Override
    public List<OutcomeWareCommon> add(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        List<OutcomeWareCommon> outcomeWareCommonList = super.add(outcomeWareCommonDto, userInfo, reqParam);
        if(CollectionUtil.isNotEmpty(outcomeWareCommonList)){
            List<OutcomeWareSale> outcomeWareSaleList = new ArrayList<>();
            for(OutcomeWareCommon outcomeWareCommon : outcomeWareCommonList){
                OutcomeWareSale outcomeWareSale = new OutcomeWareSale();
                outcomeWareSale.setAssistUserId(outcomeWareCommonDto.getAssistUserId());
                outcomeWareSale.setId(outcomeWareCommon.getId());
                outcomeWareSaleList.add(outcomeWareSale);
            }
            this.outcomeWareSaleService.saveBatch(outcomeWareSaleList);
        }
        return outcomeWareCommonList;
    }

    @Override
    public void delete(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam){
        super.delete(outcomeWareCommonDto,userInfo,reqParam);
        this.outcomeWareSaleService.removeByIds(outcomeWareCommonDto.getIdList());
    }

    @Override
    public void update(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        super.update(outcomeWareCommonDto,userInfo,reqParam);
        OutcomeWareSale outcomeWareSale = this.outcomeWareSaleService.getById(outcomeWareCommonDto.getId());
        BeanUtils.copyProperties(outcomeWareCommonDto,outcomeWareSale);
        this.outcomeWareSaleService.updateById(outcomeWareSale);
    }

    @Override
    public void batchUpdate(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        super.batchUpdate(outcomeWareCommonDto,userInfo,reqParam);
        if(CollectionUtil.isNotEmpty(outcomeWareCommonDto.getIdList())){
            Collection<OutcomeWareSale> outcomeWareSaleList =  this.outcomeWareSaleService.listByIds(outcomeWareCommonDto.getIdList());
            if(CollectionUtil.isNotEmpty(outcomeWareSaleList)){
                for(OutcomeWareSale outcomeWareSale : outcomeWareSaleList){
                    BeanUtils.copyProperties(outcomeWareCommonDto,outcomeWareSale);
                }
                this.outcomeWareSaleService.updateBatchById(outcomeWareSaleList);
            }
        }
    }
}
