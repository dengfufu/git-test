package com.zjft.usp.wms.flow.controller;


import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.flow.dto.FlowInstanceTraceDto;
import com.zjft.usp.wms.flow.service.FlowInstanceTraceService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 流程实例处理过程表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-11
 */
@RestController
@RequestMapping("/flow-instance-trace")
public class FlowInstanceTraceController {

    @Autowired
    private FlowInstanceTraceService flowInstanceTraceService;

    @ApiOperation(value = "查询流程处理记录")
    @PostMapping(value = "/listTraceBy/{flowInstanceId}")
    public Result<List<FlowInstanceTraceDto>> listTraceBy(@PathVariable("flowInstanceId") Long flowInstanceId, @CommonReqParam ReqParam reqParam) {
        return Result.succeed(flowInstanceTraceService.listSortBy(flowInstanceId, reqParam));
    }
}
