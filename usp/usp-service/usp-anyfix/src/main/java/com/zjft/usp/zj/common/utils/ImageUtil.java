package com.zjft.usp.zj.common.utils;

import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.file.dto.FileDto;
import com.zjft.usp.file.service.FileFeignService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: JFZOU
 * @Date: 2020-03-20 17:34
 * @Version 1.0
 */
@Slf4j
@Component
public class ImageUtil {

    @Resource
    private FileFeignService fileFeignService;

    public FileDto findFile(Long fileId){
        FileDto fileDto = new FileDto();
        Result<FileDto> fileDtoResult =  this.fileFeignService.findFile(fileId);
        if(fileDtoResult != null && Result.SUCCESS == fileDtoResult.getCode().intValue()){
            fileDto = fileDtoResult.getData();
        }
        return fileDto;
    }

    public List<String> getBase64Images(String fileIds) {
        List<String> imageIds = Arrays.asList(fileIds.split(","));
        List<Long> fileIdList = imageIds.stream().map(fileId -> Long.parseLong(fileId)).collect(Collectors.toList());
        return getBase64Images(fileIdList);
    }

    public List<String> getBase64Images(List<Long> listFileIds) {
        List<String> list = new ArrayList<>();
        for (Long fileId : listFileIds) {
            list.add(getBase64Image(fileId));
        }
        return list;
    }

    public String getBase64Image(Long fileId) {
        String imageBase64 = "";
        ResponseEntity<byte[]> bytes = this.fileFeignService.getImgByteArrayByFileId(fileId);
        byte[] imgIbytes = bytes.getBody();
        if (imgIbytes != null && imgIbytes.length > 0) {
            Base64.Encoder base64 = Base64.getEncoder();
            imageBase64 = base64.encodeToString(imgIbytes);
        }
        return imageBase64;
    }

    /**
     * 获得图片BASE64
     * @param multipartFile
     * @return
     */
    public String getBase64Images(MultipartFile multipartFile) throws Exception {
        if (multipartFile == null) {
            throw new AppException("文件不能为空！");
        }

        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null) {
            throw new AppException("文件名不能为空！");
        }

        if (multipartFile.getSize() <= 0) {
            throw new AppException("不能上传空文件！");
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream ins = multipartFile.getInputStream();
        IOUtils.copy(ins, bos);
        String imageBase64 = null;
        byte[] imgBytes = bos.toByteArray();
        if (imgBytes != null && imgBytes.length > 0) {
            Base64.Encoder base64 = Base64.getEncoder();
            imageBase64 = base64.encodeToString(imgBytes);
        }
        return imageBase64;
    }
}
