package com.zjft.usp.wms.flow.strategy.endtype.factory;

import com.zjft.usp.wms.flow.strategy.endtype.INodeEndStrategy;

/**
 * 定义具体策略的工厂类接口
 *
 * @Author: JFZOU
 * @Date: 2019-11-11 15:39
 */
public interface INodeEndStrategyFactory {

    String STRATEGY_CLASS_NAME_PREFIX = "com.zjft.usp.flow.EndNodeStrategy_";

    /**
     * 定义获得具体策略的抽象接口
     *
     * @param componentKey
     * @return
     */
    INodeEndStrategy getStrategy(String componentKey);
}
