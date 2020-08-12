package com.zjft.usp.wms.business.income.strategy.factory;

import com.zjft.usp.wms.business.income.strategy.IncomeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 策略管理工厂类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2019-11-14 10:04
 **/
@Service
public class IncomeStrategyFactory {

    public static final String STRATEGY_CLASS_NAME_PREFIX = "com.zjft.usp.business.income_";

    /**
     * 在初始化的时候将所有的IncomeStrategy自动加载到Map中，使用concurrentHashMap是防止多线程操作的时候出现问题
     */
    @Autowired
    Map<String, IncomeStrategy> strategies = new ConcurrentHashMap<>(3);

    public IncomeStrategy getStrategy(String component) {
        IncomeStrategy strategy = this.strategies.get(component);
        if (strategy == null) {
            throw new RuntimeException("没有定义相关的入库策略实现类，请检查！");
        }
        return strategy;
    }
}
