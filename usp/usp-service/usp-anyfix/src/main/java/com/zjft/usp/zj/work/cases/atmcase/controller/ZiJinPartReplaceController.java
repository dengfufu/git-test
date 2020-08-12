package com.zjft.usp.zj.work.cases.atmcase.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.work.cases.atmcase.composite.ZiJinPartReplaceCompoService;
import com.zjft.usp.zj.work.cases.atmcase.dto.partreplace.*;
import com.zjft.usp.zj.work.cases.atmcase.filter.PriStockPartFilter;
import com.zjft.usp.zj.work.cases.atmcase.filter.VendorPartFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ATM机CASE备件更换 前端控制器
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-13 16:40
 **/
@Api(tags = "ATM机备件更换")
@RestController
@RequestMapping("/zj/atmcase-part")
public class ZiJinPartReplaceController {
    @Autowired
    private ZiJinPartReplaceCompoService partReplaceCompoService;

    @ApiOperation(value = "获得备件更换列表")
    @PostMapping("/listPartReplace")
    public Result<PartReplaceListDto> listPartReplace(@RequestParam("workCode") String workCode,
                                                      @LoginUser UserInfo userInfo,
                                                      @CommonReqParam ReqParam reqParam) {
        return Result.succeed(partReplaceCompoService.listPartReplace(workCode, userInfo, reqParam));
    }

    @ApiOperation(value = "进入添加备件更换页面")
    @PostMapping("/addPartReplace")
    public Result<PartReplaceAddPageDto> addPartReplace(@RequestParam("workCode") String workCode,
                                                        @LoginUser UserInfo userInfo,
                                                        @CommonReqParam ReqParam reqParam) {
        return Result.succeed(partReplaceCompoService.addPartReplace(workCode, userInfo, reqParam));
    }

    @ApiOperation(value = "添加备件更换提交")
    @PostMapping("/addPartReplaceSubmit")
    public Result<JSONObject> addPartReplaceSubmit(@RequestBody PartReplaceAddDto partReplaceAddDto,
                                                   @LoginUser UserInfo userInfo,
                                                   @CommonReqParam ReqParam reqParam) {
        return Result.succeed(partReplaceCompoService.addPartReplaceSubmit(partReplaceAddDto, userInfo, reqParam));
    }

    @ApiOperation(value = "检查备件更换是否存在")
    @PostMapping("/checkPartReplaceExist")
    public Result<JSONObject> checkPartReplaceExist(@RequestParam("partReplaceId") String partReplaceId,
                                                    @LoginUser UserInfo userInfo,
                                                    @CommonReqParam ReqParam reqParam) {
        return Result.succeed(partReplaceCompoService.checkPartReplaceExist(partReplaceId, userInfo, reqParam));
    }

    @ApiOperation(value = "进入修改备件更换页面")
    @PostMapping("/modPartReplace")
    public Result<PartReplaceModPageDto> modPartReplace(@RequestParam("partReplaceId") String partReplaceId,
                                                        @RequestParam("workCode") String workCode,
                                                        @LoginUser UserInfo userInfo,
                                                        @CommonReqParam ReqParam reqParam) {
        return Result.succeed(partReplaceCompoService.modPartReplace(partReplaceId, workCode, userInfo, reqParam));
    }

    @ApiOperation(value = "添加备件更换提交")
    @PostMapping("/modPartReplaceSubmit")
    public Result<JSONObject> modPartReplaceSubmit(@RequestBody PartReplaceModDto partReplaceModDto,
                                                   @LoginUser UserInfo userInfo,
                                                   @CommonReqParam ReqParam reqParam) {
        return Result.succeed(partReplaceCompoService.modPartReplaceSubmit(partReplaceModDto, userInfo, reqParam));
    }

    @ApiOperation(value = "删除备件更换提交")
    @PostMapping("/delPartReplace")
    public Result<JSONObject> delPartReplace(@RequestParam("id") String id,
                                             @LoginUser UserInfo userInfo,
                                             @CommonReqParam ReqParam reqParam) {
        return Result.succeed(partReplaceCompoService.delPartReplace(id, userInfo, reqParam));
    }

    @ApiOperation(value = "获得CASE的换上备件列表")
    @PostMapping("/listUpGhPart")
    public Result<List<PartReplaceDto>> listUpGhPart(@RequestParam("partCode") String partCode,
                                                     @RequestParam("workCode") String workCode,
                                                     @LoginUser UserInfo userInfo,
                                                     @CommonReqParam ReqParam reqParam) {
        return Result.succeed(partReplaceCompoService.listUpGhPart(partCode, workCode, userInfo, reqParam));
    }

    @ApiOperation(value = "获得个人备件列表")
    @PostMapping("/listPriStockPart")
    public Result<List<PriStockPartDto>> listPriStockPart(@RequestBody PriStockPartFilter priStockPartFilter,
                                                          @LoginUser UserInfo userInfo,
                                                          @CommonReqParam ReqParam reqParam) {
        return Result.succeed(partReplaceCompoService.listPriStockPart(priStockPartFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "是否厂商备件")
    @PostMapping("/isVendorPart")
    public Result<VendorPartDto> isVendorPart(@RequestBody VendorPartFilter vendorPartFilter,
                                              @LoginUser UserInfo userInfo,
                                              @CommonReqParam ReqParam reqParam) {
        return Result.succeed(partReplaceCompoService.isVendorPart(vendorPartFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "获得车牌号列表")
    @PostMapping("/listCarNo")
    public Result<List<String>> listCarNo(@RequestParam("serviceBranch") String serviceBranch,
                                          @LoginUser UserInfo userInfo,
                                          @CommonReqParam ReqParam reqParam) {
        return Result.succeed(partReplaceCompoService.listCarNo(serviceBranch, userInfo, reqParam));
    }

    @ApiOperation(value = "获得备件状态")
    @PostMapping("/findPartStatus")
    public Result<Integer> findPartStatus(@RequestParam("partCode") String partCode,
                                          @RequestParam("newPartId") String newPartId,
                                          @RequestParam("newBarCode") String newBarCode,
                                          @LoginUser UserInfo userInfo,
                                          @CommonReqParam ReqParam reqParam) {
        return Result.succeed(partReplaceCompoService.findPartStatus(partCode, newPartId, newBarCode, userInfo, reqParam));
    }

    @ApiOperation(value = "检查UR")
    @PostMapping("/checkUR")
    public Result<Integer> checkUR(@RequestParam("partCode") String partCode,
                                   @LoginUser UserInfo userInfo,
                                   @CommonReqParam ReqParam reqParam) {
        return Result.succeed(partReplaceCompoService.checkUR(partCode, userInfo, reqParam));
    }

    @ApiOperation(value = "检查换上的专用备件是否已经被使用")
    @PostMapping("/checkPartId")
    public Result<JSONObject> checkPartId(@RequestParam("workCode") String workCode,
                                          @RequestParam("partCode") String partCode,
                                          @RequestParam("newPartId") String newPartId,
                                          @LoginUser UserInfo userInfo,
                                          @CommonReqParam ReqParam reqParam) {
        return Result.succeed(partReplaceCompoService.checkPartId(workCode, partCode, newPartId,
                userInfo, reqParam));
    }

    @ApiOperation(value = "是否强制使用厂商备件")
    @PostMapping("/mandatoryUseVendorPart")
    public Result<String> mandatoryUseVendorPart(@RequestBody PartReplaceDto partReplaceDto,
                                                 @LoginUser UserInfo userInfo,
                                                 @CommonReqParam ReqParam reqParam) {
        return Result.succeedStr(partReplaceCompoService.mandatoryUseVendorPart(partReplaceDto, userInfo, reqParam));
    }
}
