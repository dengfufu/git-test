package com.zjft.usp.file.composite;

import com.zjft.usp.file.dto.FileDto;

import java.util.List;

public interface FileCompoService {

    /**
     * 拷贝文件
     *
     * @param fileIdList
     * @return
     * @author zgpi
     * @date 2020/3/2 17:26
     */
    List<FileDto> copyFileList(List<Long> fileIdList);
}
