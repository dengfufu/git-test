package com.zjft.usp.anyfix.goods.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.goods.dto.GoodsPostDto;
import com.zjft.usp.anyfix.goods.filter.GoodsPostFilter;
import com.zjft.usp.anyfix.goods.model.GoodsPost;
import com.zjft.usp.anyfix.goods.mapper.GoodsPostMapper;
import com.zjft.usp.anyfix.goods.service.GoodsPostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 物品寄送基本信息表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2020-04-16
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GoodsPostServiceImpl extends ServiceImpl<GoodsPostMapper, GoodsPost> implements GoodsPostService {

    /**
     * 分页查询物品寄送
     *
     * @param page
     * @param goodsPostFilter
     * @return
     */
    @Override
    public List<GoodsPostDto> queryGoodsPost(Page page, GoodsPostFilter goodsPostFilter) {
        return this.baseMapper.queryGoodsPost(page, goodsPostFilter);
    }
}
