package com.zjft.usp.wms.flow.controller;


import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.wms.flow.composite.FlowInstanceCompoService;
import com.zjft.usp.wms.flow.dto.FlowInstanceNodeDealResultDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 流程实例表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@RestController
@RequestMapping("/flow-instance")
public class FlowInstanceController {

    @Autowired
    private FlowInstanceCompoService flowInstanceCompoService;

    @ApiOperation(value = "测试结束流程接口")
    @PostMapping(value = "/endNode")
    public Result endNode(@RequestBody FlowInstanceNodeDealResultDto dealResultDto, @LoginUser UserInfo userInfo) {
        System.out.println("进入方法");
        flowInstanceCompoService.endCurrentNode(dealResultDto, userInfo);
        return Result.succeed();
    }
}
