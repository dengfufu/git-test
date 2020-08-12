package com.zjft.usp.anyfix.settle.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.settle.dto.SettleBranchDetailDto;
import com.zjft.usp.anyfix.settle.filter.SettleBranchDetailFilter;
import com.zjft.usp.anyfix.settle.model.SettleBranchDetail;
import com.zjft.usp.common.model.ListWrapper;

/**
 * <p>
 * 网点结算单明细 服务类
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
public interface SettleBranchDetailService extends IService<SettleBranchDetail> {

    /**
     * 根据结算单号分页查询明细
     * @param settleBranchDetailFilter
     * @return
     */
    ListWrapper<SettleBranchDetailDto> pageByFilter(SettleBranchDetailFilter settleBranchDetailFilter);

}
