package com.zjft.usp.wms.flow.controller;


import com.zjft.usp.common.model.Result;
import com.zjft.usp.wms.flow.dto.FlowTemplateNodeHandlerDto;
import com.zjft.usp.wms.flow.service.FlowTemplateNodeHandlerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 流程模板节点处理人表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-18
 */
@RestController
@RequestMapping("/flow-template-node-handler")
public class FlowTemplateNodeHandlerController {

    @Autowired
    private FlowTemplateNodeHandlerService flowTemplateNodeHandlerService;

    @ApiOperation(value = "修改流程模板节点处理人(dto中需要前端传入流程模板ID，流程模板节点ID，处理人列表)")
    @PostMapping(value = "/modNodeHandlers")
    public Result modNodeHandlers(@RequestBody FlowTemplateNodeHandlerDto handlerDto) {
        flowTemplateNodeHandlerService.modHandlers(handlerDto);
        return Result.succeed();
    }
}
