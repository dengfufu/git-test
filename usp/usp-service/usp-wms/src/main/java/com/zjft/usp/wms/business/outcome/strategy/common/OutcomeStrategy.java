package com.zjft.usp.wms.business.outcome.strategy.common;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.outcome.dto.OutcomeWareCommonDto;
import com.zjft.usp.wms.business.outcome.model.OutcomeDetailCommonSave;
import com.zjft.usp.wms.business.outcome.model.OutcomeWareCommon;
import com.zjft.usp.wms.business.outcome.strategy.common.factory.OutcomeStrategyFactory;

import java.util.List;

/**
 * 出库策略接口
 *
 * @author jfzou
 * @version 1.0
 * @date 2019-11-21 09:15
 **/
public interface OutcomeStrategy {

    /**
     * 销售借用出库
     */
    public static final String OUTCOME_SALE_BORROW = OutcomeStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + "70";
    /**
     * 公司销售出库
     */
    public static final String OUTCOME_CORP_SALE = OutcomeStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + "80";
    /**
     * 归还厂商出库
     */
    public static final String OUTCOME_RETURN_VENDOR = OutcomeStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + "90";
    /**
     * 物料领用出库
     */
    public static final String OUTCOME_BORROW_WARE = OutcomeStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + "100";

    /**
     * 保存出库单
     *
     * @param outcomeWareCommonDto
     * @param userInfo
     * @param reqParam
     */
    List<OutcomeDetailCommonSave> save(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam);


    /**
     * 提交出库单
     *
     * @param outcomeWareCommonDto
     * @param userInfo
     * @param reqParam
     */
    List<OutcomeWareCommon> add(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam);


    /**
     * 删除出库单
     *
     * @param outcomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @author zphu
     * @date 2019/11/26 10:07
     * @throws
    **/
    void delete(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 审批出库单
     *
     * @param outcomeWareCommonDto
     * @param userInfo
     * @param reqParam
     */
    void audit(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 出库
     *
     * @param outcomeWareCommonDto
     * @param userInfo
     * @param reqParam
     */
    void send(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam);


    /**
     * 修改出库单
     *
     * @param outcomeWareCommonDto
     * @param userInfo
     * @param reqParam
     */
    void update(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 出库申请批量修改（申请关联的所有记录即申请基本信息修改）
     *
     * @param outcomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author zphu
     * @date 2019/11/26 10:35
     * @throws
    **/
    void batchUpdate(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 结束当前流程结点
     *
     * @param outcomeWareCommonDto
     * @param userInfo
     * @return void
     * @author zphu
     * @date 2019/11/26 11:15
     * @throws
    **/
    void endCurrentNode(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo);
}

