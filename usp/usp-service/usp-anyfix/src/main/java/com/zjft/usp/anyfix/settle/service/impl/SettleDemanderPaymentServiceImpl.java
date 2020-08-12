package com.zjft.usp.anyfix.settle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.settle.mapper.SettleDemanderPaymentMapper;
import com.zjft.usp.anyfix.settle.model.SettleDemanderPayment;
import com.zjft.usp.anyfix.settle.service.SettleDemanderPaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 委托商结算单付款表 服务实现类
 * </p>
 *
 * @author canlei
 * @since 2020-04-28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SettleDemanderPaymentServiceImpl extends ServiceImpl<SettleDemanderPaymentMapper, SettleDemanderPayment> implements SettleDemanderPaymentService {

}
