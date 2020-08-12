package com.zjft.usp.wms.business.income.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.income.dto.IncomeStatDto;
import com.zjft.usp.wms.business.income.filter.IncomeFilter;
import com.zjft.usp.wms.business.income.model.IncomeMainCommonSave;

import java.util.List;

/**
 * <p>
 * 入库基本信息共用表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface IncomeMainCommonSaveService extends IService<IncomeMainCommonSave> {

    /***
     * 根据条件查询未提交的入库单
     *
     * @author Qiugm
     * @date 2019-11-19
     * @param page
     * @param incomeFilter
     * @return java.util.List<com.zjft.usp.wms.business.income.model.IncomeMainCommonSave>
     */
    List<IncomeMainCommonSave> listByPage(Page<IncomeMainCommonSave> page, IncomeFilter incomeFilter);

    /**
     * 查询自己保存的入库单数量
     *
     * @author canlei
     * @param userInfo
     * @param reqParam
     * @return
     */
    IncomeStatDto countSave(UserInfo userInfo, ReqParam reqParam);
}
