package com.zjft.usp.wms.business.outcome.strategy.common.factory;

import com.zjft.usp.wms.business.outcome.strategy.common.OutcomeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: JFZOU
 * @Date: 2019-11-21 9:49
 */
@Service
public class OutcomeStrategyFactory {

    public static final String STRATEGY_CLASS_NAME_PREFIX = "com.zjft.usp.business.outcome_";

    /**
     * 在初始化的时候将所有的OutcomeStrategy自动加载到Map中，使用concurrentHashMap防止多线程操作的时候出现问题
     */
    @Autowired
    Map<String, OutcomeStrategy> strategies = new ConcurrentHashMap<>(3);

    public OutcomeStrategy getStrategy(String component) {
        OutcomeStrategy strategy = this.strategies.get(component);
        if (strategy == null) {
            throw new RuntimeException("没有定义相关的出库策略实现类，请检查！");
        }
        return strategy;
    }
}
