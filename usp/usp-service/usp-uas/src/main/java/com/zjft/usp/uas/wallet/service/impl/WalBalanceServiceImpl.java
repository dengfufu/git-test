package com.zjft.usp.uas.wallet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.uas.wallet.mapper.WalBalanceMapper;
import com.zjft.usp.uas.wallet.model.WalBalance;
import com.zjft.usp.uas.wallet.service.WalBalanceService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @description: 服务实现类
 * @author chenxiaod
 * @date 2019/8/6 16:39
 */
@Service
public class WalBalanceServiceImpl extends ServiceImpl<WalBalanceMapper, WalBalance> implements WalBalanceService {

    @Resource
    private WalBalanceMapper walBalanceMapper;

    @Override
    public BigDecimal walBalanceQuery(long userId){
        return walBalanceMapper.walBalanceQuery(userId);
    }
}
