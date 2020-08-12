package com.zjft.usp.wms.business.income.composite;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.income.dto.IncomeWareCommonDto;
import com.zjft.usp.wms.business.income.filter.IncomeFilter;

import java.util.Map;

/**
 * 物料入库服务接口
 *
 * @author Qiugm
 * @version 1.0
 * @date 2019-11-13 13:59
 **/

public interface IncomeCompoService {
    /**
     * 保存入库申请
     *
     * @author Qiugm
     * @date 2019-11-11
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    void save(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 修改入库申请
     *
     * @author Qiugm
     * @date 2019-11-14
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    void update(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 添加入库申请
     *
     * @author Qiugm
     * @date 2019-11-11
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    void add(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 删除入库申请
     *
     * @author Qiugm
     * @date 2019-11-14
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    void delete(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 审批入库申请
     *
     * @author Qiugm
     * @date 2019-11-11
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    void audit(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 批量审批入库申请
     *
     * @author Qiugm
     * @date 2019-11-22
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    void batchAudit(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查看申请详情
     *
     * @author Qiugm
     * @date 2019-11-11
     * @param applyId
     * @param reqParam
     * @return com.zjft.usp.wms.business.income.dto.IncomeMainCommonDto
     */
    IncomeWareCommonDto viewIncome(Long applyId, ReqParam reqParam);

    /**
     * 查询入库申请记录
     *
     * @author Qiugm
     * @date 2019-11-11
     * @param incomeFilter
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.business.income.dto.IncomeMainCommonDto>
     */
    ListWrapper<IncomeWareCommonDto> listIncome(IncomeFilter incomeFilter);

    /**
     * 查询暂存未提交的入库单
     *
     * @author Qiugm
     * @date 2019-11-19
     * @param incomeFilter
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.business.income.dto.IncomeWareCommonDto>
     */
    ListWrapper<IncomeWareCommonDto> listSaveIncome(IncomeFilter incomeFilter);

    /**
     * 根据状态统计数量
     *
     * @author canlei
     * @param incomeFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    Map<Integer, Long> countByIncomeStatus(IncomeFilter incomeFilter, UserInfo userInfo, ReqParam reqParam);

}
