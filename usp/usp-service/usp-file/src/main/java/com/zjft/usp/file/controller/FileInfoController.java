package com.zjft.usp.file.controller;

import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.file.composite.FileCompoService;
import com.zjft.usp.file.dto.FileDto;
import com.zjft.usp.file.service.FileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author chenxiaod
 * @description: 文件操作的Controller层
 * @date 2019/8/9 16:22
 */
@Controller
public class FileInfoController {

    @Autowired
    private FileInfoService fileService;
    @Autowired
    private FileCompoService fileCompoService;

    @GetMapping
    public String index() {
        return "index";
    }

    /**
     * description: 上传文件
     *
     * @param file MultipartFile文件
     * @return FileDto
     */
    @PostMapping(value = "/uploadFile")
    @ResponseBody
    public Result<FileDto> uploadFile(@RequestParam("file") MultipartFile file,
                                      @LoginUser UserInfo userInfo) throws Exception {
        return Result.succeed(fileService.uploadFile(file, userInfo, true));
    }

    /**
     * 上传图片并且保存缩略图
     *
     * @param file MultipartFile文件
     * @return List<FileDto>
     */
    @PostMapping(value = "/uploadImg")
    @ResponseBody
    public Result<List<FileDto>> uploadImg(@RequestParam("file") MultipartFile file,
                                           @RequestParam("thumbWidth") Integer tw, @RequestParam("thumbHeight") Integer th,
                                           @LoginUser UserInfo userInfo) throws Exception {
        return Result.succeed(fileService.uploadImgFile(file, userInfo, (tw == null ? 0 : tw), (th == null ? 0 : th)));
    }

    /**
     * 上传图片并且保存缩略图 Base64
     *
     * @param imgBase64 图片文件Base64
     * @return List<FileDto>
     */
    @PostMapping(value = "/uploadImgBase64")
    @ResponseBody
    public Result<List<FileDto>> uploadImgBase64(@RequestParam("imgBase64") String imgBase64,
                                           @RequestParam("thumbWidth") Integer tw, @RequestParam("thumbHeight") Integer th,
                                           @LoginUser UserInfo userInfo) throws Exception {
        return Result.succeed(fileService.uploadImgBase64(imgBase64, userInfo, (tw == null ? 0 : tw), (th == null ? 0 : th)));
    }

    /**
     * description: 上传头像图片
     *
     * @param file MultipartFile文件
     * @return List<FileDto>
     */
    @PostMapping(value = "/uploadFaceImg")
    @ResponseBody
    public Result<List<FileDto>> uploadFaceImg(@RequestParam("file") MultipartFile file,
                                               @LoginUser UserInfo userInfo) throws Exception {
        return Result.succeed(fileService.uploadFaceImgFile(file, userInfo));
    }

    /**
     * description: 上传头像图片
     *
     * @param base64Str 图片base64
     * @return List<FileDto>
     */
    @PostMapping(value = "/uploadFaceImgBase64")
    @ResponseBody
    public Result<List<FileDto>> uploadFaceImgBase64(@RequestBody String base64Str,
                                                     @LoginUser UserInfo userInfo) throws Exception {
        return Result.succeed(fileService.uploadFaceImgBase64(base64Str, userInfo));
    }


    /**
     * description: 删除单个文件
     *
     * @param fileId 文件id
     * @return Result
     */
    @PostMapping(value = "/delFile")
    @ResponseBody
    public Result delFile(@RequestParam("fileId") Long fileId) throws Exception {
        fileService.deleteFile(fileId);
        return Result.succeed("删除成功");
    }

    /**
     * description: 单个文件下载
     *
     * @param fileId   文件id
     * @param response 请求响应
     */
    @PostMapping(value = "downloadFile")
    @ResponseBody
    public void downloadFile(@RequestParam("fileId") Long fileId, HttpServletResponse response) throws IOException {
        fileService.downloadFile(fileId, response);
    }

    /**
     * description: 显示文件
     *
     * @param fileId   文件id
     * @param response 请求响应
     */
    @GetMapping(value = "showFile")
    @ResponseBody
    public void showFile(@RequestParam("fileId") Long fileId, HttpServletResponse response) throws IOException {
        fileService.showFile(fileId, response);
    }

    /**
     * description: 根据fileId查询文件
     *
     * @param fileId 文件id
     * @return FileDto
     */
    @PostMapping(value = "findFile")
    @ResponseBody
    public Result<FileDto> findFile(@RequestParam("fileId") Long fileId) throws IOException {
        return Result.succeed(fileService.findFile(fileId));
    }

    /**
     * 拷贝文件
     *
     * @param fileId 文件编号
     * @return
     * @author zgpi
     * @date 2020/3/2 17:25
     */
    @PostMapping(value = "/copyFile")
    @ResponseBody
    public Result<FileDto> copyFile(@RequestParam("fileId") Long fileId) {
        return Result.succeed(fileService.copyFile(fileId));
    }

    /**
     * 远程调用：拷贝文件列表
     *
     * @param fileIdList 文件编号列表
     * @return
     * @author zgpi
     * @date 2020/3/2 17:25
     */
    @PostMapping(value = "/feign/copyFileList")
    @ResponseBody
    public Result<List<FileDto>> copyFileList(@RequestParam("fileIdList") List<Long> fileIdList) {
        return Result.succeed(fileCompoService.copyFileList(fileIdList));
    }

    /**
     * description: 根据fileId获取文件字节数组 /feign/
     *
     * @param fileId 文件id
     * @return ResponseEntity<byte [ ]>
     */
    @PostMapping(value = "/feign/getImgByteArrayByFileId")
    @ResponseBody
    public ResponseEntity<byte[]> getImgByteArrayByFileId(@RequestParam("fileId") Long fileId) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = fileService.findFileOutputStreamByFileId(fileId);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE);
        if (byteArrayOutputStream == null) {
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(new byte[0]);
        }
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(byteArrayOutputStream.toByteArray());
    }

    /**
     * 上传BAE64格式图片
     *
     * @param base64Str
     * @return
     */
    @PostMapping("/feign/uploadBase64Img")
    @ResponseBody
    public Result<Long> uploadBase64Img(@RequestBody String base64Str) {
        Long fileId = fileService.uploadBase64Img(base64Str);
        return Result.succeed(fileId);
    }

    @PostMapping(value = "/feign/listByIdList")
    @ResponseBody
    public Result<List<FileDto>> listFileDtoByIdList(@RequestBody List<Long> fileIdList) {
        return Result.succeed(fileService.listFileDtoByIdList(fileIdList));
    }

    @PostMapping(value = "/feign/deleteFileList")
    @ResponseBody
    public Result<List<FileDto>> feignDeleteFileList(@RequestParam("fileIdList") List<Long> fileIdList) {
        fileService.deleteFileList(fileIdList);
        return Result.succeed("删除成功");
    }
}
