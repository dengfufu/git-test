package com.zjft.usp.anyfix.work.evaluate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.anyfix.work.evaluate.model.WorkEvaluateIndex;

import java.util.List;

/**
 * <p>
 * 客户评价指标表 Mapper 接口
 * </p>
 *
 * @author zphu
 * @since 2019-09-23
 */
public interface WorkEvaluateIndexMapper extends BaseMapper<WorkEvaluateIndex> {

    /**
     * 查询工程师某月的评价
     *
     * @param month
     * @param userId
     * @param evaluateId
     * @return
     * @author zphu
     * @date 2019/9/25 15:09
     * @throws
    **/
    List<WorkEvaluateIndex> selectByMonth(String month, Long userId,Integer evaluateId);
}
