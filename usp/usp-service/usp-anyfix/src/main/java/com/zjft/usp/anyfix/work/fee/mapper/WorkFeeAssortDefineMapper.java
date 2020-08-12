package com.zjft.usp.anyfix.work.fee.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeAssortDto;
import com.zjft.usp.anyfix.work.fee.filter.WorkFeeAssortFilter;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeAssortDefine;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 分类费用定义 Mapper 接口
 * </p>
 *
 * @author canlei
 * @since 2020-04-17
 */
public interface WorkFeeAssortDefineMapper extends BaseMapper<WorkFeeAssortDefine> {

    /**
     * 分页查询
     *
     * @param page
     * @param workFeeAssortFilter
     * @return
     */
    List<WorkFeeAssortDto> pageByFilter(@Param("page") Page page,
                                        @Param("workFeeAssortFilter") WorkFeeAssortFilter workFeeAssortFilter);

    /**
     * 匹配分类费用
     *
     * @param workDto
     * @return
     */
    List<WorkFeeAssortDto> matchWorkFeeAssortDefine(@Param("workDto") WorkDto workDto);

    /**
     * 查询满足条件的工单
     *
     * @param workFeeAssortDto
     * @return
     */
    List<WorkDto> selectMatchWork(@Param("workFeeAssortDto") WorkFeeAssortDto workFeeAssortDto);

}
