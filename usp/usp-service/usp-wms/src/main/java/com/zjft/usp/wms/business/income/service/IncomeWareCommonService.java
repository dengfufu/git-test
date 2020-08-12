package com.zjft.usp.wms.business.income.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.income.dto.IncomeStatDto;
import com.zjft.usp.wms.business.income.dto.IncomeWareCommonDto;
import com.zjft.usp.wms.business.income.filter.IncomeFilter;
import com.zjft.usp.wms.business.income.model.IncomeWareCommon;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 入库明细正式表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-12
 */
public interface IncomeWareCommonService extends IService<IncomeWareCommon> {

    /**
     * 根据条件查询入库单
     *
     * @author Qiugm
     * @date 2019-11-13
     * @param page
     * @param incomeFilter
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.business.income.dto.IncomeMainCommonDto>
     */
    List<IncomeWareCommonDto> listByPage(Page page, IncomeFilter incomeFilter);

    /**
     * 更新入库状态
     *
     * @author Qiugm
     * @date 2019-11-14
     * @param incomeWareCommon
     * @return void
     */
    void updateIncomeStatus(IncomeWareCommon incomeWareCommon);

    /**
     * 根据企业编号和入库单编号获取id和IncomeWareCommon的映射
     * @author canlei
     * @param corpId
     * @param idList
     * @return
     */
    Map<Long, IncomeWareCommon> mapByCorpIdAndIncomeIds(Long corpId, List<Long> idList);

    /**
     * 根据状态统计数量
     *
     * @author canlei
     * @param incomeFilter
     * @param curUserId
     * @return
     */
    List<IncomeStatDto> countByIncomeStatus(IncomeFilter incomeFilter, Long curUserId);
}
