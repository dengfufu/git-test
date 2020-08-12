package com.zjft.usp.file.task.impl;

import com.zjft.usp.file.dto.FileDto;
import com.zjft.usp.file.service.FileInfoService;
import com.zjft.usp.file.task.AsyncTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

/**
 * 异步任务实现类
 *
 * @author zgpi
 * @date 2020/3/2 19:42
 */
@Slf4j
@Service
public class AsyncTaskServiceImpl implements AsyncTaskService {

    @Autowired
    private FileInfoService fileInfoService;

    /**
     * 拷贝文件
     *
     * @param fileId
     * @return
     * @author zgpi
     * @date 2020/3/2 19:45
     */
    @Async("taskExecutor")
    @Override
    public Future<FileDto> copyFile(Long fileId, CountDownLatch latch) {
        FileDto fileDto = null;
        try {
            fileDto = fileInfoService.copyFile(fileId);
        } catch (Exception e) {
            log.error("异步拷贝文件失败：{}", e);
        } finally {
            latch.countDown();
        }
        return new AsyncResult<>(fileDto);
    }
}
