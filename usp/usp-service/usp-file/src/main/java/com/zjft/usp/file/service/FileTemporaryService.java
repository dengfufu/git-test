package com.zjft.usp.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.file.model.FileTemporary;

import java.util.List;

/**
 * description: FileTemporaryService
 * date: 2019/9/24 17:01
 * author: cxd
 * version: 1.0
 */
public interface FileTemporaryService extends IService<FileTemporary> {

    /**
     * 根据id删除临时文件
     * @date 2019/9/24
     */
    void deleteFileTemporaryByID(Long fileId);

    /**
     * 临时文件删除
     * @date 2019/9/24
     */
    void deleteFileTemporary();

    /***
     * 批量删除临时文件
     * @date 2020/2/16
     * @param fileIdList
     * @return void
     */
    void deleteFileTemporaryByFileIdList(List<Long> fileIdList);
}
