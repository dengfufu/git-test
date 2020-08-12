package com.zjft.usp.pay.business.service;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.pay.business.dto.AccountTraceDto;
import com.zjft.usp.pay.business.filter.AccountTraceFilter;
import com.zjft.usp.pay.business.model.AccountTrace;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 账户流水表  服务类
 * </p>
 *
 * @author CK
 * @since 2020-05-26
 */
public interface AccountTraceService extends IService<AccountTrace> {

    /**
     * 添加交易记录
     *
     * @param accountTraceDTO
     */
    void addAccountTrace(AccountTraceDto accountTraceDTO);

    /**
     * 分页查询记录
     *
     * @param accountTraceFilter
     * @return
     */
    ListWrapper<AccountTrace> query(AccountTraceFilter accountTraceFilter);

}
