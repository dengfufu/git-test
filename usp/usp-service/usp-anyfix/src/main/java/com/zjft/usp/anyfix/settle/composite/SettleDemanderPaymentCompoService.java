package com.zjft.usp.anyfix.settle.composite;

import com.zjft.usp.anyfix.settle.dto.SettleDemanderOnlinePaymentDto;
import com.zjft.usp.anyfix.settle.dto.SettleDemanderPaymentDto;

/**
 * 委托商结算付款聚合服务类
 *
 * @author zgpi
 * @date 2020/4/30 11:10
 */
public interface SettleDemanderPaymentCompoService {

    /**
     * 付款信息详情
     *
     * @param settleId
     * @return
     * @author zgpi
     * @date 2020/5/5 11:50
     **/
    SettleDemanderPaymentDto viewSettleDemanderPayment(Long settleId);

    /**
     * 开票
     *
     * @param settleDemanderPaymentDto
     * @param curUserId
     * @return
     * @author zgpi
     * @date 2020/4/30 10:46
     **/
    void invoice(SettleDemanderPaymentDto settleDemanderPaymentDto,
                 Long curUserId);

    /**
     * 在线付款
     *
     * @param settleDemanderOnlinePaymentDto
     * @return
     * @author zgpi
     * @date 2020/4/30 17:03
     **/
    void payOnline(SettleDemanderOnlinePaymentDto settleDemanderOnlinePaymentDto);


    /**
     * 付款
     *
     * @param settleDemanderPaymentDto
     * @param curUserId
     * @return
     * @author zgpi
     * @date 2020/4/30 17:03
     **/
    void payOffline(SettleDemanderPaymentDto settleDemanderPaymentDto,
             Long curUserId);

    /**
     * 收款
     *
     * @param settleDemanderPaymentDto
     * @param curUserId
     * @return
     * @author zgpi
     * @date 2020/5/3 15:26
     **/
    void receipt(SettleDemanderPaymentDto settleDemanderPaymentDto,
                 Long curUserId);
}
