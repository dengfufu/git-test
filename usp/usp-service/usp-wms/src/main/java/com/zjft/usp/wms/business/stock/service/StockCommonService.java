package com.zjft.usp.wms.business.stock.service;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.wms.business.stock.dto.StockCommonResultDto;
import com.zjft.usp.wms.business.stock.filter.StockCommonFilter;
import com.zjft.usp.wms.business.stock.model.StockCommon;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 库存实时总账共用表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface StockCommonService extends IService<StockCommon> {

    /**
     * 持久化库存共用数据
     *
     * @param stockCommon
     */
    void addStockCommon(StockCommon stockCommon);

    /**
     * 分页查询库存实时数据
     *
     * @param stockCommonFilter
     * @param userInfo
     * @return
     */
    ListWrapper<StockCommonResultDto> pageBy(StockCommonFilter stockCommonFilter, UserInfo userInfo);

    /**
     * 根据ids获取对应的dto
     *
     * @param stockIdList
     * @param cropId
     * @return java.util.List<com.zjft.usp.wms.business.stock.dto.StockCommonResultDto>
     * @author zphu
     * @date 2019/12/11 15:10
     * @throws
    **/
    List<StockCommonResultDto> listByStockIds(Collection<Long> stockIdList,Long cropId);
    /**
     * 根据idlist获取id和类的map
     *
     * @param idList
     * @return java.util.Map<java.lang.Long,com.zjft.usp.wms.business.stock.model.StockCommon>
     * @author zphu
     * @date 2019/11/22 16:48
     * @throws
    **/
    Map<Long, StockCommon> mapIdAndObject(Collection<Long> idList);


    /**
     * 查询明细对应的现有库存
     * @param map
     * @return
     */
    List<StockCommon> selectQtyByTrans(Map<String,Object> map);
}
