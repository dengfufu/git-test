package com.zjft.usp.wms.business.income.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.income.dto.IncomeStatDto;
import com.zjft.usp.wms.business.income.filter.IncomeFilter;
import com.zjft.usp.wms.business.income.mapper.IncomeMainCommonSaveMapper;
import com.zjft.usp.wms.business.income.model.IncomeMainCommonSave;
import com.zjft.usp.wms.business.income.service.IncomeMainCommonSaveService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 入库基本信息共用表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
public class IncomeMainCommonSaveServiceImpl extends ServiceImpl<IncomeMainCommonSaveMapper, IncomeMainCommonSave>
        implements IncomeMainCommonSaveService {

    @Resource
    private IncomeMainCommonSaveMapper incomeMainCommonSaveMapper;

    /***
     * 根据条件查询未提交的入库单
     *
     * @author Qiugm
     * @date 2019-11-19
     * @param page
     * @param incomeFilter
     * @return java.util.List<com.zjft.usp.wms.business.income.model.IncomeMainCommonSave>
     */
    @Override
    public List<IncomeMainCommonSave> listByPage(Page<IncomeMainCommonSave> page, IncomeFilter incomeFilter) {
        QueryWrapper<IncomeMainCommonSave> queryWrapper = new QueryWrapper<>();
        if (LongUtil.isNotZero(incomeFilter.getCorpId())) {
            queryWrapper.eq("corp_id", incomeFilter.getCorpId());
        }
        queryWrapper.orderByAsc("create_time");
        IPage iPage = this.page(page, queryWrapper);
        return iPage.getRecords();
    }

    /**
     * 查询自己保存的入库单数量
     *
     * @author canlei
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public IncomeStatDto countSave(UserInfo userInfo, ReqParam reqParam) {
        return this.incomeMainCommonSaveMapper.countSave(userInfo.getUserId(), reqParam.getCorpId());
    }

}
