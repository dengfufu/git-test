package com.zjft.usp.wms.business.trans.strategy.impl;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.trans.dto.TransWareCommonDto;
import org.springframework.stereotype.Component;

/**
 * 物料快速转库策略实现类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2019-11-21 19:56
 **/
@Component(AbstractTransStrategy.TRANS_WARE_SHIFT)
public class TransWareShiftStrategy extends AbstractTransStrategy {
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
    @Override
    public void save(TransWareCommonDto transWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        super.save(transWareCommonDto, userInfo, reqParam);
    }

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
    @Override
    public void add(TransWareCommonDto transWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        super.add(transWareCommonDto, userInfo, reqParam);
    }

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
    @Override
    public void audit(TransWareCommonDto transWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        super.audit(transWareCommonDto, userInfo, reqParam);
    }

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
    @Override
    public void batchAudit(TransWareCommonDto transWareCommonDto, UserInfo userInfo, ReqParam reqParam, Integer status) {
        super.batchAudit(transWareCommonDto, userInfo, reqParam,status);
    }
}
