package com.zjft.usp.anyfix.work.fee.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDto;
import com.zjft.usp.anyfix.work.fee.model.WorkFee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 工单费用表 Mapper 接口
 * </p>
 *
 * @author canlei
 * @since 2020-01-06
 */
public interface WorkFeeMapper extends BaseMapper<WorkFee> {

    /**
     * 根据条件分页查询工单费用
     *
     * @author canlei
     * @param page
     * @param workFilter
     * @return
     */
    List<WorkFeeDto> queryByWorkFilter(Page page, @Param("workFilter") WorkFilter workFilter);

    /**
     * 根据条件查询工单费用
     *
     * @author canlei
     * @param workFilter
     * @return
     */
    List<WorkFeeDto> listByWorkFilter(@Param("workFilter") WorkFilter workFilter);

    /**
     * 根据对账单号列表查询
     *
     * @param verifyIdList
     * @return
     */
    List<WorkFeeDto> listByVerifyIdList(@Param("verifyIdList") List<Long> verifyIdList);

}
