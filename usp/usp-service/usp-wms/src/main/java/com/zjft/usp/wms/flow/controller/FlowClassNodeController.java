package com.zjft.usp.wms.flow.controller;


import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.flow.dto.FlowClassNodeDto;
import com.zjft.usp.wms.flow.service.FlowClassNodeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 流程类型节点表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-08
 */
@RestController
@RequestMapping("/flow-class-node")
public class FlowClassNodeController {

    @Autowired
    private FlowClassNodeService flowClassNodeService;

    @ApiOperation(value = "查询流程节点默认配置，用于添加流程模板时配置默认节点列表，默认加载corpId=0的系统配置节点")
    @PostMapping(value = "/listForAddTemplateBy")
    public Result<List<FlowClassNodeDto>> listBy(@RequestBody FlowClassNodeDto flowClassNodeDto, @CommonReqParam ReqParam reqParam) {
        flowClassNodeDto.setCorpId(0L);
        return Result.succeed(flowClassNodeService.listBy(flowClassNodeDto));
    }
}
