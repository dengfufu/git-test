package com.zjft.usp.anyfix.corp.manage.controller;


import com.zjft.usp.anyfix.corp.manage.dto.DemanderAutoConfigDto;
import com.zjft.usp.anyfix.corp.manage.service.DemanderAutoConfigService;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 委托商自动化配置表 前端控制器
 * </p>
 *
 * @author canlei
 * @since 2020-07-20
 */
@RestController
@RequestMapping("/demander-auto-config")
public class DemanderAutoConfigController {

    @Autowired
    private DemanderAutoConfigService demanderAutoConfigService;

    @ApiOperation(value = "配置")
    @PostMapping(value = "/config")
    public Result config(@RequestBody DemanderAutoConfigDto demanderAutoConfigDto,
                         @LoginUser UserInfo userInfo,
                         @CommonReqParam ReqParam reqParam) {
        demanderAutoConfigService.autoConfig(demanderAutoConfigDto, userInfo.getUserId(), reqParam.getCorpId());
        return Result.succeed();
    }

    @ApiOperation(value = "根据委托商与服务商查询")
    @GetMapping(value = "/{demanderCorp}/{serviceCorp}")
    public Result<DemanderAutoConfigDto> findDetail(@PathVariable("demanderCorp") Long demanderCorp,
                             @PathVariable("serviceCorp") Long serviceCorp) {
        return Result.succeed(this.demanderAutoConfigService.findByDemanderAndService(demanderCorp, serviceCorp));
    }

}
