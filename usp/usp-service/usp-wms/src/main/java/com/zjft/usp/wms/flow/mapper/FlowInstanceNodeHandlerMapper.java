package com.zjft.usp.wms.flow.mapper;

import com.zjft.usp.wms.flow.dto.FlowInstanceNodeHandlerDto;
import com.zjft.usp.wms.flow.model.FlowInstanceNodeHandler;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 流程实例节点会签表 Mapper 接口
 * </p>
 *
 * @author jfzou
 * @since 2019-11-18
 */
public interface FlowInstanceNodeHandlerMapper extends BaseMapper<FlowInstanceNodeHandler> {

    /**
     * 根据实例编号list查询当前处理人list
     *
     * @author canlei
     * @param flowInstanceIdList
     * @return
     */
    List<FlowInstanceNodeHandlerDto> listCurAssignedTo(@Param("flowInstanceIdList") List<Long> flowInstanceIdList);

}
