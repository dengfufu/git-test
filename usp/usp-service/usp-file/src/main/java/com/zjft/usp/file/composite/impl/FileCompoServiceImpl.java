package com.zjft.usp.file.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.zjft.usp.file.composite.FileCompoService;
import com.zjft.usp.file.dto.FileDto;
import com.zjft.usp.file.task.AsyncTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class FileCompoServiceImpl implements FileCompoService {

    @Autowired
    private AsyncTaskService asyncTaskService;

    /**
     * 拷贝文件
     *
     * @param fileIdList
     * @return
     * @author zgpi
     * @date 2020/3/2 17:26
     */
    @Override
    public List<FileDto> copyFileList(List<Long> fileIdList) {
        if (CollectionUtil.isEmpty(fileIdList)) {
            return null;
        }
        List<Future<FileDto>> futureList = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(fileIdList.size());
        for (Long fileId : fileIdList) {
            futureList.add(asyncTaskService.copyFile(fileId, latch));
        }
        List<FileDto> fileDtoList = new ArrayList<>();
        try {
            latch.await(10, TimeUnit.SECONDS);
            for (Future<FileDto> fileDtoFuture : futureList) {
                fileDtoList.add(fileDtoFuture.get(10, TimeUnit.SECONDS));
            }
        } catch (Exception e) {
            log.error("异步拷贝文件列表失败：{}", e);
        }
        return fileDtoList;
    }
}
