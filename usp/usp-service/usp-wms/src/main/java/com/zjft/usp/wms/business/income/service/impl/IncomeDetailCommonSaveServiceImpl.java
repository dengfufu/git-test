package com.zjft.usp.wms.business.income.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.wms.business.income.mapper.IncomeDetailCommonSaveMapper;
import com.zjft.usp.wms.business.income.model.IncomeDetailCommonSave;
import com.zjft.usp.wms.business.income.service.IncomeDetailCommonSaveService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 入库明细信息共用表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
public class IncomeDetailCommonSaveServiceImpl extends ServiceImpl<IncomeDetailCommonSaveMapper,
        IncomeDetailCommonSave> implements IncomeDetailCommonSaveService {
    @Resource
    private IncomeDetailCommonSaveMapper incomeDetailCommonSaveMapper;

    /**
     * 根据入库单号删除入库明细信息
     *
     * @author Qiugm
     * @date 2019-11-13
     * @param incomeId
     * @return void
     */
    @Override
    public void deleteByIncomeId(Long incomeId) {
        this.incomeDetailCommonSaveMapper.deleteByIncomeId(incomeId);
    }

}
