package com.zjft.usp.anyfix.baseinfo.controller;


import com.zjft.usp.anyfix.baseinfo.model.ServiceEvaluateTag;
import com.zjft.usp.anyfix.baseinfo.service.ServiceEvaluateTagService;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 服务评价标签表 前端控制器
 * </p>
 *
 * @author zphu
 * @since 2019-09-24
 */
@Api(tags = "服务评价")
@RestController
@RequestMapping("/service-evaluate-tag")
public class ServiceEvaluateTagController {

    @Autowired
    private ServiceEvaluateTagService serviceEvaluateTagService;

    @ApiOperation(value = "获得评价标签列表")
    @PostMapping(value = "/list")
    public Result<List<ServiceEvaluateTag>> listServiceEvaluateTag(@RequestBody ServiceEvaluateTag serviceEvaluateTag,
                                                                   @CommonReqParam ReqParam reqParam) {
        Long corpId = reqParam.getCorpId();
        if(LongUtil.isNotZero(serviceEvaluateTag.getServiceCorp())){
            corpId = serviceEvaluateTag.getServiceCorp();
        }
        return Result.succeed(serviceEvaluateTagService.listServiceEvaluateTag(corpId));
    }

}
