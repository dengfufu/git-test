package com.zjft.usp.wms.business.income.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.wms.business.income.dto.IncomeStatDto;
import com.zjft.usp.wms.business.income.dto.IncomeWareCommonDto;
import com.zjft.usp.wms.business.income.filter.IncomeFilter;
import com.zjft.usp.wms.business.income.model.IncomeWareCommon;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 入库明细正式表 Mapper 接口
 * </p>
 *
 * @author jfzou
 * @since 2019-11-12
 */
public interface IncomeWareCommonMapper extends BaseMapper<IncomeWareCommon> {

    /**
     * 查询入库单
     *
     * @author Qiugm
     * @date 2019-11-15
     * @param page
     * @param incomeFilter
     * @return java.util.List<com.zjft.usp.wms.business.income.model.IncomeWareCommon>
     */
    List<IncomeWareCommonDto> listByPage(Page page, @Param("incomeFilter") IncomeFilter incomeFilter);

    /**
     * 查询未入库和已入库的工单数量
     *
     * @author canlei
     * @param incomeFilter
     * @return
     */
    List<IncomeStatDto> countByStatus(@Param("incomeFilter") IncomeFilter incomeFilter);

}
