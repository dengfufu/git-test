package com.zjft.usp.anyfix.work.fee.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeVerifyDetailDto;
import com.zjft.usp.anyfix.work.fee.filter.WorkFeeVerifyDetailFilter;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeVerifyDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 委托商对账单明细表 Mapper 接口
 * </p>
 *
 * @author canlei
 * @since 2020-05-12
 */
public interface WorkFeeVerifyDetailMapper extends BaseMapper<WorkFeeVerifyDetail> {

    /**
     * 根据对账单号查询工单处理列表
     *
     * @param verifyId
     * @return
     */
    List<WorkDeal> listWorkDealByVerifyId(@Param("verifyId") Long verifyId);

    /**
     * 查询对账单明细
     *
     * @param verifyId
     * @return
     */
    List<WorkFeeVerifyDetailDto> listByVerifyId(@Param("verifyId") Long verifyId);

    /**
     * 分页查询对账单明细
     *
     * @param page
     * @param workFeeVerifyDetailFilter
     * @return
     */
    List<WorkFeeVerifyDetailDto> pageByFilter(@Param("page") Page page,
                                              @Param("workFeeVerifyDetailFilter") WorkFeeVerifyDetailFilter workFeeVerifyDetailFilter);

}
