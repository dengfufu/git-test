package com.zjft.usp.anyfix.work.fee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeVerifyDetailExcelDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeVerifyDto;
import com.zjft.usp.anyfix.work.fee.filter.WorkFeeVerifyFilter;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeVerify;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
import com.zjft.usp.common.model.ListWrapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 委托商对账单表 服务类
 * </p>
 *
 * @author canlei
 * @since 2020-05-12
 */
public interface WorkFeeVerifyService extends IService<WorkFeeVerify> {

    /**
     * 分页查询
     *
     * @param workFeeVerifyFilter
     * @return
     */
    ListWrapper<WorkFeeVerifyDto> query(WorkFeeVerifyFilter workFeeVerifyFilter, Long userId, Long corpId);

    /**
     * 查询可对账工单
     *
     * @param workFilter
     * @return
     */
    WorkFeeVerifyDto listCanVerifyWork(WorkFilter workFilter);

    /**
     * 添加对账单
     *
     * @param workFeeVerifyDto
     * @param userId
     * @param corpId
     */
    void add(WorkFeeVerifyDto workFeeVerifyDto, Long userId, Long corpId);

    /**
     * 更新对账单
     *
     * @param workFeeVerifyDto
     * @param userId
     */
    void update(WorkFeeVerifyDto workFeeVerifyDto, Long userId);

    /**
     * 提交对账
     *
     * @param workFeeVerifyDto
     * @param userId
     */
    void submit(WorkFeeVerifyDto workFeeVerifyDto, Long userId);

    /**
     * 删除对账单
     *
     * @param verifyId
     * @param userId
     * @param corpId
     */
    void delete(Long verifyId, Long userId, Long corpId);

    /**
     * 获取对账单详情
     *
     * @param verifyId
     * @return
     */
    WorkFeeVerifyDto findDetail(Long verifyId);

    /**
     * 对账
     *
     * @param workFeeVerifyDto
     * @param userId
     * @param corpId
     */
    void verify(WorkFeeVerifyDto workFeeVerifyDto, Long userId, Long corpId);

    /**
     * 确认
     *
     * @param workFeeVerifyDto
     * @param userId
     * @param corpId
     */
    void confirm(WorkFeeVerifyDto workFeeVerifyDto, Long userId, Long corpId);

    /**
     * 分页查询可结算对账单
     *
     * @param workFeeVerifyFilter
     * @return
     */
    ListWrapper<WorkFeeVerifyDto> queryCanSettleVerify(WorkFeeVerifyFilter workFeeVerifyFilter);

    /**
     * 获取对账单号
     *
     * @param verifyIdList
     * @return
     */
    Map<Long, BigDecimal> mapVerifyIdAndWorkTotalFee(List<Long> verifyIdList);

    /**
     * 导入对账
     *
     * @param excelDtoList
     * @param verifyId
     * @param userId
     * @param corpId
     * @return
     */
    String importVerify(List<WorkFeeVerifyDetailExcelDto> excelDtoList, Long verifyId, Long userId, Long corpId);

    /**
     * 生成对账单号
     *
     * @param prefix
     * @return
     */
    String createVerifyName(String prefix);

    /**
     * 自动生成对账单
     *
     * @param serviceCorp
     * @return
     */
    String autoCreateVerify(Long serviceCorp);

    /**
     * 根据对账单编号列表获取编号和对账单的映射
     *
     * @param verifyIdList
     * @return
     */
    Map<Long, WorkFeeVerify> mapIdAndVerify(List<Long> verifyIdList);

}
