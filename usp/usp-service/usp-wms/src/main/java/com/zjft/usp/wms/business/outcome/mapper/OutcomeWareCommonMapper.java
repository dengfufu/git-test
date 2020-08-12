package com.zjft.usp.wms.business.outcome.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.wms.business.income.dto.IncomeStatDto;
import com.zjft.usp.wms.business.income.filter.IncomeFilter;
import com.zjft.usp.wms.business.outcome.dto.OutcomeStatDto;
import com.zjft.usp.wms.business.outcome.dto.OutcomeWareCommonDto;
import com.zjft.usp.wms.business.outcome.filter.OutcomeFilter;
import com.zjft.usp.wms.business.outcome.model.OutcomeWareCommon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.wms.business.stock.dto.StockCommonResultDto;
import com.zjft.usp.wms.business.stock.filter.StockCommonFilter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 出库信息共用表 Mapper 接口
 * </p>
 *
 * @author jfzou
 * @since 2019-11-13
 */
public interface OutcomeWareCommonMapper extends BaseMapper<OutcomeWareCommon> {


    /**
     * 分页查询出库列表
     *
     * @param outcomeFilter
     * @param page
     * @return java.util.List<com.zjft.usp.wms.business.outcome.model.OutcomeWareCommonDto>
     * @author zphu
     * @date 2019/11/25 15:59
     * @throws
    **/
    List<OutcomeWareCommonDto> listByPage(@Param("outcomeFilter") OutcomeFilter outcomeFilter, Page page);

    /**
     * 根据id获取实体
     *
     * @param outcomeFilter
     * @return com.zjft.usp.wms.business.outcome.dto.OutcomeWareCommonDto
     * @author zphu
     * @date 2019/12/16 11:16
     * @throws
    **/
    OutcomeWareCommonDto getById(@Param("outcomeFilter") OutcomeFilter outcomeFilter);

    /**
     * 查询出库单个状态数量
     *
     * @author zphu
     * @param outcomeFilter
     * @return
     */
    List<OutcomeStatDto> countByStatus(@Param("outcomeFilter") OutcomeFilter outcomeFilter);
}
