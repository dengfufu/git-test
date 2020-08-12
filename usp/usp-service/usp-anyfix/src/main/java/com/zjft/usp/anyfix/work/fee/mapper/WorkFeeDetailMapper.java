package com.zjft.usp.anyfix.work.fee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDetailDto;
import com.zjft.usp.anyfix.work.fee.filter.WorkFeeDetailFilter;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 工单费用明细 Mapper 接口
 * </p>
 *
 * @author canlei
 * @since 2020-04-17
 */
public interface WorkFeeDetailMapper extends BaseMapper<WorkFeeDetail> {

    /**
     * 根据工单编号获取费用明细列表
     *
     * @param workId
     * @return
     */
    List<WorkFeeDetailDto> listDtoByWorkId(@Param("workId") Long workId);

    /**
     * 查询工单费用明细
     *
     * @param workFeeDetailFilter
     * @return
     */
    List<WorkFeeDetailDto> listDtoByFilter(@Param("workFeeDetailFilter") WorkFeeDetailFilter workFeeDetailFilter);

    /**
     * 根据分类费用编号获取已审核的工单费用明细
     *
     * @param assortId
     * @return
     */
    List<WorkFeeDetail> listCheckedByAssortId(@Param("assortId") Long assortId);

    /**
     * 根据分类费用编号获取未审核的工单费用明细
     *
     * @param assortId
     * @return
     */
    List<WorkFeeDetail> listUncheckedByAssortId(@Param("assortId") Long assortId);

    /**
     * 根据支出类别分组查询费用
     *
     * @param workIdList
     * @return
     */
    List<WorkFeeDetailDto> listByTypeAndWorkId(@Param("workIdList") List<Long> workIdList);

}
