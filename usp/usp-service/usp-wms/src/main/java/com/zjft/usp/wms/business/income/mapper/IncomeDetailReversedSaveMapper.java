package com.zjft.usp.wms.business.income.mapper;

import com.zjft.usp.wms.business.income.model.IncomeDetailReversedSave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 入库明细通用销账暂存表 Mapper 接口
 * </p>
 *
 * @author canlei
 * @since 2019-11-28
 */
public interface IncomeDetailReversedSaveMapper extends BaseMapper<IncomeDetailReversedSave> {

    /**
     * 根据入库单编号删除
     *
     * @author canlei
     * @param incomeId
     */
    void deleteByIncomeId(@Param("incomeId") Long incomeId);

}
