package com.zjft.usp.wms.business.trans.strategy.factory;

import com.zjft.usp.wms.business.trans.strategy.TransStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 策略管理工厂类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2019-11-21 11:20
 **/
@Service
public class TransStrategyFactory {
    /**
     * 在初始化的时候将所有的IncomeStrategy自动加载到Map中，使用concurrentHashMap是防止多线程操作的时候出现问题
     */
    @Autowired
    private Map<String, TransStrategy> strategies = new ConcurrentHashMap<>(3);

    public TransStrategy getStrategy(String component) {
        TransStrategy strategy = this.strategies.get(component);
        if (strategy == null) {
            throw new RuntimeException("没有定义相关的入库策略实现类，请检查！");
        }
        return strategy;
    }
}
