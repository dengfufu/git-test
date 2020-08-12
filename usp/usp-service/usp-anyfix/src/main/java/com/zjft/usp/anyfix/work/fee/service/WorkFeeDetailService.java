package com.zjft.usp.anyfix.work.fee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeAssortDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDetailDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeImplementDto;
import com.zjft.usp.anyfix.work.fee.filter.WorkFeeDetailFilter;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeDetail;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工单费用明细 服务类
 * </p>
 *
 * @author canlei
 * @since 2020-04-17
 */
public interface WorkFeeDetailService extends IService<WorkFeeDetail> {

    /**
     * 根据实施发生费用编号查询费用明细
     *
     * @param implementId
     * @return
     */
    List<WorkFeeDetail> listByImplementId(Long implementId);

    /**
     * 根据分类费用编号获取已审核工单费用明细
     *
     * @param assortId
     * @return
     */
    List<WorkFeeDetail> listCheckedByAssortId(Long assortId);

    /**
     * 根据实施发生费用生成工单费用明细，返回总额
     *
     * @param implementDtoList
     * @param workId
     * @param userId
     * @return
     */
    BigDecimal addByImplementFeeList(List<WorkFeeImplementDto> implementDtoList, Long workId, Long userId);

    /**
     * 根据工单生成分类费用明细，返回总额
     *
     * @param workDto
     * @param userId
     * @return
     */
    BigDecimal addAssortFeeDetail(WorkDto workDto, Long userId);

    /**
     * 获取工单费用明细
     *
     * @param workId
     * @return
     */
    List<WorkFeeDetailDto> listByWorkId(Long workId);

    /**
     * 查询工单费用明细
     *
     * @param workFeeDetailFilter
     * @return
     */
    List<WorkFeeDetailDto> listByFilter(WorkFeeDetailFilter workFeeDetailFilter);

    /**
     * 添加分类费用定义时，初始化未审核的满足条件的工单该分类费用
     *
     * @param workFeeAssortDto
     * @param userId
     */
    void addDetailByAssort(WorkFeeAssortDto workFeeAssortDto, Long userId);

    /**
     * 根据分类费用编号以及工单编号列表获取工单编号和对应费用明细的映射
     *
     * @param assortId
     * @param workIdList
     * @return
     */
    Map<Long, WorkFeeDetail> mapWorkIdAndFeeDetailByAssortId(Long assortId, List<Long> workIdList);

    /**
     * 更新分类费用定义时，初始化未审核的满足条件的工单该分类费用
     *
     * @param workFeeAssortDto
     * @param userId
     */
    void updateDetailByAssort(WorkFeeAssortDto workFeeAssortDto, Long userId);

    /**
     * 删除未审核工单的指定分类费用
     *
     * @param assortId
     */
    void delUncheckDetailByAssort(Long assortId);

    /**
     * 根据workIdList获取WorkFeeDetailDto
     *
     * @param workIdList
     * @return
     */
    List<WorkFeeDetailDto> listDtoByWorkIdList(List<Long> workIdList);

    /**
     * 根据工单编号查询实施发生费用明细
     *
     * @param workId
     * @return
     */
    List<WorkFeeDetailDto> listImplementByWorkId(Long workId);

    /**
     * 根据工单编号删除实施发生费用明细
     *
     * @param workId
     */
    void delImplementById(Long workId);

}
