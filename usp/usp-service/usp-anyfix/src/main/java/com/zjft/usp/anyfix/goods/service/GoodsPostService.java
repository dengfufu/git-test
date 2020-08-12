package com.zjft.usp.anyfix.goods.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.goods.dto.GoodsPostDto;
import com.zjft.usp.anyfix.goods.filter.GoodsPostFilter;
import com.zjft.usp.anyfix.goods.model.GoodsPost;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;

import java.util.List;

/**
 * <p>
 * 物品寄送基本信息表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2020-04-16
 */
public interface GoodsPostService extends IService<GoodsPost> {

    /**
     * 分页查询物品寄送
     *
     * @param page
     * @param goodsPostFilter
     * @return
     */
    List<GoodsPostDto> queryGoodsPost(Page page, GoodsPostFilter goodsPostFilter);
}
