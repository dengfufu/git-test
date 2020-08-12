package com.zjft.usp.wms.business.outcome.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.wms.business.income.dto.IncomeStatDto;
import com.zjft.usp.wms.business.outcome.dto.OutcomeMainCommonSaveDto;
import com.zjft.usp.wms.business.outcome.dto.OutcomeStatDto;
import com.zjft.usp.wms.business.outcome.filter.OutcomeFilter;
import com.zjft.usp.wms.business.outcome.model.OutcomeMainCommonSave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 出库基本信息共用表 Mapper 接口
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface OutcomeMainCommonSaveMapper extends BaseMapper<OutcomeMainCommonSave> {

    /**
     * 分页查询列表
     *
     * @param outcomeFilter
     * @param page
     * @return java.util.List<com.zjft.usp.wms.business.outcome.dto.OutcomeMainCommonSaveDto>
     * @author zphu
     * @date 2019/11/28 17:35
     * @throws
    **/
    public List<OutcomeMainCommonSaveDto> listSaveByPage(@Param("outcomeFilter") OutcomeFilter outcomeFilter, Page page);

    /**
     * 查询保存数量
     *
     * @param curUserId
    * @param corpId
     * @return com.zjft.usp.wms.business.outcome.dto.OutcomeStatDto
     * @author zphu
     * @date 2019/12/9 17:12
     * @throws
    **/
    OutcomeStatDto countSave(@Param("curUserId") Long curUserId, @Param("corpId") Long corpId);

    /**
     * 根据id获取dto
     *
     * @param outcomeId
     * @return com.zjft.usp.wms.business.outcome.dto.OutcomeMainCommonSaveDto
     * @author zphu
     * @date 2019/12/11 10:50
     * @throws
    **/
    OutcomeMainCommonSaveDto getDetailById(@Param("outcomeId") Long outcomeId);
}
