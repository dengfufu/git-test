package com.zjft.usp.file.service.fallback;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.file.dto.FileDto;
import com.zjft.usp.file.service.FileFeignService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author : chenxiaod
 * @Date : 2019/9/9 10:19
 * @Desc : 文件服务Feign接口实现类
 * @Version 1.0.0
 */
@Slf4j
@Component
public class FileFeignServiceFallbackFactory implements FallbackFactory<FileFeignService> {
    @Override
    public FileFeignService create(Throwable cause) {
        return new FileFeignService() {
            @Override
            public Result<FileDto> uploadFile(@RequestParam("file") MultipartFile files) {
                log.error("上传文件失败");
                return null;
            }

            @Override
            public Result<List<FileDto>> uploadFaceImgBase64(@RequestBody String files) {
                log.error("上传图片失败");
                return null;
            }
            @Override
            public Result delFile(@RequestParam("fileId")Long fileId) {
                log.error("删除单个文件失败");
                return null;
            }
            @Override
            public void downloadFile(@RequestParam("fileId") Long fileId, HttpServletResponse response) {
                log.error("单个文件下载失败");
            }
            @Override
            public void showFile(@RequestParam("fileId") Object fileId, HttpServletResponse response) {
                log.error("显示文件失败");
            }
            @Override
            public Result<FileDto> findFile(@RequestParam("fileId") Long fileId) {
                log.error("查询文件失败");
                return null;
            }

            @Override
            public ResponseEntity<byte[]> getImgByteArrayByFileId(@RequestParam("fileId") Long fileId) {
                log.error("获取文件流失败");
                return null;
            }

            @Override
            public Result deleteFileTemporaryByID(@RequestParam("fileId")Long fileId){
                log.error("删除临时文件失败");
                return null;
            }

            @Override
            public Result deleteFileTemporaryByFileIdList(List<Long> fileIdList) {
                log.error("批量删除临时文件失败");
                return null;
            }

            /**
             * 批量删除临时文件
             *
             * @param json
             * @return
             */
            @Override
            public Result deleteTempFileByFileIdList(String json) {
                return null;
            }

            @Override
            public Result deleteFileList(List<Long> fileIdList) {
                return null;
            }

            @Override
            public Result<Long> uploadBase64Img(String base64Str) {
                return null;
            }

            /**
             * 根据文件编号列表获得文件列表信息
             *
             * @param json
             * @return
             * @author zgpi
             * @date 2019/11/7 18:39
             **/
            @Override
            public Result listFileDtoByIdList(String json) {
                return null;
            }

            /**
             * 拷贝文件列表
             *
             * @param fileIdList
             * @return
             * @author zgpi
             * @date 2020/3/2 20:07
             */
            @Override
            public Result<List<FileDto>> copyFileList(List<Long> fileIdList) {
                return null;
            }
        };
    }
}
