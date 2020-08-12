package com.zjft.usp.uas.corp.controller;

import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginClient;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.corp.dto.CorpDto;
import com.zjft.usp.uas.corp.dto.CorpRegistryDto;
import com.zjft.usp.uas.corp.dto.CorpRoleDto;
import com.zjft.usp.uas.corp.dto.StaffScopeDto;
import com.zjft.usp.uas.corp.filter.CorpRegistryFilter;
import com.zjft.usp.uas.corp.model.CorpRegistry;
import com.zjft.usp.uas.corp.service.CorpRegistryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 企业注册控制器
 *
 * @author canlei
 * @version 1.0
 * @date 2019-09-29 09:06
 **/
@Api(tags = "企业注册")
@RestController
@RequestMapping(value = "/corp-registry")
public class CorpRegistryController {

    @Autowired
    private CorpRegistryService corpRegistryService;

    @ApiOperation("注册企业")
    @PostMapping(value = "/add")
    public Result corpRegistry(@RequestBody CorpRegistryDto corpRegistryDto,
                               @CommonReqParam ReqParam reqParam,
                               @LoginUser UserInfo user,
                               @LoginClient String clientId) {
        corpRegistryDto.setTxId(reqParam.getTxId());
        corpRegistryDto.setClientId(clientId);
        Long corpId = corpRegistryService.corpRegistry(corpRegistryDto, reqParam, user.getUserId());
        return Result.succeed(corpId);
    }

    @ApiOperation("获取企业详细信息")
    @GetMapping(value = "/{corpId}")
    public Result<CorpDto> queryCorpInfo(@PathVariable("corpId") Long corpId,
                                         @LoginUser UserInfo userInfo) {
        return Result.succeed(corpRegistryService.queryCorpInfo(corpId, userInfo.getUserId()));
    }

    @ApiOperation("修改企业管理密码")
    @PostMapping(value = "/password/change")
    public Result<Object> changeCorpPassword(@RequestBody CorpRegistryDto corpRegistryDto,
                                             @CommonReqParam ReqParam reqParam,
                                             @LoginClient String clientId,
                                             @LoginUser UserInfo user) throws Exception {
        corpRegistryService.changePassword(corpRegistryDto, reqParam, user.getUserId(),clientId);
        return Result.succeed();
    }

    @ApiOperation("更改注册信息")
    @PostMapping(value = "/update")
    public Result<Object> modifyCorpReg(@RequestBody CorpRegistryDto corpRegistryDto,
                                        @CommonReqParam ReqParam reqParam,
                                        @LoginClient String clientId,
                                        @LoginUser UserInfo user) {
        corpRegistryDto.setMyCorp(false);
        corpRegistryService.updateCorpReg(corpRegistryDto, reqParam, user.getUserId(),clientId);
        return Result.succeed();
    }

    @ApiOperation("更改本人企业的注册信息，公共接口")
    @PostMapping(value = "/updateMy")
    public Result<Object> modifyMyCorpReg(@RequestBody CorpRegistryDto corpRegistryDto,
                                        @CommonReqParam ReqParam reqParam,
                                        @LoginClient String clientId,
                                        @LoginUser UserInfo user) {
        corpRegistryDto.setMyCorp(true);
        corpRegistryService.updateCorpReg(corpRegistryDto, reqParam, user.getUserId(),clientId);
        return Result.succeed();
    }

    @ApiOperation("根据条件查询企业列表")
    @PostMapping(value = "/query")
    public Result<ListWrapper<CorpDto>> listCorp(@RequestBody CorpRegistryFilter corpRegistryFilter,
                                                 @LoginUser UserInfo userInfo) {
        return Result.succeed(this.corpRegistryService.query(corpRegistryFilter, userInfo.getUserId()));
    }

    @ApiOperation("人员规模列表")
    @GetMapping(value = "/staff-size/list")
    public Result<List<StaffScopeDto>> listStaffQty() {
        List<StaffScopeDto> staffScopeDtos = corpRegistryService.listStaffScope();
        return Result.succeed(staffScopeDtos);
    }

    @ApiOperation("模糊查询企业")
    @PostMapping(value = "/match")
    public Result<List<CorpRegistry>> matchCorp(@RequestBody CorpRegistryFilter corpRegistryFilter) {
        return Result.succeed(corpRegistryService.matchCorp(corpRegistryFilter));
    }

    @ApiOperation("根据用户id查询企业及角色信息")
    @GetMapping(value = "/list/{userId}")
    public Result<List<CorpRoleDto>> listCorpRole(@PathVariable("userId") Long userId){
        return Result.succeed(corpRegistryService.listCorpRoleByUserId(userId));
    }

    @ApiOperation(value = "远程调用：添加企业基本信息")
    @PostMapping(value = "/feign/add")
    public Result<Long> addCorp(@RequestBody CorpRegistry corpRegistry) {
        Long corpId = corpRegistryService.addCorp(corpRegistry);
        return Result.succeed(corpId);
    }

    @ApiOperation(value = "远程调用：模糊查询企业")
    @PostMapping(value = "/feign/matchCorp")
    public  Result<List<CorpRegistry>> matchCorpFeign(@RequestBody CorpRegistryFilter corpRegistryFilter) {
        return Result.succeed(corpRegistryService.matchCorp(corpRegistryFilter));
    }

    @ApiOperation(value = "远程调用：根据多个ID查询企业信息")
    @PostMapping(value = "/feign/corpIds/list")
    public Result<List<CorpDto>> listCorpByIdList(@RequestBody List<Long> corpIdList) {
        return Result.succeed(corpRegistryService.listCorpByIdList(corpIdList));
    }

    @ApiOperation(value = "远程调用：根据企业ID获得企业信息")
    @GetMapping(value = "/feign/{corpId}")
    public Result<CorpDto> findCorpById(@PathVariable("corpId") Long corpId) {
        CorpDto corpDto = new CorpDto();
        CorpRegistry corpRegistry = corpRegistryService.getById(corpId);
        if (corpRegistry != null) {
            BeanUtils.copyProperties(corpRegistry, corpDto);
        }
        return Result.succeed(corpDto);
    }

    @ApiOperation(value = "远程调用：根据企业编号或者带有地址的企业信息")
    @GetMapping(value = "/feign/info/{corpId}")
    public Result<CorpDto> findCorpAddrInfoById(@PathVariable("corpId") Long corpId) {
        CorpDto corpDto = corpRegistryService.getCorpInfoWithAddress(corpId);
        return Result.succeed(corpDto);
    }


    @ApiOperation(value = "远程调用：根据企业ID获得企业信息")
    @GetMapping(value = "/feign/name/{corpName}")
    public Result<List<CorpRegistry>> findCorpById(@PathVariable("corpName") String corpName) {

        List<CorpRegistry> list= corpRegistryService.getCorpByName(corpName);
        return Result.succeed(list);
    }

    @ApiOperation(value = "远程调用：根据条件查询企业编号列表")
    @PostMapping(value = "/feign/listCorpIdByFilter")
    public Result<List<Long>> listCorpIdByFilter(@RequestBody CorpRegistryFilter corpRegistryFilter) {
        return Result.succeed(this.corpRegistryService.listCorpIdByFilter(corpRegistryFilter));
    }

    @ApiOperation(value = "远程调用：获得企业编号与名称映射")
    @GetMapping(value = "/feign/map")
    public Result<Map<Long, String>> mapIdAndName() {
        Map<Long, String> map = new HashMap<>();
        List<CorpRegistry> list = corpRegistryService.list();
        if (list != null) {
            map = list.stream().collect(Collectors.toMap(CorpRegistry::getCorpId, CorpRegistry::getCorpName));
        }
        return Result.succeed(map);
    }

    @ApiOperation(value = "远程调用：根据多个ID查询企业编号与名称映射")
    @PostMapping(value = "/feign/mapByCorpIdList")
    public Result<Map<Long, String>> mapIdAndNameByCorpIdList(@RequestBody List<Long> corpIdList) {
        return Result.succeed(corpRegistryService.mapCorpIdAndShortName(corpIdList));
    }

    @ApiOperation("远程调用：注册企业")
    @PostMapping(value = "/feign/register")
    public Result feignRegisterCorp(@RequestBody String json) {
        Long corpId =  this.corpRegistryService.feignCreateDemander(json);
        return Result.succeed(corpId);
    }

    @ApiOperation("远程调用：获取带有注册人信息的企业详情")
    @GetMapping(value = "/feign/getCorpDetail/{corpId}")
    public Result getCorpDetailWithRegUserNameById(@PathVariable("corpId") Long corpId) {
        return Result.succeed(corpRegistryService.getCorpDetailWithRegUserNameById(corpId));
    }


    @ApiOperation(value = "远程调用：自动认证")
    @GetMapping(value = "/feign/register/{corpId}")
    public Result<CorpDto> addVerifyByCorpId(@PathVariable("corpId") Long corpId) {
        corpRegistryService.addVerifyByCorpId(corpId);
        return Result.succeed();
    }

    @ApiOperation(value = "远程调用：获取企业系统编号与企业编号的映射")
    @PostMapping(value = "/feign/mapCorpIdAndCode")
    public Result<Map<Long, String>> mapCorpIdAndCode(@RequestBody List<Long> corpIdList) {
        return Result.succeed(corpRegistryService.mapCorpIdAndCode(corpIdList));
    }

}
