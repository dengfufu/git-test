package com.zjft.usp.anyfix.settle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.settle.dto.SettleCustomDetailDto;
import com.zjft.usp.anyfix.settle.filter.SettleCustomDetailFilter;
import com.zjft.usp.anyfix.settle.model.SettleCustomDetail;
import com.zjft.usp.common.model.ListWrapper;

import java.util.List;

/**
 * <p>
 * 客户结算单明细 服务类
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
public interface SettleCustomDetailService extends IService<SettleCustomDetail> {

    /**
     * 根据结算单号查询明细
     * @param settleCustomDetailFilter
     * @return
     */
    ListWrapper<SettleCustomDetailDto> pageByFilter(SettleCustomDetailFilter settleCustomDetailFilter);

}
