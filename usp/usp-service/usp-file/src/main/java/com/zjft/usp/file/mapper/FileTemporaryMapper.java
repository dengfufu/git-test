package com.zjft.usp.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.file.model.FileInfo;
import com.zjft.usp.file.model.FileTemporary;

import java.util.List;

/**
 * description: FileTemporaryMapper
 * date: 2019/9/24 15:02
 * author: cxd
 * version: 1.0
 */
public interface FileTemporaryMapper extends BaseMapper<FileTemporary> {

    /**
     * 删除超时的文件
     * @date 2019/9/24
     */
    void deleteByTime();

    /***
     * 查询符合条件的所有文件
     * @date 2020/2/16
     * @param
     * @return java.util.List<com.zjft.usp.file.model.FileInfo>
     */
    List<FileInfo> selectFileByTime();
}
