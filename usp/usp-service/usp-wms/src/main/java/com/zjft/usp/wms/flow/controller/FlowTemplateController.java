package com.zjft.usp.wms.flow.controller;


import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.flow.composite.FlowTemplateCompoService;
import com.zjft.usp.wms.flow.dto.FlowTemplateCopyDto;
import com.zjft.usp.wms.flow.dto.FlowTemplateDto;
import com.zjft.usp.wms.flow.filter.FlowTemplateFilter;
import com.zjft.usp.wms.flow.model.FlowTemplate;
import com.zjft.usp.wms.flow.service.FlowTemplateService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 流程模板表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@RestController
@RequestMapping("/flow-template")
public class FlowTemplateController {

    @Autowired
    private FlowTemplateService flowTemplateService;

    @Autowired
    private FlowTemplateCompoService flowTemplateCompoService;

    @ApiOperation(value = "分页查询流程模板")
    @PostMapping(value = "/pageBy")
    public Result<ListWrapper<FlowTemplateDto>> pageBy(@RequestBody FlowTemplateFilter flowTemplateFilter) {
        return Result.succeed(flowTemplateService.pageBy(flowTemplateFilter));
    }

    @ApiOperation(value = "添加流程模板")
    @PostMapping(value = "/addFlowTemplate")
    public Result addFlowTemplate(@RequestBody FlowTemplateDto flowTemplateDto, @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        flowTemplateDto.setCorpId(reqParam.getCorpId());
        flowTemplateCompoService.addFlowTemplate(flowTemplateDto, userInfo);
        return Result.succeed();
    }

    @ApiOperation(value = "通过复制功能添加流程模板")
    @PostMapping(value = "/addByCopy")
    public Result addByCopy(@RequestBody FlowTemplateCopyDto flowTemplateCopyDto, @LoginUser UserInfo userInfo) {
        flowTemplateCompoService.addByCopy(flowTemplateCopyDto, userInfo);
        return Result.succeed();
    }

    @ApiOperation(value = "修改流程模板基本信息+节点信息")
    @PostMapping(value = "/modFlowTemplateNode")
    public Result modFlowTemplateNode(@RequestBody FlowTemplateDto flowTemplateDto, @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        flowTemplateDto.setCorpId(reqParam.getCorpId());
        flowTemplateCompoService.modFlowTemplateNode(flowTemplateDto, userInfo);
        return Result.succeed();
    }

    @ApiOperation(value = "修改流程模板基本信息")
    @PostMapping(value = "/modFlowTemplateBaseInfo")
    public Result modFlowTemplateBaseInfo(@RequestBody FlowTemplate flowTemplate, @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        flowTemplate.setCorpId(reqParam.getCorpId());
        flowTemplateCompoService.modFlowTemplateBaseInfo(flowTemplate, userInfo);
        return Result.succeed();
    }

    @ApiOperation(value = "删除流程模板")
    @DeleteMapping(value = "/deleteFlowTemplate/{id}")
    public Result deleteById(@PathVariable("id") Long id) {
        flowTemplateCompoService.delFlowTemplateBy(id);
        return Result.succeed();
    }

    @ApiOperation(value = "获得单个流程模板所有数据")
    @GetMapping(value = "/getFlowTemplate/{id}")
    public Result<FlowTemplateDto> getFlowTemplateById(@PathVariable("id") Long id) {
        return Result.succeed(flowTemplateCompoService.getFlowTemplateById(id));
    }

}
