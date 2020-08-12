package com.zjft.usp.wms.flow.factory;

import cn.hutool.core.date.DateUtil;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.wms.flow.dto.FlowInstanceNodeDealResultDto;
import com.zjft.usp.wms.flow.model.FlowInstanceNode;
import com.zjft.usp.wms.flow.model.FlowInstanceTrace;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 集中封装，方便维护
 * @Author: JFZOU
 * @Date: 2019-11-18 16:37
 */
@Service
public class FlowInstanceTraceFactory {

    public static FlowInstanceTrace getFlowInstanceTrace(
            FlowInstanceNode flowInstanceNode,
            FlowInstanceNodeDealResultDto dealResultDto,
            UserInfo userInfo) {

        FlowInstanceTrace newFlowInstanceTrace = new FlowInstanceTrace();
        newFlowInstanceTrace.setId(KeyUtil.getId());
        newFlowInstanceTrace.setFlowInstanceId(flowInstanceNode.getFlowInstanceId());
        newFlowInstanceTrace.setCurrentNodeId(flowInstanceNode.getId());

        if (!StringUtils.isEmpty(dealResultDto.getNodeEndTypeName())) {
            newFlowInstanceTrace.setDealResultName(dealResultDto.getNodeEndTypeName());
        } else {
            newFlowInstanceTrace.setDealResultName(dealResultDto.getCountersignPassedName());
        }
        newFlowInstanceTrace.setCompletedDescription(dealResultDto.getDoDescription());
        newFlowInstanceTrace.setCompletedBy(userInfo.getUserId());
        newFlowInstanceTrace.setCompletedTime(DateUtil.date().toTimestamp());
        return newFlowInstanceTrace;
    }

}
