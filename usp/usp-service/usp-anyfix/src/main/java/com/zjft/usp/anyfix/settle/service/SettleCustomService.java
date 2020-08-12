package com.zjft.usp.anyfix.settle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.settle.dto.SettleCustomDto;
import com.zjft.usp.anyfix.settle.filter.SettleCustomFilter;
import com.zjft.usp.anyfix.settle.model.SettleCustom;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;

/**
 * <p>
 * 客户结算单 服务类
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
public interface SettleCustomService extends IService<SettleCustom> {

    /**
     * 增加客户结算记录
     * @param customSettleDto
     * @param curUserId
     * @return
     */
    Result addRecord(SettleCustomDto customSettleDto, Long curUserId);

    /**
     * 分页查询客户结算单
     * @param filter
     * @return
     */
    ListWrapper<SettleCustomDto> pageByFilter(SettleCustomFilter filter);

    /**
     * 根据结算单编号删除
     * @param settleId
     */
    void deleteById(Long settleId);

}
