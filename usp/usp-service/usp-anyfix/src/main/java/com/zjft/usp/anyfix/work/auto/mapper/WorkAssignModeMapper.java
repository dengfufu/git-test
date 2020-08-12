package com.zjft.usp.anyfix.work.auto.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.work.auto.dto.WorkAssignModeDto;
import com.zjft.usp.anyfix.work.auto.dto.WorkDispatchServiceCorpDto;
import com.zjft.usp.anyfix.work.auto.filter.WorkAssignModeFilter;
import com.zjft.usp.anyfix.work.auto.model.WorkAssignMode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 派单模式配置 Mapper 接口
 * </p>
 *
 * @author zphu
 * @since 2019-09-26
 */
public interface WorkAssignModeMapper extends BaseMapper<WorkAssignMode> {

    /**
     * 分页查询自动派单规则列表
     *
     * @param workAssignModeFilter
     * @param page
     * @return
     * @author zgpi
     * @date 2019/11/14 11:04
     **/
    List<WorkAssignModeDto> query(@Param("filter") WorkAssignModeFilter workAssignModeFilter,
                                  Page page);

    /**
     * 根据条件获得自动派单规则
     *
     * @param workDispatchServiceCorpDto
     * @return
     * @author zgpi
     * @date 2019/11/14 19:00
     **/
    List<WorkAssignModeDto> matchAssignMode(WorkDispatchServiceCorpDto workDispatchServiceCorpDto);

}
