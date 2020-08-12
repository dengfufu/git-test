package com.zjft.usp.anyfix.goods.mapper;

import com.zjft.usp.anyfix.goods.model.GoodsPostDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 物品寄送明细信息表 Mapper 接口
 * </p>
 *
 * @author zgpi
 * @since 2020-04-16
 */
public interface GoodsPostDetailMapper extends BaseMapper<GoodsPostDetail> {

    /**
     * 物品寄送单未签收明细数量
     *
     * @param postId
     * @return
     * @author zgpi
     * @date 2020/4/24 10:40
     */
    Integer findNotSignedCount(Long postId);

    /**
     * 物品寄送单签收明细数量
     *
     * @param postId
     * @return
     * @author zgpi
     * @date 2020/4/24 10:40
     */
    Integer findSignedCount(Long postId);
}
