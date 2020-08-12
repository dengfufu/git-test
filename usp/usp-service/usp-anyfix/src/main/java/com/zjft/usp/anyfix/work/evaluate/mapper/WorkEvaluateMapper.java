package com.zjft.usp.anyfix.work.evaluate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.work.evaluate.dto.WorkEvaluateCountDto;
import com.zjft.usp.anyfix.work.evaluate.dto.WorkEvaluateDto;
import com.zjft.usp.anyfix.work.evaluate.dto.WorkEvaluateTotalCountDto;
import com.zjft.usp.anyfix.work.evaluate.model.WorkEvaluate;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * <p>
 * 客户评价表 Mapper 接口
 * </p>
 *
 * @author zphu
 * @since 2019-09-23
 */
public interface WorkEvaluateMapper extends BaseMapper<WorkEvaluate> {

    /**
     * 根据指标查询评价
     *
     * @param page
     * @param workEvaluateDto
     * @param userInfo
     * @param reqParam
     * @return java.util.List<com.zjft.usp.anyfix.work.evaluate.dto.WorkEvaluateDto>
     * @author zphu
     * @date 2019/10/31 14:18
     * @throws
    **/
    List<WorkEvaluateDto> queryEvaluateByIndex(Page page, @Param("workEvaluateDto") WorkEvaluateDto workEvaluateDto, @Param("userInfo") UserInfo userInfo, @Param("reqParam") ReqParam reqParam);


    /**
     * 查询工单评价统计
     * @param serviceCorp
     * @param evaluateId
     * @return
     */
    List<WorkEvaluateCountDto> queryWorkEvaluateCountDto(@Param("serviceCorp") Long serviceCorp,
                                                         @Param("evaluateId") String evaluateId,
                                                         @Param("countTime") Integer countTime);


    /**
     * 查询统计当前评价的量
     * @param serviceCorp
     * @param evaluateId
     * @return
     */
    List<WorkEvaluateTotalCountDto> queryWorkEvaluateTotalCountDto(@Param("serviceCorp") Long serviceCorp,
                                                                          @Param("evaluateId") String evaluateId,
                                                                          @Param("countTime") Integer countTime);

}
