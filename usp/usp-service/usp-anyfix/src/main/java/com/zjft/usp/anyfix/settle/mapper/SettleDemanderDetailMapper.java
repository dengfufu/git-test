package com.zjft.usp.anyfix.settle.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.settle.dto.SettleDemanderDetailDto;
import com.zjft.usp.anyfix.settle.filter.SettleDemanderDetailFilter;
import com.zjft.usp.anyfix.settle.model.SettleDemanderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeVerifyDto;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeVerifyDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 供应商结算单明细 Mapper 接口
 * </p>
 *
 * @author canlei
 * @since 2020-01-13
 */
public interface SettleDemanderDetailMapper extends BaseMapper<SettleDemanderDetail> {

    /**
     * 查询供应商结算单明细
     *
     * settleDemanderDetailFilter
     * @return
     */
    List<WorkFeeVerifyDto> listWorkFeeVerify(@Param("settleDemanderDetailFilter") SettleDemanderDetailFilter settleDemanderDetailFilter);

    /**
     * 查询供应商结算单明细
     *
     * settleDemanderDetailFilter
     * @return
     */
    List<WorkFeeVerifyDto> queryWorkFeeVerify(Page page, @Param("settleDemanderDetailFilter") SettleDemanderDetailFilter settleDemanderDetailFilter);

    /**
     * 查询供应商结算单明细
     *
     * settleDemanderDetailFilter
     * @return
     */
    List<WorkFeeDto> listWork(@Param("settleDemanderDetailFilter") SettleDemanderDetailFilter settleDemanderDetailFilter);

    /**
     * 查询供应商结算单明细
     *
     * settleDemanderDetailFilter
     * @return
     */
    List<WorkFeeDto> queryWork(Page page, @Param("settleDemanderDetailFilter") SettleDemanderDetailFilter settleDemanderDetailFilter);

    /**
     * 根据结算单编号查询工单处理信息
     *
     * @param settleId
     * @return
     */
//    List<WorkDeal> listWorkDealBySettleId(@Param("settleId") Long settleId);

}
