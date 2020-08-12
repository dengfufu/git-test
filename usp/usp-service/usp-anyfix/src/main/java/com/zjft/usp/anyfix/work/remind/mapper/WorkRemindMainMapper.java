package com.zjft.usp.anyfix.work.remind.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.work.remind.dto.WorkRemindMainDto;
import com.zjft.usp.anyfix.work.remind.filter.WorkRemindFilter;
import com.zjft.usp.anyfix.work.remind.model.WorkRemindMain;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 工单预警主表 Mapper 接口
 * </p>
 *
 * @author jfzou
 * @since 2020-04-17
 */
public interface WorkRemindMainMapper extends BaseMapper<WorkRemindMain> {

    List<WorkRemindMainDto> queryWorkRemind(@Param("workRemindFilter") WorkRemindFilter workRemindFilter, Page page);
}
