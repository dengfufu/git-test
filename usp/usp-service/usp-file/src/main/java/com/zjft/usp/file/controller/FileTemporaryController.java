package com.zjft.usp.file.controller;

import com.zjft.usp.common.model.Result;
import com.zjft.usp.file.service.FileTemporaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * description: FileTemporaryController
 * date: 2019/9/24 17:08
 * author: cxd
 * version: 1.0
 */
@RestController
public class FileTemporaryController {

    @Autowired
    FileTemporaryService fileTemporaryService;

    @PostMapping("/deleteFileTemporaryByID")
    public Result deleteFileTemporaryByID(@RequestParam("fileId") Long fileId) {
        fileTemporaryService.deleteFileTemporaryByID(fileId);
        return Result.succeed("删除成功");
    }

    @PostMapping("/deleteFileTemporary")
    public Result deleteFileTemporary() {
        fileTemporaryService.deleteFileTemporary();
        return Result.succeed("删除成功");
    }

    /***
     * 批量删除临时文件
     * @date 2020/2/16
     * @param fileIdList
     * @return void
     */
    @PostMapping("/deleteFileTemporaryByFileIdList")
    public Result deleteFileTemporaryByFileIdList(@RequestParam("fileIdList") List<Long> fileIdList) {
        fileTemporaryService.deleteFileTemporaryByFileIdList(fileIdList);
        return Result.succeed("删除成功");
    }

    /**
     * 远程调用：批量删除临时文件
     *
     * @param fileIdList
     * @return void
     * @date 2020/2/16
     */
    @PostMapping("/feign/temp/delete")
    public Result deleteTempFileByFileIdList(@RequestBody List<Long> fileIdList) {
        fileTemporaryService.deleteFileTemporaryByFileIdList(fileIdList);
        return Result.succeed();
    }
}
