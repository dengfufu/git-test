package com.zjft.usp.file.service;

import com.zjft.usp.common.model.Result;
import com.zjft.usp.file.dto.FileDto;
import com.zjft.usp.file.service.fallback.FileFeignServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 文件Feign接口
 *
 * @author chenxiaod
 * @version 1.0
 * @datetime 2019/9/9 10:29
 */
@FeignClient(name = "usp-file", fallbackFactory = FileFeignServiceFallbackFactory.class)
public interface FileFeignService {

    /**
     * description: 上传文件
     *
     * @param files MultipartFile文件
     * @return FileDto
     */
    @PostMapping(value = "/uploadFile")
    Result<FileDto> uploadFile(@RequestParam("file") MultipartFile files);

    /**
     * description: 上传图片
     *
     * @param files 图片
     * @return Result<List < FileDto>>
     */
    @PostMapping(value = "/uploadFaceImgBase64")
    Result<List<FileDto>> uploadFaceImgBase64(@RequestBody String files);

    /**
     * description: 删除单个文件
     *
     * @param fileId 文件id
     * @return Result
     */
    @PostMapping(value = "/delFile")
    Result delFile(@RequestParam("fileId") Long fileId);

    /**
     * description: 单个文件下载
     *
     * @param fileId   文件id
     * @param response 请求响应
     */
    @PostMapping(value = "downloadFile")
    void downloadFile(@RequestParam("fileId") Long fileId, HttpServletResponse response);

    /**
     * description: 显示文件
     *
     * @param fileId   文件id
     * @param response 请求响应
     */
    @GetMapping(value = "showFile")
    void showFile(@RequestParam("fileId") Object fileId, HttpServletResponse response);

    /**
     * description: 根据fileId查询文件
     *
     * @param fileId 文件id
     * @return FileDto
     */
    @PostMapping(value = "findFile")
    Result<FileDto> findFile(@RequestParam("fileId") Long fileId);

    /**
     * description: 根据fileId获取文件流
     *
     * @param fileId 文件id
     * @return ResponseEntity<byte [ ]>
     */
    @PostMapping(value = "/feign/getImgByteArrayByFileId")
    ResponseEntity<byte[]> getImgByteArrayByFileId(@RequestParam("fileId") Long fileId);

    /**
     * 根据id删除临时文件
     *
     * @param fileId 文件id
     * @return com.zjft.usp.common.model.Result
     * @date 2019/9/24
     */
    @PostMapping("/deleteFileTemporaryByID")
    Result deleteFileTemporaryByID(@RequestParam("fileId") Long fileId);

    /**
     * 批量删除临时文件
     *
     * @param fileIdList
     * @return
     */
    @PostMapping("/deleteFileTemporaryByFileIdList")
    Result deleteFileTemporaryByFileIdList(@RequestParam("fileIdList") List<Long> fileIdList);

    /**
     * 批量删除临时文件
     *
     * @param fileIdList
     * @return
     */
    @PostMapping("/feign/deleteFileList")
    Result deleteFileList(@RequestParam("fileIdList") List<Long> fileIdList);

    /**
     * 批量删除临时文件
     *
     * @param json
     * @return
     */
    @RequestMapping(value = "/feign/temp/delete",
            method = RequestMethod.POST, headers = {"content-type=application/json"})
    Result deleteTempFileByFileIdList(@RequestBody String json);

    /**
     * description: 上传图片
     *
     * @param base64Str
     * @return
     */
    @PostMapping("/feign/uploadBase64Img")
    Result<Long> uploadBase64Img(@RequestBody String base64Str);

    /**
     * 根据文件编号列表获得文件列表信息
     *
     * @param json
     * @return
     * @author zgpi
     * @date 2019/11/7 18:39
     **/
    @RequestMapping(value = "/feign/listByIdList",
            method = RequestMethod.POST, headers = {"content-type=application/json"})
    Result listFileDtoByIdList(@RequestBody String json);

    /**
     * 拷贝文件列表
     *
     * @param fileIdList
     * @return
     * @author zgpi
     * @date 2020/3/2 20:07
     */
    @RequestMapping(value = "/feign/copyFileList", method = RequestMethod.POST)
    Result<List<FileDto>> copyFileList(@RequestParam("fileIdList") List<Long> fileIdList);
}
