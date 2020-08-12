package com.zjft.usp.wms.business.income.strategy.impl;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.income.dto.IncomeWareCommonDto;
import org.springframework.stereotype.Component;

/**
 * 公司采购入库策略实现类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2019-11-14 10:09
 **/
@Component(AbstractIncomeStrategy.INCOME_CORP_PURCHASE)
public class IncomeCorpPurchaseStrategy extends AbstractIncomeStrategy {
    /***
     * 保存入库申请单
     *
     * @author Qiugm
     * @date 2019-11-13
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    @Override
    public void save(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        super.save(incomeWareCommonDto, userInfo, reqParam);
    }

    /***
     * 入库申请提交
     *
     * @author Qiugm
     * @date 2019-11-13
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    @Override
    public void add(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        super.add(incomeWareCommonDto, userInfo, reqParam);
    }

    /**
     * 审核入库申请
     *
     * @author Qiugm
     * @date 2019-11-14
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    @Override
    public void audit(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        super.audit(incomeWareCommonDto, userInfo, reqParam);
    }

    /**
     * 修改入库申请
     *
     * @author Qiugm
     * @date 2019-11-14
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    @Override
    public void update(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        super.update(incomeWareCommonDto, userInfo, reqParam);
    }

}
