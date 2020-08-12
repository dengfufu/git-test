package com.zjft.usp.wms.business.consign.strategy;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.consign.dto.ConsignMainDto;
import com.zjft.usp.wms.business.consign.strategy.factory.ConsignStrategyFactory;


/**
 * 发货接口
 *
 * @author zphu
 * @version 1.0
 * @date 2019-12-03 20:33
 **/
public interface ConsignStrategy {

    /**
     * 物料出库发货
     */
    String OUTCOME = ConsignStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + "20";

    /**
     * 调拨发货
     */
    String TRANS = ConsignStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + "30";

    /**
     * 提交发货申请
     *
     * @param consignMainDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author zphu
     * @date 2019/12/4 9:44
     * @throws
    **/
    void add(ConsignMainDto consignMainDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 收货
     *
     * @param consignMainDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author zphu
     * @date 2019/12/6 10:21
     * @throws
    **/
    void receive(ConsignMainDto consignMainDto, UserInfo userInfo, ReqParam reqParam);

}

