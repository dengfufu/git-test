package com.zjft.usp.anyfix.corp.manage.controller;


import com.zjft.usp.anyfix.corp.manage.composite.DemanderServiceManagerCompoService;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderServiceDto;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderServiceManagerDto;
import com.zjft.usp.anyfix.corp.manage.filter.DemanderServiceManagerFilter;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 委托商与服务商客户经理关系表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2020-06-15
 */
@RestController
@RequestMapping("/demander-service-manager")
public class DemanderServiceManagerController {

    @Autowired
    private DemanderServiceManagerCompoService demanderServiceManagerCompoService;

    @ApiOperation(value = "获得客户经理")
    @PostMapping(value = "/findManagers")
    public Result<DemanderServiceManagerDto> findDemanderServiceManager(@RequestBody DemanderServiceManagerDto demanderServiceManagerDto) {
        return Result.succeed(demanderServiceManagerCompoService.findDemanderServiceManager(demanderServiceManagerDto));
    }

    @ApiOperation(value = "编辑客户经理")
    @PostMapping(value = "/edit")
    public Result editDemanderServiceManager(@RequestBody DemanderServiceManagerDto demanderServiceManagerDto) {
        demanderServiceManagerCompoService.editDemanderServiceManager(demanderServiceManagerDto);
        return Result.succeed();
    }

    @ApiOperation(value = "根据客户经理获取委托商列表")
    @PostMapping(value = "/listDemander")
    public Result<List<DemanderServiceDto>> listDemanderByManager(@RequestBody DemanderServiceManagerFilter filter,
                                                                  @LoginUser UserInfo userInfo,
                                                                  @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(filter.getManagerId())) {
            filter.setManagerId(userInfo.getUserId());
        }
        if (LongUtil.isZero(filter.getServiceCorp())) {
            filter.setServiceCorp(reqParam.getCorpId());
        }
        return Result.succeed(demanderServiceManagerCompoService.listDemanderByManager(filter.getServiceCorp(), filter.getManagerId()));
    }

    @ApiOperation(value = "客户经理对应的委托商编号列表")
    @GetMapping(value = "/feign/demander/list")
    public Result<List<Long>> listDemanderCorpByManager(@RequestParam("corpId") Long corpId,
                                                        @RequestParam("manager") Long manager) {
        return Result.succeed(demanderServiceManagerCompoService.listDemanderCorpByManager(corpId, manager));
    }

}
