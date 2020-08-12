package com.zjft.usp.wms.business.income.strategy;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.income.dto.IncomeWareCommonDto;

/**
 * 物料入库策略接口
 *
 * @author Qiugm
 * @version 1.0
 * @date 2019-11-13 20:33
 **/
public interface IncomeStrategy {

    /***
     * 保存入库申请单
     *
     * @author Qiugm
     * @date 2019-11-13
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    void save(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam);

    /***
     * 入库申请提交
     *
     * @author Qiugm
     * @date 2019-11-13
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    void add(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 审批入库申请
     *
     * @author Qiugm
     * @date 2019-11-14
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    void audit(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 修改入库申请
     *
     * @author Qiugm
     * @date 2019-11-14
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    void update(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 结束当前流程结点
     *
     * @author canlei
     * @param incomeWareCommonDto
     * @param userInfo
     */
    void endCurrentNode(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo);
}

