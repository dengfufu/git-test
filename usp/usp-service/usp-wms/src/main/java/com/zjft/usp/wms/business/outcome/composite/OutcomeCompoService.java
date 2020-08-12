package com.zjft.usp.wms.business.outcome.composite;

import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.outcome.dto.OutcomeMainCommonSaveDto;
import com.zjft.usp.wms.business.outcome.dto.OutcomeWareCommonDto;
import com.zjft.usp.wms.business.outcome.filter.OutcomeFilter;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 *
 * @author zphu
 * @date 2019/11/22 15:19
**/
public interface OutcomeCompoService {
    /**
     * 添加物料出库申请
     *
     * @param outcomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author zphu
     * @date 2019/11/21 15:52
     * @throws
     **/
    void add(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 删除物料出库申请
     *
     * @param outcomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author zphu
     * @date 2019/11/26 10:04
     * @throws
    **/
    void delete(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 修改物料出库申请
     *
     * @param outcomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author zphu
     * @date 2019/11/26 10:13
     * @throws
    **/
    void update(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 批量修改物料出库申请
     *
     * @param outcomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author zphu
     * @date 2019/11/26 10:29
     * @throws
    **/
    void batchUpdate(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 保存出库申请
     *
     * @param outcomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author zphu
     * @date 2019/11/22 15:11
     * @throws
     **/
    void save(OutcomeWareCommonDto outcomeWareCommonDto,UserInfo userInfo, ReqParam reqParam);

    /**
     * 分页查询出库记录
     *
     * @param outcomeFilter
     * @param userInfo
     * @param reqParam
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.business.outcome.dto.OutcomeWareCommonDto>
     * @author zphu
     * @date 2019/11/25 14:23
     * @throws
     **/
    ListWrapper<OutcomeWareCommonDto> list(OutcomeFilter outcomeFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查询某个工单详情
     *
     * @param outcomeId
     * @param userInfo
     * @param reqParam
     * @return com.zjft.usp.wms.business.outcome.dto.OutcomeWareCommonDto
     * @author zphu
     * @date 2019/11/29 14:10
     * @throws
    **/
    OutcomeWareCommonDto detail(Long outcomeId, UserInfo userInfo, ReqParam reqParam);

    /**
     * 审核出库记录
     *
     * @param outcomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zphu
     * @date 2019/11/25 14:23
     * @throws
     **/
    void audit(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 批量审核出库记录
     *
     * @param outcomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author zphu
     * @date 2019/11/25 20:00
     * @throws
    **/
    void batchAudit(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 分页查询出库申请保存列表
     *
     * @param outcomeFilter
     * @param userInfo
     * @param reqParam
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.business.income.dto.OutcomeMainCommonSaveDto>
     * @author zphu
     * @date 2019/11/25 16:39
     * @throws
    **/
    ListWrapper<OutcomeMainCommonSaveDto> listSave(OutcomeFilter outcomeFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查询保存的出库记录
     *
     * @param outcomeId
     * @param userInfo
     * @param reqParam
     * @return com.zjft.usp.wms.business.outcome.dto.OutcomeMainCommonSaveDto
     * @author zphu
     * @date 2019/12/11 10:19
     * @throws
    **/
    OutcomeMainCommonSaveDto detailSave(Long outcomeId, UserInfo userInfo,ReqParam reqParam);

    /**
     * 删除暂存的出库申请
     *
     * @param outcomeId
     * @return void
     * @author zphu
     * @date 2019/11/25 19:25
     * @throws
    **/
    void deleteSave(Long outcomeId);

    /**
     * 修改暂存的出库
     *
     * @param outcomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author zphu
     * @date 2019/11/25 19:45
     * @throws
    **/
    void updateSave(OutcomeWareCommonDto outcomeWareCommonDto,UserInfo userInfo, ReqParam reqParam);

    /**
     * 根据状态统计数量
     *
     * @author zphu
     * @param outcomeFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    Map<Integer, Long> countByOutcomeStatus(OutcomeFilter outcomeFilter, UserInfo userInfo, ReqParam reqParam);
}
