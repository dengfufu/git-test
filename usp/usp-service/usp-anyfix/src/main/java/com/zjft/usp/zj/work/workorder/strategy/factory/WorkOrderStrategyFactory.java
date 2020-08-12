package com.zjft.usp.zj.work.workorder.strategy.factory;

import com.zjft.usp.zj.work.workorder.strategy.WorkOrderStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 老平台派工单策略管理工厂类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-23 16:51
 **/

@Service
public class WorkOrderStrategyFactory {

    public static final String STRATEGY_CLASS_NAME_PREFIX = "com.zjft.usp.zj.workorder_";

    /**
     * 在初始化的时候将所有的WorkOrderStrategy自动加载到Map中，使用concurrentHashMap是防止多线程操作的时候出现问题
     */
    @Autowired
    Map<String, WorkOrderStrategy> strategies = new ConcurrentHashMap<>(3);

    public WorkOrderStrategy getStrategy(String component) {
        WorkOrderStrategy strategy = this.strategies.get(component);
        if (strategy == null) {
            throw new RuntimeException("没有定义相关的派工策略实现类，请检查！");
        }
        return strategy;
    }
}
