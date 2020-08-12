package com.zjft.usp.anyfix.work.auto.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.work.auto.dto.WorkDispatchServiceCorpDto;
import com.zjft.usp.anyfix.work.auto.filter.WorkDispatchServiceCorpFilter;
import com.zjft.usp.anyfix.work.auto.model.WorkDispatchServiceCorp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ljzhu
 * @Package com.zjft.anyfix.work.mapper
 * @date 2019-09-27 08:52
 * @note 工单分配服务商表 work_dispatch_service_corp
 */
public interface WorkDispatchServiceCorpMapper extends BaseMapper<WorkDispatchServiceCorp> {

    /**
     * 分页查询自动分配服务商规则
     *
     * @param workDispatchServiceCorpFilter
     * @param page
     * @return
     * @author zgpi
     * @date 2019/11/13 19:28
     **/
    List<WorkDispatchServiceCorpDto> query(@Param("filter") WorkDispatchServiceCorpFilter workDispatchServiceCorpFilter,
                                           Page page);

    /**
     * 根据条件匹配服务商
     *
     * @param workDispatchServiceCorpDto
     * @return
     * @author zgpi
     * @date 2019/11/14 14:11
     **/
    List<WorkDispatchServiceCorpDto> matchServiceCorp(WorkDispatchServiceCorpDto workDispatchServiceCorpDto);
}
