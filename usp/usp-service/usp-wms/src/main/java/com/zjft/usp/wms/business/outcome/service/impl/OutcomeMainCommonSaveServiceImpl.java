package com.zjft.usp.wms.business.outcome.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.outcome.dto.OutcomeMainCommonSaveDto;
import com.zjft.usp.wms.business.outcome.dto.OutcomeStatDto;
import com.zjft.usp.wms.business.outcome.filter.OutcomeFilter;
import com.zjft.usp.wms.business.outcome.model.OutcomeMainCommonSave;
import com.zjft.usp.wms.business.outcome.mapper.OutcomeMainCommonSaveMapper;
import com.zjft.usp.wms.business.outcome.service.OutcomeMainCommonSaveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 出库基本信息共用表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
public class OutcomeMainCommonSaveServiceImpl extends ServiceImpl<OutcomeMainCommonSaveMapper, OutcomeMainCommonSave> implements OutcomeMainCommonSaveService {

    @Override
    public List<OutcomeMainCommonSaveDto> listSaveByPage(OutcomeFilter outcomeFilter, Page page) {
        return this.baseMapper.listSaveByPage(outcomeFilter,page);
    }

    @Override
    public OutcomeStatDto countSave(UserInfo userInfo, ReqParam reqParam) {
        return this.baseMapper.countSave(userInfo.getUserId(),reqParam.getCorpId());
    }

    @Override
    public OutcomeMainCommonSaveDto getDetailById(Long outcomeId) {
        return this.baseMapper.getDetailById(outcomeId);
    }
}
