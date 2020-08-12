package com.zjft.usp.anyfix.settle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.settle.dto.BankAccountDto;
import com.zjft.usp.anyfix.settle.dto.SettleDemanderDto;
import com.zjft.usp.anyfix.settle.filter.SettleDemanderFilter;
import com.zjft.usp.anyfix.settle.model.SettleDemander;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 供应商结算单 Mapper 接口
 * </p>
 *
 * @author canlei
 * @since 2020-01-13
 */
public interface SettleDemanderMapper extends BaseMapper<SettleDemander> {

    /**
     * 分页条件查询
     *
     * @param page
     * @param settleDemanderFilter
     * @return
     */
    List<SettleDemanderDto> pageByFilter(Page page, @Param("settleDemanderFilter") SettleDemanderFilter settleDemanderFilter);

    /**
     * 条件查询(不分页)
     *
     * @param settleDemanderFilter
     * @return
     */
    List<SettleDemanderDto> listByFilter(@Param("settleDemanderFilter") SettleDemanderFilter settleDemanderFilter);

    /**
     * 根据结算单编号list获取结算费用，用SettleDemanderDto接收
     *
     * @param settleIdList
     * @return
     */
//    List<SettleDemanderDto> listFeeBySettleIdList(@Param("settleIdList") List<Long> settleIdList);

    /**
     * 根据结算单编号获取结算费用
     *
     * @param settleId
     * @return
     */
//    SettleDemanderDto getFeeBySettleId(@Param("settleId") Long settleId);

    /**
     * 查询收款账户信息
     *
     * @param page
     * @param settleDemanderFilter
     * @return
     */
    List<BankAccountDto> pageBankAccount(@Param("page") Page page,
                                         @Param("settleDemanderFilter") SettleDemanderFilter settleDemanderFilter);

    /**
     * 查询最大合同号序号
     *
     * @param prefix
     * @param serviceCorp
     * @param exceptId
     * @return
     */
    String findMaxSettleCode(@Param("prefix") String prefix, @Param("serviceCorp") Long serviceCorp, @Param("exceptId") Long exceptId);

    /**
     * 根据前缀列表获取最大合同号列表
     *
     * @param prefixList
     * @return
     */
    List<SettleDemanderDto> listMaxSettleCode(@Param("prefixList") List<String> prefixList);

}
