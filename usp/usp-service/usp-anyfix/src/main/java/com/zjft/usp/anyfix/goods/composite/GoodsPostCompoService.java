package com.zjft.usp.anyfix.goods.composite;

import com.zjft.usp.anyfix.goods.dto.GoodsPostDto;
import com.zjft.usp.anyfix.goods.dto.GoodsPostSignDto;
import com.zjft.usp.anyfix.goods.filter.GoodsPostFilter;
import com.zjft.usp.common.model.ListWrapper;

import java.util.List;

/**
 * <p>
 * 物品寄送聚合服务类
 * </p>
 *
 * @author zgpi
 * @since 2020-04-16
 */
public interface GoodsPostCompoService {

    /**
     * 分页查询物品寄送单
     *
     * @param goodsPostFilter
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/4/16 20:37
     */
    ListWrapper<GoodsPostDto> queryGoodsPost(GoodsPostFilter goodsPostFilter,
                                             Long curUserId,
                                             Long curCorpId);

    /**
     * 导出物品寄送单
     *
     * @param goodsPostFilter
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/4/27 20:37
     */
    List<GoodsPostDto> exportGoodsPost(GoodsPostFilter goodsPostFilter,
                                       Long curUserId,
                                       Long curCorpId);

    /**
     * 查看物品寄送单详情
     *
     * @param postId
     * @return
     * @author zgpi
     * @date 2020/4/22 13:52
     */
    GoodsPostDto viewGoodsPost(Long postId);

    /**
     * 添加物品寄送单
     *
     * @param goodsPostDto
     * @param curUserId
     * @return
     * @author zgpi
     * @date 2020/4/20 16:57
     */
    void addGoodsPost(GoodsPostDto goodsPostDto, Long curUserId);

    /**
     * 编辑物品寄送单
     *
     * @param goodsPostDto
     * @param curUserId
     * @return
     * @author zgpi
     * @date 2020/4/23 14:36
     */
    void editGoodsPost(GoodsPostDto goodsPostDto, Long curUserId);

    /**
     * 删除物品寄送单
     *
     * @param postId
     * @return
     * @author zgpi
     * @date 2020/4/22 15:07
     */
    void delGoodsPost(Long postId);

    /**
     * 签收物品寄送单
     *
     * @param goodsPostSignDto
     * @param curUserId
     * @return
     * @author zgpi
     * @date 2020/4/24 10:01
     */
    void signGoodsPost(GoodsPostSignDto goodsPostSignDto, Long curUserId);

    /**
     * 当前用户是否属于企业人员
     *
     * @param postId
     * @param curUserId
     * @return
     * @author zgpi
     * @date 2020/4/26 16:22
     **/
    GoodsPostDto findIfCorpUser(Long postId, Long curUserId);

    /**
     * 获得企业的发货或收货地址
     * 如存在历史的发货或收货寄送单，则取上一次的地址
     * 若不存在：
     *   若是服务商，有网点则取网点的地址，没有则取公司地址
     *   若是委托商，则取公司地址
     *
     * @param goodsPostFilter
     * @return
     * @author zgpi
     * @date 2020/5/13 10:42
     **/
    GoodsPostDto findAddress(GoodsPostFilter goodsPostFilter);
}
