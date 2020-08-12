package com.zjft.usp.anyfix.corp.manage.controller;


import com.zjft.usp.anyfix.corp.manage.dto.DemanderCustomDetailDto;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderCustomDto;
import com.zjft.usp.anyfix.corp.manage.filter.DemanderCustomFilter;
import com.zjft.usp.anyfix.corp.manage.model.DemanderCustom;
import com.zjft.usp.anyfix.corp.manage.service.DemanderCustomService;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.dto.CorpDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务委托方与用户企业关系表 前端控制器
 * </p>
 *
 * @author canlei
 * @since 2019-10-23
 */
@Api(tags = "客户档案")
@RestController
@RequestMapping("/demander-custom")
public class DemanderCustomController {

    @Autowired
    private DemanderCustomService demanderCustomService;

    @ApiOperation(value = "根据供应商查询客户列表")
    @PostMapping(value = "/custom/list")
    public Result<List<DemanderCustomDto>> listCustomByDemander(@RequestBody DemanderCustomFilter demanderCustomFilter,
                                                                @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(demanderCustomFilter.getDemanderCorp())) {
            demanderCustomFilter.setDemanderCorp(reqParam.getCorpId());
        }
        return Result.succeed(this.demanderCustomService.listCustomByDemander(demanderCustomFilter));
    }

    @ApiOperation(value = "根据供应商获得客户下拉列表", notes = "包含当前企业")
    @PostMapping(value = "/custom/select")
    public Result<List<DemanderCustom>> selectCustomByDemander(@RequestBody DemanderCustomFilter demanderCustomFilter,
                                                               @CommonReqParam ReqParam reqParam) {
        Long demanderCorp = demanderCustomFilter.getDemanderCorp();
        if (LongUtil.isZero(demanderCorp)) {
            demanderCorp = reqParam.getCorpId();
        }
        return Result.succeed(this.demanderCustomService.selectCustomByDemander(demanderCorp, reqParam.getCorpId()));
    }

    @ApiOperation(value = "模糊匹配客户列表")
    @PostMapping(value = "/custom/match")
    public Result<List<CorpDto>> matchCustom(@RequestBody DemanderCustomFilter demanderCustomFilter,
                                   @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(demanderCustomFilter.getDemanderCorp())) {
            demanderCustomFilter.setDemanderCorp(reqParam.getCorpId());
        }
        return Result.succeed(this.demanderCustomService.listAvailableCorp(demanderCustomFilter));
    }

    @ApiOperation(value = "查看客户详情")
    @PostMapping(value = "/custom/detail")
    public Result<DemanderCustomDetailDto> detail(@RequestBody DemanderCustomFilter demanderCustomFilter) {
        return Result.succeed(demanderCustomService.getDetailDtoByIds(demanderCustomFilter.getCustomId()));
    }

    @ApiOperation(value = "根据客户查询供应商列表")
    @PostMapping(value = "/demander/list")
    public Result<List<DemanderCustomDto>> listDemanderByCustom(@RequestBody DemanderCustomFilter demanderCustomFilter,
                                                                @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(demanderCustomFilter.getCustomCorp())) {
            demanderCustomFilter.setCustomCorp(reqParam.getCorpId());
        }
        return Result.succeed(this.demanderCustomService.listDemanderByCustom(demanderCustomFilter));
    }

    @ApiOperation(value = "根据供应商或者服务商企业编号查询客户列表")
    @GetMapping(value = "/custom/list/{corpId}")
    public Result<List<DemanderCustom>> listCustomByCorpId(@PathVariable("corpId") Long corpId) {
        return Result.succeed(this.demanderCustomService.listCustomByCorpId(corpId));
    }

    @ApiOperation(value = "查询服务商或者委托商的客户列表")
    @PostMapping(value = "/branch/custom/match")
    public Result<List<DemanderCustomDto>> listCustomByCorp(@RequestBody DemanderCustomFilter demanderCustomFilter,
                                                         @CommonReqParam ReqParam reqParam) {
        if(LongUtil.isZero(demanderCustomFilter.getCorpId())) {
            demanderCustomFilter.setCorpId(reqParam.getCorpId());
        }
        return Result.succeed(this.demanderCustomService.matchCustomByCorpForBranch(demanderCustomFilter));
    }

    @ApiOperation(value = "分页查询客户企业")
    @PostMapping(value = "/query")
    public Result<ListWrapper<DemanderCustomDto>> query(@RequestBody DemanderCustomFilter demanderCustomFilter,
                                                        @CommonReqParam ReqParam reqParam) {
        ListWrapper<DemanderCustomDto> listWrapper = this.demanderCustomService.pageByFilter(demanderCustomFilter, reqParam);
        return Result.succeed(listWrapper);
    }

    @ApiOperation(value = "添加客户企业")
    @PostMapping(value = "/add")
    public Result add(@RequestBody DemanderCustomDto demanderCustomDto,
                      @LoginUser UserInfo userInfo) {
        this.demanderCustomService.addDemanderCustom(demanderCustomDto, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation(value = "更新客户企业")
    @PostMapping(value = "/update")
    public Result update(@RequestBody DemanderCustomDto demanderCustomDto,
                         @LoginUser UserInfo userInfo) {
        this.demanderCustomService.updateDemanderCustom(demanderCustomDto, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation(value = "删除客户企业")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable("id") Long id) {
        Assert.notNull(id, "主键不能为空！");
        this.demanderCustomService.removeById(id);
        return Result.succeed();
    }

    @ApiOperation(value = "远程调用：根据主键list获取主键与名称的映射")
    @PostMapping(value = "/feign/mapIdAndNameByIdList")
    public Result<Map<Long, String>> mapIdAndCustomNameByIdList(@RequestBody List<Long> customIdList) {
        return Result.succeed(this.demanderCustomService.mapIdAndCustomNameByIdList(customIdList));
    }

    @ApiOperation(value = "远程调用：根据主键获得详情")
    @GetMapping(value = "/feign/{customId}")
    public Result<DemanderCustomDto> findDtoByIdFeign(@PathVariable("customId") Long customId) {
        return Result.succeed(this.demanderCustomService.findDtoById(customId));
    }

    @ApiOperation(value = "远程调用：根据客户企业编号获取所有主键")
    @GetMapping(value = "/feign/listIdsByCustom/{customCorp}")
    public Result<List<Long>> listIdsByCustom(@PathVariable("customCorp") Long customCorp) {
        List<Long> list = this.demanderCustomService.listIdsByCustomCorp(customCorp);
        return Result.succeed(list);
    }

    @ApiOperation(value = "远程调用：根据委托商编号获取主键和客户名称的映射")
    @GetMapping(value = "/feign/mapCustomIdAndName/{demanderCorp}")
    public Result<Map<Long, String>> mapCustomIdAndNameByDemander(@PathVariable("demanderCorp") Long demanderCorp) {
        return Result.succeed(this.demanderCustomService.mapCustomIdAndNameByDemander(demanderCorp));
    }

    @ApiOperation(value = "远程调用：批量增加客户名称")
    @PostMapping(value = "/feign/customCorp/batchSave")
    public Result<Map<String, Map<String,Long>>> batchSaveCustomCorp(@RequestParam("customCorpNameList") List<String> customCropNameList,
                                                        @RequestParam("demanderCorp") Long demanderCorp ,
                                                        @RequestParam("userId") Long userId ) {
        return Result.succeed(this.demanderCustomService.batchAddDemanderCustom(customCropNameList,demanderCorp,userId));

    }
}
