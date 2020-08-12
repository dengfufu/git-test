package com.zjft.usp.anyfix.work.fee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeVerifyDetailDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeVerifyDetailExcelDto;
import com.zjft.usp.anyfix.work.fee.filter.WorkFeeVerifyDetailFilter;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeVerifyDetail;
import com.zjft.usp.common.model.ListWrapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 委托商对账单明细表 服务类
 * </p>
 *
 * @author canlei
 * @since 2020-05-12
 */
public interface WorkFeeVerifyDetailService extends IService<WorkFeeVerifyDetail> {

    /**
     * 根据对账单编号查询
     *
     * @param verifyId
     * @return
     */
    List<WorkFeeVerifyDetailDto> listDtoByVerifyId(Long verifyId);

    /**
     * 分页查询
     *
     * @param workFeeVerifyDetailFilter
     * @return
     */
    ListWrapper<WorkFeeVerifyDetailDto> query(WorkFeeVerifyDetailFilter workFeeVerifyDetailFilter);

    /**
     * 根据对账单编号删除
     *
     * @param verifyId
     */
    void deleteByVerifyId(Long verifyId);

    /**
     * 根据对账单号查询WorkDeal列表
     *
     * @param verifyId
     * @return
     */
    List<WorkDeal> listWorkDealByVerifyId(Long verifyId);

    /**
     * 查询对账单号明细
     *
     * @param verifyId
     * @return
     */
    List<WorkFeeVerifyDetail> listByVerifyId(Long verifyId);

    /**
     * 根据对账单号list获取明细list
     *
     * @param verifyIdList
     * @return
     */
    List<WorkFeeVerifyDetail> listByVerifyIdList(List<Long> verifyIdList);

    /**
     * 根据工单编号查询
     *
     * @param workId
     * @return
     */
    WorkFeeVerifyDetail findByWorkId(Long workId);

    /**
     * 根据对账单号获取工单编号与对账明细的映射
     *
     * @param verifyId
     * @return
     */
    Map<Long, WorkFeeVerifyDetail> mapWorkIdAndDetailByVerifyId(Long verifyId);

    /**
     * 根据对账单编号获取导出明细列表
     *
     * @param verifyId
     * @return
     */
    List<WorkFeeVerifyDetailExcelDto> listExcelDto(Long verifyId);

    /**
     * 根据工单编号列表获取工单编号和对账单明细的映射
     *
     * @param workIdList
     * @return
     */
    Map<Long, WorkFeeVerifyDetail> mapWorkIdAndDetail(List<Long> workIdList);

}
