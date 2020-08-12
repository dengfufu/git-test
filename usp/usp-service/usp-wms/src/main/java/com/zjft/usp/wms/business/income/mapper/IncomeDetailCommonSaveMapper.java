package com.zjft.usp.wms.business.income.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.wms.business.income.model.IncomeDetailCommonSave;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 入库明细信息共用表 Mapper 接口
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface IncomeDetailCommonSaveMapper extends BaseMapper<IncomeDetailCommonSave> {

    /**
     * 根据入库单号删除入库明细信息
     *
     * @author Qiugm
     * @date 2019-11-13
     * @param incomeId
     * @return void
     */
    void deleteByIncomeId(@Param("incomeId") Long incomeId);

}
