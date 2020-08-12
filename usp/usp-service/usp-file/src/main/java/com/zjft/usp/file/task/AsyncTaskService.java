package com.zjft.usp.file.task;

import com.zjft.usp.file.dto.FileDto;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

/**
 * 异步任务实现类
 *
 * @author zgpi
 * @date 2020/3/2 19:42
 */
public interface AsyncTaskService {

    /**
     * 拷贝文件
     *
     * @param fileId
     * @param latch
     * @return
     * @author zgpi
     * @date 2020/3/2 19:55
     */
    Future<FileDto> copyFile(Long fileId, CountDownLatch latch);
}
