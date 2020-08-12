package com.zjft.usp.zj.work.cases.atmcase.controller;

import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.work.cases.atmcase.composite.AtmCaseCompoService;
import com.zjft.usp.zj.work.cases.atmcase.dto.file.FileInfoDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 新增WO平台文件控制器，集中管理ATM CASE相关的图片等。
 * 老平台不同类型图片或者文件可能放在不同的文件业务表，因此方法应写明是哪一类业务
 * 如果是公共的则重新定义公共方法及在ANYFIX.YML中配置公共方法请求路径
 *
 * @Author: JFZOU
 * @Date: 2020-03-24 14:59
 * @Version 1.0
 */
@RestController
@RequestMapping("/zj/file")
public class AtmCaseFileController {

    @Autowired
    private AtmCaseCompoService atmCaseCompoService;

    /**
     * description: 上传人脸图片
     *
     * @param file     图片对象
     * @param jsonData JSON格式业务数据
     * @param userInfo 当前用户对象
     * @param reqParam 当前用户公共参数
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/upload/uploadFaceImg")
    @ResponseBody
    public Result<FileInfoDto> uploadFaceImg(@RequestParam("file") MultipartFile file,
                                             @RequestParam("jsonData") String jsonData,
                                             @LoginUser UserInfo userInfo,
                                             @CommonReqParam ReqParam reqParam) throws Exception {
        return Result.succeed(atmCaseCompoService.uploadFaceImg(file, jsonData, userInfo, reqParam));
    }

    /**
     * description: 上传人脸图片
     *
     * @param jsonData JSON格式业务数据
     * @param userInfo 当前用户对象
     * @param reqParam 当前用户公共参数
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/upload/uploadFaceImgBase64")
    @ResponseBody
    public Result<FileInfoDto> uploadFaceImgBase64(
            @RequestParam("jsonData") String jsonData,
            @RequestParam("base64Img") String base64Img,
            @LoginUser UserInfo userInfo,
            @CommonReqParam ReqParam reqParam) throws Exception {
        return Result.succeed(atmCaseCompoService.uploadFaceImgBase64(jsonData, base64Img, userInfo, reqParam));
    }

    /**
     * 删除单个文件
     *
     * @param fileId
     * @param userInfo
     * @param reqParam
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/delOmeCaseImg")
    @ResponseBody
    public Result delOmeCaseImg(@RequestParam("fileId") Long fileId,
                                @LoginUser UserInfo userInfo,
                                @CommonReqParam ReqParam reqParam) throws Exception {
        atmCaseCompoService.delOmeCaseImg(fileId, userInfo, reqParam);
        return Result.succeed("删除成功");
    }

    /**
     * 显示图片(建议仅限制于前端file.url这一类方式使用，同时需要在网关中配置不需要TOKEN认证)
     *
     * @param corpId
     * @param userId
     * @param fileId
     * @param response
     */
    @ApiOperation(value = "在线显示CASE图片")
    @GetMapping("/showOmeCaseImg/corpId/{corpId}/userId/{userId}/fileId/{fileId}")
    public void showOmeCaseImg(@PathVariable("corpId") Long corpId,
                               @PathVariable("userId") Long userId,
                               @PathVariable("fileId") Long fileId, HttpServletResponse response) {

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);

        ReqParam reqParam = new ReqParam();
        reqParam.setCorpId(corpId);
        atmCaseCompoService.showOmeCaseImg(fileId, userInfo, reqParam, response);
    }

    @ApiOperation("上传设备照片")
    @PostMapping("/uploadPhoto")
    public Result<FileInfoDto> uploadPhoto(@RequestParam("base64Img") String base64Img,
                                           @RequestParam("jsonData") String jsonData,
                                           @LoginUser UserInfo userInfo,
                                           @CommonReqParam ReqParam reqParam) throws Exception {
        return Result.succeed(this.atmCaseCompoService.uploadPhoto(base64Img, jsonData, userInfo, reqParam));
    }

    @ApiOperation("删除设备照片")
    @PostMapping("/delPhoto")
    public Result delPhoto(@RequestParam("fileId") Long fileId,
                           @LoginUser UserInfo userInfo,
                           @CommonReqParam ReqParam reqParam) {
        this.atmCaseCompoService.delPhoto(fileId, userInfo, reqParam);
        return Result.succeed("删除成功!");
    }

    @ApiOperation("显示设备照片")
    @GetMapping("/viewPhoto/corpId/{corpId}/userId/{userId}/fileId/{fileId}")
    public void viewPhoto(@PathVariable("corpId") Long corpId,
                          @PathVariable("userId") Long userId,
                          @PathVariable("fileId") Long fileId, HttpServletResponse response) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);

        ReqParam reqParam = new ReqParam();
        reqParam.setCorpId(corpId);
        atmCaseCompoService.viewPhoto(fileId, userInfo, reqParam, response);
    }

    @ApiOperation(value = "上传单据照片")
    @PostMapping("/uploadReceiptPhoto")
    public Result<FileInfoDto> uploadReceiptPhoto(@RequestParam("base64Img") String base64Img,
                                                  @RequestParam("jsonData") String jsonData,
                                                  @LoginUser UserInfo userInfo,
                                                  @CommonReqParam ReqParam reqParam) throws Exception {
        return Result.succeed(this.atmCaseCompoService.uploadReceiptPhoto(base64Img, jsonData, userInfo, reqParam));
    }

    @ApiOperation("删除单据照片")
    @PostMapping("/delReceiptPhoto")
    public Result delReceiptPhoto(@RequestParam("fileId") Long fileId,
                           @LoginUser UserInfo userInfo,
                           @CommonReqParam ReqParam reqParam) {
        this.atmCaseCompoService.delReceiptPhoto(fileId, userInfo, reqParam);
        return Result.succeed("删除成功!");
    }

}
