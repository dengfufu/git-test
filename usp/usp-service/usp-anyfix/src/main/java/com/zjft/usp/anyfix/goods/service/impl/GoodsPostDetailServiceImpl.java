package com.zjft.usp.anyfix.goods.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.anyfix.goods.enums.SignStatusEnum;
import com.zjft.usp.anyfix.goods.model.GoodsPostDetail;
import com.zjft.usp.anyfix.goods.mapper.GoodsPostDetailMapper;
import com.zjft.usp.anyfix.goods.service.GoodsPostDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.utils.IntUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 物品寄送明细信息表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2020-04-16
 */
@Service
public class GoodsPostDetailServiceImpl extends ServiceImpl<GoodsPostDetailMapper, GoodsPostDetail> implements GoodsPostDetailService {

    /**
     * 获得物品寄送单签收状态
     *
     * @param postId
     * @return
     * @author zgpi
     * @date 2020/4/24 10:40
     */
    @Override
    public int findSignStatus(Long postId) {
        List<GoodsPostDetail> goodsPostDetailList = this.list(new QueryWrapper<GoodsPostDetail>()
                .eq("post_id", postId));
        Integer notSignedCount = this.baseMapper.findNotSignedCount(postId);
        // 没有明细，未签收
        if (CollectionUtil.isEmpty(goodsPostDetailList)) {
            return SignStatusEnum.NOT_SIGN.getCode();
        }
        if (IntUtil.isZero(notSignedCount)) {
            // 不存在未签收明细，表示全部签收
            return SignStatusEnum.ALL_SIGN.getCode();
        } else if (notSignedCount > 0 && goodsPostDetailList.size() == notSignedCount) {
            // 存在未签收明细，且总数和未签收数相等，表示未签收
            return SignStatusEnum.NOT_SIGN.getCode();
        } else if (notSignedCount > 0 && goodsPostDetailList.size() > notSignedCount) {
            // 存在未签收明细，且总数大于未签收数，表示部分签收
            return SignStatusEnum.PART_SIGN.getCode();
        }
        return SignStatusEnum.NOT_SIGN.getCode();
    }
}
