package com.zjft.usp.file.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.zjft.usp.file.mapper.FileInfoMapper;
import com.zjft.usp.file.mapper.FileTemporaryMapper;
import com.zjft.usp.file.model.FileInfo;
import com.zjft.usp.file.model.FileTemporary;
import com.zjft.usp.file.service.FileTemporaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * description: FileTemporaryServiceImpl
 * date: 2019/9/24 17:03
 * author: cxd
 * version: 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class FileTemporaryServiceImpl extends ServiceImpl<FileTemporaryMapper, FileTemporary> implements FileTemporaryService {

    @Resource
    private FileTemporaryMapper fileTemporaryMapper;
    @Resource
    private FileInfoMapper uasFileMapper;
    @Autowired
    private FastFileStorageClient storageClient;
    /**
     * 根据id删除临时文件
     * @date 2019/9/24
     * @param fileId 文件id
     */
    @Override
    public void deleteFileTemporaryByID(Long fileId) {
        fileTemporaryMapper.deleteById(fileId);
    }

    /***
     * 批量删除临时文件
     * @date 2020/2/16
     * @param fileIdList
     * @return void
     */
    @Override
    public void deleteFileTemporaryByFileIdList(List<Long> fileIdList) {
        if(fileIdList != null && fileIdList.size()>0){
            fileTemporaryMapper.deleteBatchIds(fileIdList);
        }
    }

    /**
     * 临时文件删除
     * @date 2019/9/24
     */
    @Override
    public void deleteFileTemporary() {
        //查询所有符合条件的临时文件id
        List<FileInfo> fileList = fileTemporaryMapper.selectFileByTime();
        List<Long> fileIdList = null;
        for (FileInfo file:fileList) {
            fileIdList.add(file.getFileId());
        }
        //第一步，删除临时文件
        fileTemporaryMapper.deleteByTime();

        if(fileIdList != null && fileIdList.size()>0){
            //第二步，删除数据库上传文件信息
            uasFileMapper.deleteBatchIds(fileIdList);
            //第三步，删除服务器端文件
            for (FileInfo uasFile:fileList) {
                try {
                    this.storageClient.deleteFile(uasFile.getPath());
                } catch (Exception e) {
                    log.error("要删除的临时文件失败。ID：" + uasFile.getFileId() + " 路径：" + uasFile.getPath() + " 错误信息：" + e.getMessage());
                }
            }
        }
    }
}
