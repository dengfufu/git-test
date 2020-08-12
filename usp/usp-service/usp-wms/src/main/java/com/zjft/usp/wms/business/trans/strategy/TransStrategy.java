package com.zjft.usp.wms.business.trans.strategy;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.trans.dto.TransWareCommonDto;

/**
 * 调拨策略接口类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2019-11-21 11:20
 **/
public interface TransStrategy {
    /**
     * 保存申请单
     *
     * @author Qiugm
     * @date 2019-11-21
     * @param transWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    void save(TransWareCommonDto transWareCommonDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 添加调拨单申请信息并自动拆单
     *
     * @author Qiugm
     * @date 2019-11-21
     * @param transWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    void add(TransWareCommonDto transWareCommonDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 审批申请单
     *
     * @author Qiugm
     * @date 2019-11-21
     * @param transWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    void audit(TransWareCommonDto transWareCommonDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 审批申请单
     *
     * @author zrlin
     * @date 2019-12-16
     * @param transWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    void batchAudit(TransWareCommonDto transWareCommonDto, UserInfo userInfo, ReqParam reqParam, Integer status);
}
