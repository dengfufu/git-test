package com.zjft.usp.wms.business.outcome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.outcome.dto.OutcomeDetailCommonSaveDto;
import com.zjft.usp.wms.business.outcome.filter.OutcomeFilter;
import com.zjft.usp.wms.business.outcome.model.OutcomeDetailCommonSave;

import java.util.List;

/**
 * <p>
 * 出库明细信息共用表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface OutcomeDetailCommonSaveService extends IService<OutcomeDetailCommonSave> {

    /**
     * 分页查询保存的出库记录
     *
     * @param outcomeFilter
     * @param reqParam
     * @param userInfo
     * @return java.util.List<com.zjft.usp.wms.business.outcome.dto.OutcomeDetailCommonSaveDto>
     * @author zphu
     * @date 2019/11/25 16:55
     * @throws
     **/
    List<OutcomeDetailCommonSaveDto> listSaveByPage(OutcomeFilter outcomeFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 根据参数获取dto
     *
     * @param outcomeId
     * @return java.util.List<com.zjft.usp.wms.business.outcome.dto.OutcomeDetailCommonSaveDto>
     * @author zphu
     * @date 2019/12/11 10:58
     * @throws
    **/
    List<OutcomeDetailCommonSaveDto> listByOutcomeId(Long outcomeId);
}
