package com.zjft.usp.pay.business.service.impl;

import com.zjft.usp.pay.business.model.TradeConfirm;
import com.zjft.usp.pay.business.mapper.TradeConfirmMapper;
import com.zjft.usp.pay.business.service.TradeConfirmService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 交易状态确认表  服务实现类
 * </p>
 *
 * @author CK
 * @since 2020-06-01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TradeConfirmServiceImpl extends ServiceImpl<TradeConfirmMapper, TradeConfirm> implements TradeConfirmService {

}
