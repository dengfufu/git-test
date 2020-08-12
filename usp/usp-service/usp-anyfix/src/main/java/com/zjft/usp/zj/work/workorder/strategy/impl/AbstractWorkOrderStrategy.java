package com.zjft.usp.zj.work.workorder.strategy.impl;

import com.zjft.usp.zj.work.workorder.strategy.WorkOrderStrategy;
import com.zjft.usp.zj.work.workorder.strategy.factory.WorkOrderStrategyFactory;

/**
 * 老平台派工单策略抽象类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-23 16:31
 **/

public abstract class AbstractWorkOrderStrategy implements WorkOrderStrategy {
    /**
     * 工行
     */
    public static final String WORK_ORDER_ICBC = WorkOrderStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + "1";

    /**
     * 石嘴山银行
     */
    public static final String WORK_ORDER_SCS = WorkOrderStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + "2";

}
