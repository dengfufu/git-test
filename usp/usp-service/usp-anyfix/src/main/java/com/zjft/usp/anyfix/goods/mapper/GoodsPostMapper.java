package com.zjft.usp.anyfix.goods.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.goods.dto.GoodsPostDto;
import com.zjft.usp.anyfix.goods.filter.GoodsPostFilter;
import com.zjft.usp.anyfix.goods.model.GoodsPost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 物品寄送基本信息表 Mapper 接口
 * </p>
 *
 * @author zgpi
 * @since 2020-04-16
 */
public interface GoodsPostMapper extends BaseMapper<GoodsPost> {

    /**
     * 查询物品寄送单
     *
     * @param page
     * @param goodsPostFilter
     * @return
     * @author zgpi
     * @date 2020/4/21 9:36
     **/
    List<GoodsPostDto> queryGoodsPost(Page page, @Param("goodsPostFilter") GoodsPostFilter goodsPostFilter);
}
