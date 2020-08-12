package com.zjft.usp.zj.work.repair.strategy.impl;

import com.zjft.usp.zj.work.repair.strategy.RepairStrategy;
import com.zjft.usp.zj.work.repair.strategy.factory.RepairStrategyFactory;

/**
 * 老平台报修策略抽象类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-23 16:31
 **/

public abstract class AbstractRepairStrategy implements RepairStrategy {

    /**
     * 工行报修
     */
    public static final String REPAIR_ICBC = RepairStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + "1";

    /**
     * 石嘴山银行报修
     */
    public static final String REPAIR_SCS = RepairStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + "2";

}
