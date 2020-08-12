package com.zjft.usp.wms.business.outcome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.wms.business.outcome.dto.OutcomeDetailCommonSaveDto;
import com.zjft.usp.wms.business.outcome.dto.OutcomeWareCommonDto;
import com.zjft.usp.wms.business.outcome.filter.OutcomeFilter;
import com.zjft.usp.wms.business.outcome.model.OutcomeDetailCommonSave;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 出库明细信息共用表 Mapper 接口
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface OutcomeDetailCommonSaveMapper extends BaseMapper<OutcomeDetailCommonSave> {

    /**
     * 分页查询保存的出库记录
     *
     * @param outcomeFilter
     * @param page
     * @return java.util.List<com.zjft.usp.wms.business.outcome.dto.OutcomeWareCommonDto>
     * @author zphu
     * @date 2019/11/25 17:07
     * @throws
    **/
    List<OutcomeWareCommonDto> listSaveByPage(@Param("outcomeFilter") OutcomeFilter outcomeFilter, Page page);

    /**
     * 根据主表id获取详情列表
     *
     * @param outcomeId
     * @return java.util.List<com.zjft.usp.wms.business.outcome.dto.OutcomeDetailCommonSaveDto>
     * @author zphu
     * @date 2019/12/11 10:59
     * @throws
    **/
    List<OutcomeDetailCommonSaveDto> listByOutcomeId(@Param("outcomeId") Long outcomeId);
}
