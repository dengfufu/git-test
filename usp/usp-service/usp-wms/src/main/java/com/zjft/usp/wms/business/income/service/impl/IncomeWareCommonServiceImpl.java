package com.zjft.usp.wms.business.income.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.income.dto.IncomeStatDto;
import com.zjft.usp.wms.business.income.dto.IncomeWareCommonDto;
import com.zjft.usp.wms.business.income.filter.IncomeFilter;
import com.zjft.usp.wms.business.income.mapper.IncomeWareCommonMapper;
import com.zjft.usp.wms.business.income.model.IncomeMainCommonSave;
import com.zjft.usp.wms.business.income.model.IncomeWareCommon;
import com.zjft.usp.wms.business.income.service.IncomeMainCommonSaveService;
import com.zjft.usp.wms.business.income.service.IncomeWareCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 入库明细正式表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-12
 */
@Service
public class IncomeWareCommonServiceImpl extends ServiceImpl<IncomeWareCommonMapper, IncomeWareCommon> implements IncomeWareCommonService {

    @Resource
    private IncomeWareCommonMapper incomeWareCommonMapper;

    /**
     * 根据条件查询入库单
     *
     * @author Qiugm
     * @date 2019-11-13
     * @param incomeFilter
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.business.income.dto.IncomeMainCommonDto>
     */
    @Override
    public List<IncomeWareCommonDto> listByPage(Page page, IncomeFilter incomeFilter) {
        return incomeWareCommonMapper.listByPage(page, incomeFilter);
    }

    /**
     * 更新入库状态
     *
     * @author Qiugm
     * @date 2019-11-14
     * @param incomeWareCommon
     * @return void
     */
    @Override
    public void updateIncomeStatus(IncomeWareCommon incomeWareCommon) {
        UpdateWrapper<IncomeWareCommon> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id", incomeWareCommon.getId());
        this.update(incomeWareCommon, updateWrapper);
    }

    /**
     * 根据企业编号和入库单编号获取id和IncomeWareCommon的映射
     * @param corpId
     * @param idList
     * @return
     */
    @Override
    public Map<Long, IncomeWareCommon> mapByCorpIdAndIncomeIds(Long corpId, List<Long> idList) {
        Map<Long, IncomeWareCommon> map = new HashMap<>();
        if(LongUtil.isZero(corpId) || CollectionUtil.isEmpty(idList)) {
            return map;
        }
        QueryWrapper<IncomeWareCommon> queryWrapper = new QueryWrapper<IncomeWareCommon>().eq("corp_id", corpId)
                .in("id", idList);
        List<IncomeWareCommon> list = this.list(queryWrapper);
        list.forEach(incomeWareCommon -> {
            map.put(incomeWareCommon.getId(), incomeWareCommon);
        });
        return map;
    }

    /**
     * 根据状态统计数量
     *
     * @author canlei
     * @param incomeFilter
     * @param curUserId
     * @return
     */
    @Override
    public List<IncomeStatDto> countByIncomeStatus(IncomeFilter incomeFilter, Long curUserId) {
        List<IncomeStatDto> countList = this.incomeWareCommonMapper.countByStatus(incomeFilter);
        return countList;
    }

}