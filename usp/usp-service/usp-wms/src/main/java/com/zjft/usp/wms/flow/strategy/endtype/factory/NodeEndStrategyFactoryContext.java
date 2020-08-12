package com.zjft.usp.wms.flow.strategy.endtype.factory;

import com.zjft.usp.wms.flow.strategy.endtype.INodeEndStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通过工厂方法类建具体策略对象上下文
 *
 * @Author: JFZOU
 * @Date: 2019-11-11 15:40
 */
@Service
public class NodeEndStrategyFactoryContext implements INodeEndStrategyFactory {

    /**
     * 自动在初始化的时候将所有的IBaseStrategy自动加载到Map中，使用concurrentHashMap是防止多线程操作的时候出现问题
     */
    @Autowired
    Map<String, INodeEndStrategy> strategyMap = new ConcurrentHashMap<>(3);

    @Override
    public INodeEndStrategy getStrategy(String componentKey) {
        INodeEndStrategy strategy = this.strategyMap.get(componentKey);
        if (strategy == null) {
            throw new RuntimeException("没有定义相关的节点跳转策略实现类，请检查！");
        }
        return strategy;
    }
}
