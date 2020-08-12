package com.zjft.usp.zj.work.repair.strategy.factory;

import com.zjft.usp.zj.work.repair.strategy.RepairStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 老平台报修策略管理工厂类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-23 16:51
 **/

@Service
public class RepairStrategyFactory {

    public static final String STRATEGY_CLASS_NAME_PREFIX = "com.zjft.usp.zj.repair_";

    /**
     * 在初始化的时候将所有的RepairStrategy自动加载到Map中，使用concurrentHashMap是防止多线程操作的时候出现问题
     */
    @Autowired
    Map<String, RepairStrategy> strategies = new ConcurrentHashMap<>(3);

    public RepairStrategy getStrategy(String component) {
        RepairStrategy strategy = this.strategies.get(component);
        if (strategy == null) {
            throw new RuntimeException("没有定义相关的报修策略实现类，请检查！");
        }
        return strategy;
    }
}
