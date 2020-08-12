package com.zjft.usp.anyfix.settle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.settle.dto.SettleStaffDetailDto;
import com.zjft.usp.anyfix.settle.filter.SettleStaffDetailFilter;
import com.zjft.usp.anyfix.settle.model.SettleStaffDetail;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.vo.ReqParam;

/**
 * <p>
 * 员工结算单明细 服务类
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
public interface SettleStaffDetailService extends IService<SettleStaffDetail> {

    /**
     * 查询员工结算单明细
     * @param settleStaffDetailFilter
     * @param reqParam
     * @return
     */
    ListWrapper<SettleStaffDetailDto> query(SettleStaffDetailFilter settleStaffDetailFilter, ReqParam reqParam);

}
