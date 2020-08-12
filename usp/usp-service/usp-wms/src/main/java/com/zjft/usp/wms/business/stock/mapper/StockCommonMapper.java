package com.zjft.usp.wms.business.stock.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.wms.business.stock.dto.StockCommonDto;
import com.zjft.usp.wms.business.stock.dto.StockCommonResultDto;
import com.zjft.usp.wms.business.stock.filter.StockCommonFilter;
import com.zjft.usp.wms.business.stock.model.StockCommon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 库存实时总账共用表 Mapper 接口
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface StockCommonMapper extends BaseMapper<StockCommon> {

    /**
     * 根据条件查询库房
     *
     * @param stockCommonFilter
     * @param page
     * @return java.util.List<com.zjft.usp.wms.business.stock.model.StockCommonResultDto>
     * @author zphu
     * @date 2019/11/26 16:07
     * @throws
    **/
    List<StockCommonResultDto> listByPage(@Param("stockCommonFilter") StockCommonFilter stockCommonFilter, Page page);

    /**
     * 根据明细申请条件查询对应的库存
     * @param map
     * @return
     */
    List<StockCommon> selectByTrans(Map<String,Object> map);

}
