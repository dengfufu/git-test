package com.zjft.usp.anyfix.goods.service;

import com.zjft.usp.anyfix.goods.model.GoodsPostDetail;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 物品寄送明细信息表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2020-04-16
 */
public interface GoodsPostDetailService extends IService<GoodsPostDetail> {

    /**
     * 获得物品寄送单签收状态
     *
     * @param postId
     * @return
     * @author zgpi
     * @date 2020/4/24 10:40
     */
    int findSignStatus(Long postId);
}
