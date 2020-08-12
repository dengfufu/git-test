package com.zjft.usp.anyfix.settle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.settle.dto.SettleStaffDto;
import com.zjft.usp.anyfix.settle.filter.SettleStaffFilter;
import com.zjft.usp.anyfix.settle.model.SettleStaff;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.vo.ReqParam;

/**
 * <p>
 * 员工结算单 服务类
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
public interface SettleStaffService extends IService<SettleStaff> {

    /**
     * 分页查询员工结算单
     * @param settleStaffFilter
     * @return
     */
    ListWrapper<SettleStaffDto> pageByFilter(SettleStaffFilter settleStaffFilter, ReqParam reqParam);

    /**
     * 根据结算单号删除
     * @param settleId
     */
    void deleteById(Long settleId);

}
