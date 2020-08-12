package com.zjft.usp.wms.business.outcome.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.outcome.dto.OutcomeDetailCommonSaveDto;
import com.zjft.usp.wms.business.outcome.filter.OutcomeFilter;
import com.zjft.usp.wms.business.outcome.mapper.OutcomeDetailCommonSaveMapper;
import com.zjft.usp.wms.business.outcome.model.OutcomeDetailCommonSave;
import com.zjft.usp.wms.business.outcome.service.OutcomeDetailCommonSaveService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 出库明细信息共用表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
public class OutcomeDetailCommonSaveServiceImpl extends ServiceImpl<OutcomeDetailCommonSaveMapper, OutcomeDetailCommonSave> implements OutcomeDetailCommonSaveService {

    @Override
    public List<OutcomeDetailCommonSaveDto> listSaveByPage(OutcomeFilter outcomeFilter, UserInfo userInfo, ReqParam reqParam){
        outcomeFilter.setUserId(userInfo.getUserId());
        outcomeFilter.setUserCorpId(reqParam.getCorpId());
        return null;
    }

    @Override
    public List<OutcomeDetailCommonSaveDto> listByOutcomeId(Long outcomeId) {
        return this.baseMapper.listByOutcomeId(outcomeId);
    }
}
