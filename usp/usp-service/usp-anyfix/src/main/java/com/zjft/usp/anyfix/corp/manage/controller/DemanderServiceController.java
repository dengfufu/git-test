package com.zjft.usp.anyfix.corp.manage.controller;


import com.zjft.usp.anyfix.corp.manage.dto.DemanderServiceDto;
import com.zjft.usp.anyfix.corp.manage.filter.DemanderServiceFilter;
import com.zjft.usp.anyfix.corp.manage.model.DemanderService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderServiceService;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginClient;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.dto.CorpDto;
import com.zjft.usp.uas.dto.CorpRegistry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 服务商档案 前端控制器
 * </p>
 *
 * @author canlei
 * @since 2019-10-23
 */
@Api(tags = "服务商档案")
@RestController
@RequestMapping("/demander-service")
public class DemanderServiceController {

    @Autowired
    private DemanderServiceService demanderServiceService;

    @ApiOperation(value = "分页查询服务商管理")
    @PostMapping(value = "/query")
    public Result<ListWrapper<DemanderServiceDto>> query(@RequestBody DemanderServiceFilter demanderServiceFilter,
                                                         @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(demanderServiceFilter.getDemanderCorp())) {
            demanderServiceFilter.setDemanderCorp(reqParam.getCorpId());
        }
        ListWrapper<DemanderServiceDto> listWrapper = this.demanderServiceService.pageByFilter(demanderServiceFilter);
        return Result.succeed(listWrapper);
    }

    @ApiOperation(value = "添加服务商")
    @PostMapping(value = "/add")
    public Result add(@RequestBody DemanderServiceDto demanderServiceDto, @LoginUser UserInfo userInfo) {
        this.demanderServiceService.addDemanderService(demanderServiceDto, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation(value = "创建服务商")
    @PostMapping(value = "/create")
    public Result createDemander(@RequestBody CorpRegistry corpRegistry,
                                 @CommonReqParam ReqParam reqParam,
                                 @LoginUser UserInfo userInfo,
                                 @LoginClient String clientId) {
        corpRegistry.setTxId(reqParam.getTxId());
        corpRegistry.setClientId(clientId);
        demanderServiceService.createDemanderService(corpRegistry, reqParam, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation(value = "修改服务商")
    @PostMapping(value = "/update")
    public Result update(@RequestBody DemanderServiceDto demanderServiceDto, @LoginUser UserInfo userInfo) {
        this.demanderServiceService.updateDemanderService(demanderServiceDto, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation(value = "修改委托商")
    @PostMapping(value = "/demander/update")
    public Result demanderUpdate(@RequestBody DemanderServiceDto demanderServiceDto, @LoginUser UserInfo userInfo) {
        this.demanderServiceService.updateDemanderService(demanderServiceDto, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation(value = "删除服务商")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable("id") Long id) {
        Assert.notNull(id, "主键不能为空！");
        this.demanderServiceService.removeById(id);
        return Result.succeed("删除成功！");
    }

    @ApiOperation(value = "根据供应商查询服务商")
    @PostMapping(value = "/service/list")
    public Result<List<DemanderServiceDto>> listServiceByDemander(@RequestBody DemanderServiceFilter demanderServiceFilter,
                                                                  @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(demanderServiceFilter.getDemanderCorp())) {
            demanderServiceFilter.setDemanderCorp(reqParam.getCorpId());
        }
        return Result.succeed(this.demanderServiceService.listServiceByDemander(demanderServiceFilter));
    }

    @ApiOperation(value = "查询可供选择的服务商")
    @PostMapping(value = "/service/list/available")
    public Result<List<CorpDto>> listServiceAvailable(@RequestBody DemanderServiceFilter demanderServiceFilter,
                                                      @CommonReqParam ReqParam reqParam) {
        if (demanderServiceFilter.isForDemander()) {
            if (LongUtil.isZero(demanderServiceFilter.getDemanderCorp())) {
                demanderServiceFilter.setServiceCorp(reqParam.getCorpId());
            }
        } else {
            if (LongUtil.isZero(demanderServiceFilter.getDemanderCorp())) {
                demanderServiceFilter.setDemanderCorp(reqParam.getCorpId());
            }
        }
        return Result.succeed(this.demanderServiceService.listServiceAvailableByDemander(demanderServiceFilter));
    }

    @ApiOperation(value = "根据服务商查询供应商")
    @PostMapping(value = "/demander/list")
    public Result<List<DemanderServiceDto>> listDemanderByService(@RequestBody DemanderServiceFilter demanderServiceFilter,
                                                                  @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(demanderServiceFilter.getServiceCorp())) {
            demanderServiceFilter.setServiceCorp(reqParam.getCorpId());
        }
        return Result.succeed(this.demanderServiceService.listDemanderByService(demanderServiceFilter));
    }

    @ApiOperation(value = "根据服务商查询供应商")
    @PostMapping(value = "/demander/query")
    public Result<ListWrapper<DemanderServiceDto>> pageDemanderByService(@RequestBody DemanderServiceFilter demanderServiceFilter,
                                                                         @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(demanderServiceFilter.getServiceCorp())) {
            demanderServiceFilter.setServiceCorp(reqParam.getCorpId());
        }
        return Result.succeed(this.demanderServiceService.pageDemander(demanderServiceFilter));
    }

    @ApiOperation(value = "查询供应商企业详情")
    @GetMapping(value = "/demander/detail/{id}")
    public Result<DemanderServiceDto> detail(@PathVariable("id") Long id) {
        return Result.succeed(this.demanderServiceService.getDemanderDetailById(id));
    }

    @ApiOperation(value = "根据客户企业或供应商企业编号查询服务商列表")
    @GetMapping(value = "/service/list/{corpId}")
    public Result<List<DemanderServiceDto>> listServiceByCorpId(@PathVariable("corpId") Long corpId) {
        List<DemanderServiceDto> dtoList = this.demanderServiceService.listServiceByCorpId(corpId);
        return Result.succeed(dtoList);
    }

    @ApiOperation(value = "根据企业编号获取所有相关企业列表", notes = "不包含自己")
    @GetMapping(value = "/relates/{corpId}")
    public Result<List<CorpDto>> listRelatesCorp(@PathVariable("corpId") Long corpId,
                                                 @CommonReqParam ReqParam reqParam) {
        List<CorpDto> list = this.demanderServiceService.listRelatesCorp(corpId, reqParam.getCorpId());
        return Result.succeed(list);
    }

    @ApiOperation(value = "根据服务商模糊查询委托商")
    @PostMapping(value = "/demander/match")
    public Result<List<DemanderServiceDto>> matchDemander(@RequestBody DemanderServiceFilter demanderServiceFilter) {
        return Result.succeed(this.demanderServiceService.matchDemander(demanderServiceFilter));
    }

    @ApiOperation(value = "远程调用：根据企业编号获取所有相关企业编号")
    @GetMapping(value = "/feign/listAllRelatedCorpIds/{corpId}")
    public Result<List<Long>> listFeignAllRelatedCorpIds(@PathVariable("corpId") Long corpId) {
        List<Long> list = this.demanderServiceService.listAllRelatedCorpIds(corpId);
        return Result.succeed(list);
    }

    @ApiOperation(value = "远程调用：根据委托商编号获取服务商企业编号列表")
    @GetMapping(value = "/feign/listServiceCorp/{demanderCorp}")
    public Result<List<Long>> listServiceCorpByDemander(@PathVariable("demanderCorp") Long demanderCorp) {
        List<Long> list = this.demanderServiceService.listRelatedCorpIdsByDemander(demanderCorp);
        return Result.succeed(list);
    }

    @ApiOperation(value = "远程调用：根据该企业获取委托商企业编号列表")
    @GetMapping(value = "/feign/demanderCorpId/{corpId}")
    public Result<List<Long>> listDemanderCorpId(@PathVariable("corpId") Long corpId) {
        List<Long> list = this.demanderServiceService.listDemanderCorpId(corpId);
        return Result.succeed(list);
    }

    @ApiOperation(value = "委托协议配置")
    @PostMapping(value = "/cont/config")
    public Result<List<Long>> contConfig(@RequestBody DemanderServiceDto demanderServiceDto) {
        this.demanderServiceService.addContConfig(demanderServiceDto);
        return Result.succeed();
    }

    @ApiOperation(value = "通过编号获取委托协议配置")
    @GetMapping(value = "/cont/getConfig/{id}")
    public Result getContConfig(@PathVariable("id") Long id) {
        DemanderServiceDto demanderServiceDto = this.demanderServiceService.getContConfigById(id);
        return Result.succeed(demanderServiceDto);
    }

    @ApiOperation(value = "获取收费规则委托协议配置")
    @PostMapping(value = "/cont/fee/getConfig")
    public Result getContConfigByParams(@RequestBody DemanderService demanderService) {
        DemanderServiceDto demanderServiceDto = this.demanderServiceService.getContConfigByParams(demanderService);
        return Result.succeed(demanderServiceDto);
    }

    @ApiOperation(value = "获取服务标准委托协议配置")
    @PostMapping(value = "/cont/service/getConfig")
    public Result getServiceContConfigByParams(@RequestBody DemanderService demanderService) {
        DemanderServiceDto demanderServiceDto = this.demanderServiceService.getContConfigByParams(demanderService);
        return Result.succeed(demanderServiceDto);
    }

}
