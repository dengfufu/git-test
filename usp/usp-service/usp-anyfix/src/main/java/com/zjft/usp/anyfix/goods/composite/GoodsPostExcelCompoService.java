package com.zjft.usp.anyfix.goods.composite;

import com.zjft.usp.anyfix.goods.dto.GoodsPostExportDto;
import com.zjft.usp.anyfix.goods.filter.GoodsPostFilter;

import java.util.List;

/**
 * 物品寄送Excel服务类
 *
 * @author zgpi
 * @date 2020/4/27 16:33
 */
public interface GoodsPostExcelCompoService {

    /**
     * 获取物品寄送单导出数据
     *
     * @param goodsPostFilter
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/4/27 16:34
     **/
    List<GoodsPostExportDto> listGoodsPostExportDto(GoodsPostFilter goodsPostFilter,
                                                    Long curUserId,
                                                    Long curCorpId);
}
