package com.zjft.usp.file.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.proto.storage.DownloadCallback;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.Base64ToMultipartFileUtil;
import com.zjft.usp.common.utils.ImgUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.file.config.UploadProperties;
import com.zjft.usp.file.dto.FileDto;
import com.zjft.usp.file.enums.FileTypeEnum;
import com.zjft.usp.file.mapper.FileInfoMapper;
import com.zjft.usp.file.mapper.FileTemporaryMapper;
import com.zjft.usp.file.model.FileInfo;
import com.zjft.usp.file.model.FileTemporary;
import com.zjft.usp.file.service.FileInfoService;
import com.zjft.usp.file.util.VideoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenxiaod
 * @description: 实现类
 * @date 2019/8/9 16:35
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@EnableConfigurationProperties(UploadProperties.class)
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements FileInfoService {

    @Resource
    private FileInfoMapper uasFileMapper;
    @Resource
    private FileTemporaryMapper fileTemporaryMapper;
    @Autowired
    private FastFileStorageClient storageClient;
    @Resource
    private UploadProperties properties;

    /**
     * description: 查询文件列表
     *
     * @param fileId 文件id
     * @return FileDto
     */
    @Override
    public FileDto findFile(Long fileId) {
        FileInfo fileInfo = uasFileMapper.selectById(fileId);
        FileDto fileDto = new FileDto();
        BeanUtils.copyProperties(fileInfo, fileDto);
        fileDto.setFileId(fileInfo.getFileId());
        return fileDto;
    }

    /**
     * description: 上传头像图片
     *
     * @param base64Str base64图片
     * @return List<FileDto>
     */
    @Override
    public List<FileDto> uploadFaceImgBase64(String base64Str, UserInfo userInfo) throws Exception {
        if (base64Str == null) {
            throw new AppException("大头像不能为空！");
        }
        MultipartFile files = Base64ToMultipartFileUtil.base64ToMultipart(base64Str);
        return this.uploadFaceImgFile(files, userInfo);
    }

    /**
     * description: 上传头像图片
     *
     * @param multipartFile 图片文件
     * @return List<FileDto>
     */
    @Override
    public List<FileDto> uploadFaceImgFile(MultipartFile multipartFile, UserInfo userInfo) throws Exception {
        List<FileDto> fileDtoList = new LinkedList<>();
        FileDto uasFile = this.uploadFile(multipartFile, userInfo, false);
        if (uasFile == null) {
            throw new AppException("大头像上传失败！");
        }
        fileDtoList.add(uasFile);

        if (multipartFile == null) {
            throw new AppException("图片不能为空！");
        }
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null) {
            throw new AppException("图片名不能空！");
        }
        String fileOriginalFilename = originalFilename.replaceAll(" ", "");
        String faceImgBigUrl = System.getProperty("user.dir") + properties.getUploadPathName() + fileOriginalFilename;

        // 获取缩略图的url
        String[] arr = faceImgBigUrl.split("\\.");
        String thumpImgUrl = arr[0] + "_80x80." + arr[1];
        String[] thumpImgNameArr = fileOriginalFilename.split("\\.");
        String thumpImgName = thumpImgNameArr[0] + "_80x80." + thumpImgNameArr[1];
        //压缩图片至80x80像素
        ImgUtil.resizeImage(faceImgBigUrl, thumpImgUrl, 80, 80, true);
        File file = new File(thumpImgUrl);

        FileInfo uploadFile = new FileInfo();
        try {
            uploadFile.setFileId(KeyUtil.getId());
            uploadFile.setBizType(6);
            try {
                /*BufferedImage image = ImageIO.read(files.getInputStream());*/
                BufferedImage image = ImageIO.read(file);
                //如果image=null 表示上传的不是图片格式
                if (image != null) {
                    //获取图片宽度，单位px
                    uploadFile.setWidth(image.getWidth());
                    //获取图片高度，单位px
                    uploadFile.setHeight(image.getHeight());
                }
            } catch (IOException e) {
                log.error("图片文件解析异常：{}", e);
            }
            //获取当前用户id
            if (userInfo != null) {
                uploadFile.setAddUserId(userInfo.getUserId());
            }
            uploadFile.setFileName(thumpImgName);
            //视频时长
            uploadFile.setDuration(0L);
            //截取字符串，取得文件类型
            String fileSuffix = multipartFile.getContentType();
            if (fileSuffix != null) {
                String fileType = fileSuffix.substring(0, fileSuffix.lastIndexOf("/"));
                if (fileType.equals("jpeg") || fileType.equals("image")) {
                    uploadFile.setFormat(FileTypeEnum.IMAGE.getCode());
                }
            }
            uploadFile.setAddTime(new Date());
            uploadFile.setSize(file.length());

            FileInputStream fileInputStream = null;
            //截取a.txt,b.mp4的后缀
            String subFileType = fileOriginalFilename.substring(fileOriginalFilename.indexOf(".") + 1);
            try {
                log.info("开始图片文件==============>{}", uploadFile.getFileName());
                fileInputStream = new FileInputStream(file);
                //上传
                StorePath storePath = this.storageClient.uploadFile(fileInputStream, uploadFile.getSize(), subFileType, null);
                uploadFile.setPath(storePath.getFullPath());
                //插入到数据库
                uasFileMapper.insert(uploadFile);
                log.info("图片存储在服务器的路径==============>{}", properties.getBaseUrl() + storePath.getFullPath());
            } catch (FileNotFoundException e) {
                log.error("本地图片被删除了，图片找不到=======>{}", e.getMessage());
            } finally {
                //关闭io流
                try {
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    file.delete();
                    log.info("本地文件被删除了");
                } catch (IOException e) {
                }
            }
        } catch (Exception e) {
            throw new AppException("文件上传失败：");
        }
        FileDto fileDto = new FileDto();
        BeanUtils.copyProperties(uploadFile, fileDto);
        fileDto.setFileId(uploadFile.getFileId());
        fileDtoList.add(fileDto);
        return fileDtoList;
    }


    /**
     * 上传图片
     * @param multipartFile 图片文件
     * @param userInfo 用户信息
     * @param thumbWith 缩略图宽（0=无）
     * @param thumbHeight 缩略图高（0=无）
     * @return 第1项：原图，第2项：缩略图（可能没有）
     * @throws Exception
     */
    @Override
    public List<FileDto> uploadImgFile(MultipartFile multipartFile, UserInfo userInfo, int thumbWith,
                                       int thumbHeight) throws Exception {
        if (multipartFile == null) {
            throw new AppException("图片不能为空！");
        }
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null) {
            throw new AppException("图片文件名不能空！");
        }
        List<FileDto> fileDtoList = new ArrayList<>(2);
        // 原图
        FileDto fileInfo = this.uploadFile(multipartFile, userInfo, false);
        if (fileInfo == null) {
            throw new AppException("图片上传失败！");
        }
        fileDtoList.add(fileInfo);

        if (thumbWith> 0 && thumbWith > 0 &&
                (thumbWith < fileInfo.getWidth() || thumbHeight < fileInfo.getHeight())) {
            try {
                String fileOriginalFilename = originalFilename.replaceAll(" ", "");
                String imgBigUrl = System.getProperty("user.dir") + properties.getUploadPathName() + fileOriginalFilename;
                // 获取缩略图的PATH
                String[] arr = imgBigUrl.split("\\.");
                String thumpImgUrl = new StringBuilder(arr[0]).append("_").append(thumbWith).append("x").append(thumbHeight)
                        .append(".").append(arr[1]).toString();

                String[] thumpImgNameArr = fileOriginalFilename.split("\\.");
                String thumpImgName = new StringBuilder(thumpImgNameArr[0]).append("_").append(thumbWith).append("x")
                        .append(thumbHeight).append(".").append(thumpImgNameArr[1]).toString();
                //压缩图片
                ImgUtil.resizeImage(imgBigUrl, thumpImgUrl, thumbWith, thumbHeight, false);
                File file = new File(thumpImgUrl);
                FileDto fileDto = this.uploadLocalFile(file, userInfo, thumpImgName, multipartFile.getContentType(), true);
                fileDtoList.add(fileDto);
            } catch (Exception e) {
                log.error("获取缩略图失败。", e);
            }
        }
        return fileDtoList;
    }

    /**
     * 上传图片Base64
     * @param imgBase64 图片文件Base64
     * @param userInfo 用户信息
     * @param thumbWith 缩略图宽（0=无）
     * @param thumbHeight 缩略图高（0=无）
     * @return 第1项：原图，第2项：缩略图（可能没有）
     */
    @Override
    public List<FileDto> uploadImgBase64(String imgBase64, UserInfo userInfo, int thumbWith,
                                int thumbHeight) throws Exception {
        if (StrUtil.isBlank(imgBase64)) {
            throw new AppException("图片不能为空！");
        }
        MultipartFile files = Base64ToMultipartFileUtil.base64ToMultipart(imgBase64);
        return this.uploadImgFile(files, userInfo, thumbWith, thumbHeight);
    }

    @Override
    public Long uploadBase64Img(String base64Str) {
        if (StrUtil.isBlank(base64Str)) {
            throw new AppException("图片不能为空！");
        }
        FileDto fileDto = new FileDto();
        try {
            MultipartFile files = Base64ToMultipartFileUtil.base64ToMultipart(base64Str);
            fileDto = this.uploadFile(files, null, true);
            if (fileDto == null) {
                throw new AppException("图片上传失败！");
            }
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
        return fileDto.getFileId();
    }

    @Override
    public FileDto uploadFile(MultipartFile multipartFile, UserInfo userInfo, Boolean isDeleteLocalFile) throws Exception {
        if (multipartFile == null) {
            throw new AppException("文件不能为空！");
        }
        String contentType = multipartFile.getContentType();
        File file;
        try {
            //如果没有该路径，就创建
            File filePath = new File(System.getProperty("user.dir") + properties.getUploadPathName());
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            String originalFilename = multipartFile.getOriginalFilename();
            if (originalFilename == null) {
                throw new AppException("文件名不能为空！");
            }
            originalFilename = originalFilename.replaceAll(" ", "");
            //本地文件地址
            file = new File(filePath + "/" + originalFilename);
            try {
                multipartFile.transferTo(file);
                return this.uploadLocalFile(file, userInfo,
                        originalFilename, contentType, isDeleteLocalFile);

            } catch (IOException e) {
                throw new AppException("文件上传本地失败！");
            }

        } catch (Exception e) {
            throw new AppException("文件上传失败");
        }
    }

    private FileDto uploadLocalFile(File file, UserInfo userInfo, String originalFilename, String contentType,
                                    Boolean isDeleteLocalFile) throws Exception {
        if (file == null || !file.exists()) {
            throw new AppException("文件不能为空或不存在！");
        }
        FileInfo uploadFile = new FileInfo();
        try {

            uploadFile.setFileId(KeyUtil.getId());
            uploadFile.setBizType(6);
            //截取字符串，取得文件类型
            if (contentType != null) {
                String fileType = contentType.substring(0, contentType.lastIndexOf("/"));
                if (fileType.equals("jpeg") || fileType.equals("image")) {
                    uploadFile.setFormat(FileTypeEnum.IMAGE.getCode());
                } else if (fileType.equals("video")) {
                    uploadFile.setFormat(FileTypeEnum.VIDEO.getCode());
                    uploadFile.setDuration(VideoUtil.readVideoTimeMs(file));
                } else if (fileType.equals("audio")) {
                    uploadFile.setFormat(FileTypeEnum.AUDIO.getCode());
                    uploadFile.setDuration(VideoUtil.readVideoTimeMs(file));
                } else if (fileType.equals("text")) {
                    uploadFile.setFormat(FileTypeEnum.TEXT.getCode());
                } else {
                    uploadFile.setFormat(FileTypeEnum.NONE.getCode());
                }
            } else {
                uploadFile.setFormat(FileTypeEnum.NONE.getCode());
            }
            if (uploadFile.getFormat() == FileTypeEnum.IMAGE.getCode()) {
                try {
                    /*BufferedImage image = ImageIO.read(files.getInputStream());*/
                    BufferedImage image = ImageIO.read(file);
                    //如果image=null 表示上传的不是图片格式
                    if (image != null) {
                        //获取图片宽度，单位px
                        uploadFile.setWidth(image.getWidth());
                        //获取图片高度，单位px
                        uploadFile.setHeight(image.getHeight());
                    }
                } catch (IOException e) {
                    log.error("图片文件解析异常：{}", e);
                }
            }
            //获取当前用户id
            if (userInfo != null) {
                uploadFile.setAddUserId(userInfo.getUserId());
            }
            uploadFile.setFileName(originalFilename);
            //视频时长
            uploadFile.setDuration(0L);
            uploadFile.setAddTime(DateUtil.date());
            uploadFile.setSize(file.length());

            FileInputStream fileInputStream = null;
            //截取a.txt,b.mp4的后缀
            String subFileType = originalFilename.substring(originalFilename.indexOf(".") + 1);
            try {
                log.info("开始上传文件==============>{}", uploadFile.getFileName());
                fileInputStream = new FileInputStream(file);
                //上传
                StorePath storePath = this.storageClient.uploadFile(fileInputStream, uploadFile.getSize(), subFileType, null);
                uploadFile.setPath(storePath.getFullPath());
                //插入到数据库
                uasFileMapper.insert(uploadFile);
                FileTemporary fileTemporary = new FileTemporary();
                fileTemporary.setFileId(uploadFile.getFileId());
                fileTemporary.setAddTime(DateUtil.date());
                //插入临时文件表
                fileTemporaryMapper.insert(fileTemporary);
                log.info("文件存储在服务器的路径==============>{}", properties.getBaseUrl() + storePath.getFullPath());
            } catch (FileNotFoundException e) {
                log.error("本地文件被删除了，文件找不到=======>{}", e.getMessage());
                throw e;
            } finally {
                //关闭io流
                try {
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                } catch (IOException e) {
                    log.error("文件流关闭异常：{}", e);
                }
            }
        } catch (Exception e) {
            log.error("文件上传失败：{}", e);
            throw new Exception("文件上传失败");
        }
        if (file.exists() && isDeleteLocalFile) {
            file.delete();
        }
        FileDto fileDto = new FileDto();
        BeanUtils.copyProperties(uploadFile, fileDto);
        fileDto.setFileId(uploadFile.getFileId());
        return fileDto;
    }

    /**
     * description: 根据文件名删除本地文件，根据id删除数据库数据
     *
     * @param fileId 文件id
     */
    @Override
    public void deleteFile(Long fileId) {
        if (fileId == null || fileId == 0) {
            return;
        }
        FileInfo uasFile = uasFileMapper.selectById(fileId);
        if (uasFile == null) {
            log.error("要删除的文件记录不存在。ID：" + fileId);
            return;
//            throw new AppException("文件不存在`！");
        }
        uasFileMapper.deleteById(fileId);
        try {
            this.storageClient.deleteFile(uasFile.getPath());
        } catch (Exception e) {
            log.error("要删除的文件失败。ID：" + fileId + " 路径：" + uasFile.getPath() + " 错误信息：" + e.getMessage());
        }
    }


    /**
     * description: 下载文件
     *
     * @param fileId   文件id
     * @param response 请求响应
     */
    @Override
    public void downloadFile(Long fileId, HttpServletResponse response) throws IOException {
        if (fileId == null || fileId == 0) {
            return;
        }
        FileInfo fileInfo = uasFileMapper.selectById(fileId);
        if (fileInfo == null) {
            throw new AppException("文件不存在！");
        }
        ByteArrayOutputStream byteArrayOutputStream = this.findFileOutputStreamByFileInfo(fileInfo);
        //支持中文名称，避免乱码
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;fileName=" + new String(fileInfo.getFileName().getBytes("UTF-8"), "iso-8859-1"));
        response.setCharacterEncoding("UTF-8");
        OutputStream outputStream = response.getOutputStream();

        IOUtils.copy(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), outputStream);
        outputStream.write(byteArrayOutputStream.toByteArray());
    }

    /**
     * description: 显示文件
     *
     * @param fileId   文件id
     * @param response 请求响应
     */
    @Override
    public void showFile(Long fileId, HttpServletResponse response) throws IOException {
        if (fileId == null || fileId == 0) {
            return;
        }
        //获取文件后缀，根据文件后缀获得文件的Mime类型
        FileInfo fileInfo = uasFileMapper.selectById(fileId);
        if (fileInfo == null) {
            throw new AppException("文件不存在！");
        }
        ByteArrayOutputStream byteArrayOutputStream = this.findFileOutputStreamByFileInfo(fileInfo);
        String substring = fileInfo.getFileName().substring(fileInfo.getFileName().indexOf(".") + 1);
        response.setContentType(MimeMappings.DEFAULT.get(substring));
        OutputStream outputStream = response.getOutputStream();
        IOUtils.copy(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), outputStream);
    }

    /**
     * description: 获取文件流
     *
     * @param fileId 文件id
     * @return ByteArrayOutputStream
     */
    @Override
    public ByteArrayOutputStream findFileOutputStreamByFileId(Long fileId) {
        if (fileId == null || fileId == 0) {
            return null;
        }
        FileInfo fileInfo = uasFileMapper.selectById(fileId);
        if (fileInfo == null) {
            throw new AppException("文件不存在！");
        }
        ByteArrayOutputStream byteArrayOutputStream = this.findFileOutputStreamByFileInfo(fileInfo);
        return byteArrayOutputStream;
    }

    /**
     * 根据文件编号列表获得文件列表信息
     *
     * @param fileIdList
     * @return
     * @author zgpi
     * @date 2019/11/7 18:33
     **/
    @Override
    public List<FileDto> listFileDtoByIdList(List<Long> fileIdList) {
        List<FileDto> fileDtoList = new ArrayList<>();
        if (fileIdList != null && !fileIdList.isEmpty()) {
            List<FileInfo> fileInfoList = this.list(new QueryWrapper<FileInfo>().in("fileid", fileIdList));
            FileDto fileDto;
            for (FileInfo fileInfo : fileInfoList) {
                fileDto = new FileDto();
                BeanUtils.copyProperties(fileInfo, fileDto);
                fileDtoList.add(fileDto);
            }
        }
        return fileDtoList;
    }

    /**
     * 拷贝文件
     *
     * @param fileId
     * @return
     * @author zgpi
     * @date 2020/3/2 17:26
     */
    @Override
    public FileDto copyFile(Long fileId) {
        if (LongUtil.isZero(fileId)) {
            return null;
        }
        FileDto fileDto = this.findFile(fileId);
        if (fileDto == null) {
            return null;
        }
        InputStream inputStream = null;
        FileInfo fileInfo = new FileInfo();
        try {
            BeanUtils.copyProperties(fileDto, fileInfo);
            fileInfo.setFileId(KeyUtil.getId());
            fileInfo.setAddTime(DateUtil.date());
            ByteArrayOutputStream byteArrayOutputStream = this.findFileOutputStreamByFileId(fileId);
            byte[] fileBytes = byteArrayOutputStream.toByteArray();
            inputStream = new ByteArrayInputStream(fileBytes);

            log.info("开始上传文件：{}", fileDto.getFileName());
            String subFileType = fileDto.getFileName().substring(fileDto.getFileName().indexOf(".") + 1);
            //上传
            StorePath storePath = this.storageClient.uploadFile(inputStream, fileDto.getSize(), subFileType, null);
            fileInfo.setPath(storePath.getFullPath());
            //插入到数据库
            uasFileMapper.insert(fileInfo);
            FileTemporary fileTemporary = new FileTemporary();
            fileTemporary.setFileId(fileInfo.getFileId());
            fileTemporary.setAddTime(DateUtil.date());
            //插入临时文件表
            fileTemporaryMapper.insert(fileTemporary);
            log.info("文件存储在服务器的路径：{}", properties.getBaseUrl() + storePath.getFullPath());
        } catch (Exception e) {
            log.error("拷贝文件出现异常：{}", e);
            throw e;
        } finally {
            //关闭io流
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error("文件流关闭异常：{}", e);
            }
        }
        fileDto = new FileDto();
        BeanUtils.copyProperties(fileInfo, fileDto);
        fileDto.setFileId(fileInfo.getFileId());
        return fileDto;
    }

    @Override
    public void deleteFileList(List<Long> fileIdList) {
        if (CollectionUtil.isEmpty(fileIdList)) {
            return;
        }
        List<FileInfo> fileList  = uasFileMapper.selectBatchIds(fileIdList);
        if (CollectionUtil.isEmpty(fileList)) {
            log.error("要删除的文件记录列表不存在" + fileIdList );
            return;
        }
        List<Long> idList = fileList.stream().map(FileInfo::getFileId).distinct().collect(Collectors.toList());
        uasFileMapper.deleteBatchIds(idList);
        FileInfo file = null;
        try {
            for(FileInfo fileInfo: fileList ) {
                file = fileInfo;
                this.storageClient.deleteFile(fileInfo.getPath());
            }
        } catch (Exception e) {
            log.error("要删除的文件失败。ID：" + file.getFileId() + " 路径：" + file.getPath() + " 错误信息：" + e.getMessage());
        }
    }

    /**
     * 根据文件信息获得文件字节流
     *
     * @param fileInfo
     * @return
     * @author zgpi
     * @date 2019/9/18 2:54 下午
     **/
    private ByteArrayOutputStream findFileOutputStreamByFileInfo(FileInfo fileInfo) {
        ByteArrayOutputStream byteArrayOutputStream;
        if (fileInfo == null) {
            throw new AppException("文件不存在！");
        } else {
            // 这里的filesPath需要是这种格式的：M00/00/00/wKg7g1wN2YyAAH1MAADJbaKmScw004.jpg
            String filesPath = fileInfo.getPath().substring(properties.getGroupName().length() + 1);
            byteArrayOutputStream = this.storageClient.downloadFile(properties.getGroupName(), filesPath, new DownloadCallback<ByteArrayOutputStream>() {
                @Override
                public ByteArrayOutputStream recv(InputStream ins) throws IOException {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    IOUtils.copy(ins, bos);
                    return bos;
                }
            });
        }
        return byteArrayOutputStream;
    }
}
