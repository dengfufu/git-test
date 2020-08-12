package com.zjft.usp.anyfix.work.fee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDto;
import com.zjft.usp.anyfix.work.fee.model.WorkFee;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
import com.zjft.usp.common.model.ListWrapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工单费用表 服务类
 * </p>
 *
 * @author canlei
 * @since 2020-01-06
 */
public interface WorkFeeService extends IService<WorkFee> {

    /**
     * 根据工单编号查询
     *
     * @param workId
     * @return
     * @author canlei
     */
    WorkFeeDto getDtoById(Long workId);

    /**
     * 更新
     *
     * @param workFeeDto
     * @param curUserId
     * @author canlei
     */
    void update(WorkFeeDto workFeeDto, Long curUserId, Long curCorpId);

    /**
     * 根据条件分页查询
     *
     * @author canlei
     * @param workFilter
     * @return
     */
    ListWrapper<WorkFeeDto> queryByWorkFilter(WorkFilter workFilter);

    /**
     * 根据条件查询
     *
     * @author canlei
     * @param workFilter
     * @return
     */
    List<WorkFeeDto> listByWorkFilter(WorkFilter workFilter);

    /**
     * 获取总费用
     *
     * @param workFee
     * @return
     */
    BigDecimal getTotalFee(WorkFee workFee);

    /**
     * 根据工单编号列表获取工单编号与工单费用的映射
     *
     * @param workIdList
     * @return
     */
    Map<Long, WorkFee> mapWorkFee(List<Long> workIdList);

    /**
     * 根据对账单列表查询
     *
     * @param verifyIdList
     * @return
     */
    List<WorkFeeDto> listByVerifyIdList(List<Long> verifyIdList);

    /**
     * 根据工单编号列表获取工单编号与WorkFeeDto的映射
     *
     * @param workIdList
     * @return
     */
    Map<Long, WorkFeeDto> mapWorkIdAndFeeDto(List<Long> workIdList);

}
