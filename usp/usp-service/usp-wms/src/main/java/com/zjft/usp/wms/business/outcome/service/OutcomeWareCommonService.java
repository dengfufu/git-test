package com.zjft.usp.wms.business.outcome.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.outcome.dto.OutcomeStatDto;
import com.zjft.usp.wms.business.outcome.dto.OutcomeWareCommonDto;
import com.zjft.usp.wms.business.outcome.filter.OutcomeFilter;
import com.zjft.usp.wms.business.outcome.model.OutcomeWareCommon;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 出库信息共用表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-13
 */
public interface OutcomeWareCommonService extends IService<OutcomeWareCommon> {


    /**
     * 更新出库状态
     *
     * @param outcomeWareCommon
     * @return void
     * @author zphu
     * @date 2019/11/22 14:33
     * @throws
    **/
    void updateOutcomeStatus(OutcomeWareCommon outcomeWareCommon);

    /**
     * 分页查询出库记录
     *
     * @param outcomeFilter
     * @param reqParam
     * @param page
     * @return java.util.List<com.zjft.usp.wms.business.outcome.model.OutcomeWareCommon>
     * @author zphu
     * @date 2019/11/25 14:32
     * @throws
    **/
    List<OutcomeWareCommonDto> listByPage(OutcomeFilter outcomeFilter, ReqParam reqParam, Page page);

    /**
     * 根据id获取实体
     *
     * @param outcomeFilter
     * @return com.zjft.usp.wms.business.outcome.dto.OutcomeWareCommonDto
     * @author zphu
     * @date 2019/12/16 11:16
     * @throws
     **/
    OutcomeWareCommonDto getById(OutcomeFilter outcomeFilter);

    /**
     * 查询出库单各个状态数量
     *
     * @param outcomeFilter
     * @return java.util.List<com.zjft.usp.wms.business.outcome.dto.OutcomeStatDto>
     * @author zphu
     * @date 2019/12/9 16:59
     * @throws
    **/
    List<OutcomeStatDto> countByStatus(OutcomeFilter outcomeFilter);

    /**
     * 根据企业编号和出库单编号获取id和OutcomeWareCommon的映射
     * @author canlei
     * @param idList
     * @return
     */
    Map<Long, OutcomeWareCommon> mapIdAndObject(List<Long> idList);

}
