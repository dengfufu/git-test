package com.zjft.usp.wms.business.outcome.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.outcome.dto.OutcomeDetailCommonSaveDto;
import com.zjft.usp.wms.business.outcome.dto.OutcomeMainCommonSaveDto;
import com.zjft.usp.wms.business.outcome.dto.OutcomeStatDto;
import com.zjft.usp.wms.business.outcome.filter.OutcomeFilter;
import com.zjft.usp.wms.business.outcome.model.OutcomeMainCommonSave;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 出库基本信息共用表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface OutcomeMainCommonSaveService extends IService<OutcomeMainCommonSave> {

    /**
     * 分页查询保存的出口信息
     * @param outcomeFilter
     * @param page
     * @return
     */
    List<OutcomeMainCommonSaveDto> listSaveByPage(OutcomeFilter outcomeFilter, Page page);

    /**
     * 查询保存数量
     *
     * @param userInfo
     * @param reqParam
     * @return com.zjft.usp.wms.business.outcome.dto.OutcomeStatDto
     * @author zphu
     * @date 2019/12/9 17:13
     * @throws
    **/
    OutcomeStatDto countSave(UserInfo userInfo, ReqParam reqParam);

    /**
     * 根据参数获取dto
     *
     * @param outcomeId
     * @return com.zjft.usp.wms.business.outcome.dto.OutcomeMainCommonSaveDto
     * @author zphu
     * @date 2019/12/11 10:51
     * @throws
    **/
    OutcomeMainCommonSaveDto getDetailById(Long outcomeId);

}
