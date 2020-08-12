package com.zjft.usp.wms.business.income.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.wms.business.income.dto.IncomeStatDto;
import com.zjft.usp.wms.business.income.model.IncomeMainCommonSave;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 入库基本信息共用表 Mapper 接口
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface IncomeMainCommonSaveMapper extends BaseMapper<IncomeMainCommonSave> {

    /**
     * 查询保存的入库单数量
     *
     * @author canlei
     * @param curUserId
     * @param corpId
     * @return
     */
    IncomeStatDto countSave(@Param("curUserId") Long curUserId, @Param("corpId") Long corpId);

}
