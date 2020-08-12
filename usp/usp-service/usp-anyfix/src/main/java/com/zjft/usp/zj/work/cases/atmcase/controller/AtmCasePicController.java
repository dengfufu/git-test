package com.zjft.usp.zj.work.cases.atmcase.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.work.cases.atmcase.composite.AtmCasePicCompoService;
import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.CaseDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.CasePicDetailDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.CasePicDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.CasePicPageDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.file.FileInfoDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * ATM机CASE照片上传 前端控制器
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-04-06 16:58
 **/

@Api(tags = "ATM机CASE照片上传请求")
@RestController
@RequestMapping("/zj/atmcase-pic")
public class AtmCasePicController {
    @Autowired
    private AtmCasePicCompoService atmCasePicCompoService;

    @ApiOperation(value = "获取未审核可修改的CASE照片列表")
    @PostMapping("/listNotAuditCasePic")
    public Result<List<CaseDto>> listNotAuditCasePic(@LoginUser UserInfo userInfo,
                                                     @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.atmCasePicCompoService.listNotAuditCasePic(userInfo, reqParam));
    }


    @ApiOperation(value = "获取未审核可修改的CASE照片列表的数量")
    @PostMapping("/count/notAuditCasePic")
    public Result<Integer> countNotAuditCasePic(@LoginUser UserInfo userInfo,
                                           @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.atmCasePicCompoService.countNotAuditCasePic(userInfo, reqParam));
    }

    @ApiOperation(value = "查询CASE照片")
    @GetMapping("/listCasePhoto/{workCode}")
    public Result<CasePicPageDto> listCasePhoto(@PathVariable("workCode") String workCode,
                                                @LoginUser UserInfo userInfo,
                                                @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.atmCasePicCompoService.initCasePhoto(workCode, userInfo, reqParam));
    }

    @ApiOperation(value = "获取CASE的demo照片编号")
    @PostMapping("/listDemoPhotoId")
    public Result<Map<String, Object>> listDemoPhotoId(@RequestParam("workCode") String workCode,
                                                       @RequestParam("mainId") Long mainId,
                                                       @LoginUser UserInfo userInfo,
                                                       @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.atmCasePicCompoService.listDemoPhotoId(workCode, mainId, userInfo, reqParam));
    }

    @ApiOperation(value = "获取CASE照片编号")
    @GetMapping("/listCasePhotoId/{workCode}")
    public Result listCasePhotoId(@PathVariable("workCode") String workCode,
                                  @LoginUser UserInfo userInfo,
                                  @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.atmCasePicCompoService.listCasePhotoId(workCode, userInfo, reqParam));
    }

    @ApiModelProperty(value = "根据机器制造号获取case照片列表")
    @PostMapping("/listCasePhotoIdByMachineCode")
    public Result<List<CasePicDetailDto>> listCasePhotoIdByMachineCode(@RequestBody CasePicDetailDto casePicDetailDto,
                                               @LoginUser UserInfo userInfo,
                                               @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.atmCasePicCompoService.listCasePhotoIdByMachineCode(casePicDetailDto, userInfo, reqParam));
    }

    @ApiOperation(value = "上传CASE照片")
    @PostMapping("/uploadCasePic")
    public Result<FileInfoDto> uploadCasePic(@RequestParam("base64Img") String base64Img,
                                             @RequestParam("jsonData") String jsonData,
                                             @LoginUser UserInfo userInfo,
                                             @CommonReqParam ReqParam reqParam) throws Exception {
        this.atmCasePicCompoService.uploadCasePic(base64Img, jsonData, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "删除CASE照片")
    @PostMapping("/delCasePic/workCode/{workCode}")
    public Result delCasePic(@PathVariable("workCode") String workCode,
                             @RequestParam("fileId") Long fileId,
                             @LoginUser UserInfo userInfo,
                             @CommonReqParam ReqParam reqParam) {
        this.atmCasePicCompoService.delCasePic(workCode, fileId, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation("显示CASE照片")
    @GetMapping("/viewCasePic/corpId/{corpId}/userId/{userId}/fileId/{fileId}")
    public void viewPhoto(@PathVariable("corpId") Long corpId,
                          @PathVariable("userId") Long userId,
                          @PathVariable("fileId") Long fileId, HttpServletResponse response) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);

        ReqParam reqParam = new ReqParam();
        reqParam.setCorpId(corpId);
        atmCasePicCompoService.viewCasePhoto(fileId, userInfo, reqParam, response);
    }

    @ApiOperation("显示CASE的demo照片")
    @GetMapping("/viewDemoPhoto/corpId/{corpId}/userId/{userId}/fileId/{fileId}")
    public void viewDemoPhoto(@PathVariable("corpId") Long corpId,
                              @PathVariable("userId") Long userId,
                              @PathVariable("fileId") Long fileId, HttpServletResponse response) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);

        ReqParam reqParam = new ReqParam();
        reqParam.setCorpId(corpId);
        atmCasePicCompoService.viewDemoPhoto(fileId, userInfo, reqParam, response);
    }

    @ApiOperation("不上传照片提交")
    @PostMapping("/addNoPhotoReason")
    public Result addNoPhotoReason(@RequestBody CasePicDto casePicDto,
                                   @LoginUser UserInfo userInfo,
                                   @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.atmCasePicCompoService.addNoPhotoReason(casePicDto, userInfo, reqParam));
    }

    @ApiOperation("上传提交")
    @PostMapping("/uploadSubmit")
    public Result<JSONObject> uploadSubmit(@RequestBody CasePicDto casePicDto,
                                           @LoginUser UserInfo userInfo,
                                           @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.atmCasePicCompoService.uploadSubmit(casePicDto, userInfo, reqParam));
    }

}
