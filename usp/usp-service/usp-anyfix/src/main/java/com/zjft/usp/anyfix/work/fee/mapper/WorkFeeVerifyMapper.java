package com.zjft.usp.anyfix.work.fee.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeVerifyDto;
import com.zjft.usp.anyfix.work.fee.filter.WorkFeeVerifyFilter;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeVerify;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeVerifyDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 委托商对账单表 Mapper 接口
 * </p>
 *
 * @author canlei
 * @since 2020-05-12
 */
public interface WorkFeeVerifyMapper extends BaseMapper<WorkFeeVerify> {

    /**
     * 分页查询
     *
     * @param page
     * @param workFeeVerifyFilter
     * @return
     */
    List<WorkFeeVerifyDto> pageByFilter(@Param("page") Page page, @Param("workFeeVerifyFilter") WorkFeeVerifyFilter workFeeVerifyFilter);

    /**
     * 分页查询可结算对账单
     *
     * @param page
     * @param workFeeVerifyFilter
     * @return
     */
    List<WorkFeeVerifyDto> queryCanSettleVerify(@Param("page") Page page, @Param("workFeeVerifyFilter") WorkFeeVerifyFilter workFeeVerifyFilter);

    /**
     * 根据前缀查询最大对账单号
     *
     * @param prefix
     * @return
     */
    String findMaxVerifyName(@Param("prefix") String prefix);

    /**
     * 分组获取最大对账单号
     *
     * @param prefixList
     * @return
     */
    List<WorkFeeVerifyDto> listMaxVerifyName(@Param("prefixList") List<String> prefixList, @Param("prefixLength") Integer prefixLength);

}
