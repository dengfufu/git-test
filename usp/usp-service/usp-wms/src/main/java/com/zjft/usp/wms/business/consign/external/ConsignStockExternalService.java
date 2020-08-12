package com.zjft.usp.wms.business.consign.external;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.wms.business.consign.dto.ConsignDetailDto;
import com.zjft.usp.wms.business.consign.dto.ConsignMainDto;
import com.zjft.usp.wms.business.consign.model.ConsignDetail;
import com.zjft.usp.wms.business.outcome.model.OutcomeWareCommon;

/**
 * external用于调用外部业务，income业务里不要直接调用外部业务接口，如果需要定义外部接口，在external包下管理
 * 调用外部类，不直接调用外部业务类，通过本类做个包装
 * @Author: JFZOU
 * @Date: 2019-11-12 16:48
 */
public interface ConsignStockExternalService {

    /***
     * 发货调整库存
     *
     * @author zphu
     * @date 2019-11-13
     * @param consignDetail
     * @return void
     */
    void adjustStockConsign(ConsignDetail consignDetail, UserInfo userInfo);

    /***
     * 收货调整库存
     *
     * @author zphu
     * @date 2019-11-13
     * @param consignDetail
     * @return void
     */
    void adjustStockReceive(ConsignDetail consignDetail, UserInfo userInfo);
}
