package com.zjft.usp.anyfix.work.check.composite;

import com.zjft.usp.anyfix.work.check.dto.WorkCheckDto;

/**
 * 工单审核聚合服务类
 *
 * @author zgpi
 * @date 2020/5/11 20:24
 */
public interface WorkCheckCompoService {

    /**
     * 获得工单审核记录
     *
     * @param workId
     * @return
     * @author zgpi
     * @date 2020/5/12 16:36
     **/
    WorkCheckDto findWorkCheck(Long workId);

    /**
     * 审核服务
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/5/11 20:24
     **/
    void checkService(WorkCheckDto workCheckDto, Long curUserId, Long curCorpId);

    /**
     * 审核费用
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/5/29 11:13
     **/
    void checkFee(WorkCheckDto workCheckDto, Long curUserId, Long curCorpId);

    /**
     * 确认服务
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/5/29 11:22
     **/
    void confirmService(WorkCheckDto workCheckDto, Long curUserId, Long curCorpId);

    /**
     * 确认费用
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/5/29 11:22
     **/
    void confirmFee(WorkCheckDto workCheckDto, Long curUserId, Long curCorpId);

    /**
     * 批量审核服务
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/6/1 10:20
     **/
    String batchCheckService(WorkCheckDto workCheckDto, Long curUserId, Long curCorpId);

    /**
     * 批量审核费用
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/6/1 10:20
     **/
    String batchCheckFee(WorkCheckDto workCheckDto, Long curUserId, Long curCorpId);

    /**
     * 批量确认服务
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/6/1 15:43
     **/
    String batchConfirmService(WorkCheckDto workCheckDto, Long curUserId, Long curCorpId);

    /**
     * 批量确认费用
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/6/1 15:43
     **/
    String batchConfirmFee(WorkCheckDto workCheckDto, Long curUserId, Long curCorpId);

    /**
     * 自动确认服务
     *
     * @param serviceCorp
     * @return
     */
    String autoConfirmService(Long serviceCorp);

    /**
     * 自动确认费用
     *
     * @param serviceCorp
     * @return
     */
    String autoConfirmFee(Long serviceCorp);

}
