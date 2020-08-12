package com.zjft.usp.anyfix.work.fee.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeImplementDto;
import com.zjft.usp.anyfix.work.fee.filter.WorkFeeImplementFilter;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeImplementDefine;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 实施发生费用定义 Mapper 接口
 * </p>
 *
 * @author canlei
 * @since 2020-04-17
 */
public interface WorkFeeImplementDefineMapper extends BaseMapper<WorkFeeImplementDefine> {

    /**
     * 分页查询
     *
     * @param page
     * @param workFeeImplementFilter
     * @return
     */
    List<WorkFeeImplementDto> pageByFilter(@Param("page") Page page,
                                           @Param("workFeeImplementFilter") WorkFeeImplementFilter workFeeImplementFilter);

    /**
     * 列表查询
     *
     * @param workFeeImplementFilter
     * @return
     */
    List<WorkFeeImplementDto> listByFilter(@Param("workFeeImplementFilter") WorkFeeImplementFilter workFeeImplementFilter);


}
