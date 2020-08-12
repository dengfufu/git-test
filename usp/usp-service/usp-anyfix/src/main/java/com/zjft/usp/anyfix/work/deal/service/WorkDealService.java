package com.zjft.usp.anyfix.work.deal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.work.check.dto.WorkCheckDto;
import com.zjft.usp.anyfix.work.deal.dto.EngineerDto;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.anyfix.work.request.dto.WorkRequestDto;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 工单处理信息表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-23
 */
public interface WorkDealService extends IService<WorkDeal> {

    /**
     * 新增工单处理信息
     *
     * @param workRequestDto
     * @return
     * @author zgpi
     * @date 2019/9/24 9:27 上午
     **/
    void addWorkDeal(WorkRequestDto workRequestDto);

    /**
     * 修改工单处理信息
     *
     * @param workRequestDto
     * @return
     * @author zgpi
     * @date 2020/2/26 19:53
     */
    void modWorkDealByEdit(WorkRequestDto workRequestDto);

    /**
     * 根据工单号查询列表
     *
     * @param workIdList
     * @return
     * @date 2020/2/18
     */
    List<WorkDeal> queryWorkDeal(Set<Long> workIdList);

    /**
     * 查询工程师
     *
     * @param workFilter
     * @return
     */
    List<EngineerDto> queryEngineerDto(WorkFilter workFilter);

    /**
     * 修改协同工程师
     *
     * @param workDto
     * @param curUserId
     */
    void updateTogetherEngineers(WorkDto workDto, Long curUserId);

    /**
     * 审核服务
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/5/29 11:16
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
     * @date 2020/5/29 11:16
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
     * @date 2020/5/29 13:42
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
     * @date 2020/5/29 13:42
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
     * @date 2020/6/1 10:23
     **/
    void batchCheckService(WorkCheckDto workCheckDto,
                           Long curUserId, Long curCorpId);

    /**
     * 批量审核费用
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/6/1 10:23
     **/
    void batchCheckFee(WorkCheckDto workCheckDto,
                       Long curUserId, Long curCorpId);

    /**
     * 批量确认服务
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/6/1 15:48
     **/
    void batchConfirmService(WorkCheckDto workCheckDto,
                             Long curUserId, Long curCorpId);

    /**
     * 批量确认费用
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/6/1 15:48
     **/
    void batchConfirmFee(WorkCheckDto workCheckDto,
                         Long curUserId, Long curCorpId);

    /**
     * 自动确认服务
     *
     * @param workIdList
     * @param workCheckDto
     */
    void autoConfirmService(List<Long> workIdList, WorkCheckDto workCheckDto);

    /**
     * 自动确认费用
     *
     * @param workIdList
     * @param workCheckDto
     */
    void autoConfirmFee(List<Long> workIdList, WorkCheckDto workCheckDto);


    /**
     * 查询是否存在相同类型的未完成工单
     * @param workRequestDto
     * @return
     */
    List<WorkDeal> listSameWork(WorkRequestDto workRequestDto);
}
