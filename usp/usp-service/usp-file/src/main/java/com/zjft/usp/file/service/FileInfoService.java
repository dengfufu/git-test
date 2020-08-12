package com.zjft.usp.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.file.dto.FileDto;
import com.zjft.usp.file.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author chenxiaod
 * @description: service层
 * @date 2019/8/9 16:31
 */
public interface FileInfoService extends IService<FileInfo> {

    /**
     * description: 上传
     *
     * @param file              MultipartFile文件
     * @param isDeleteLocalFile 是否删除临时文件
     * @return FileDto
     */
    FileDto uploadFile(MultipartFile file, UserInfo userInfo, Boolean isDeleteLocalFile) throws Exception;

    /**
     * description: 根据文件名称删除文件
     *
     * @param fileId 文件id
     */
    void deleteFile(Long fileId) throws Exception;

    /**
     * description: 从fastdfs服务器下载文件
     *
     * @param fileId   文件id
     * @param response 请求响应
     */
    void downloadFile(Long fileId, HttpServletResponse response) throws IOException;

    /**
     * description: 从fastdfs服务器显示文件
     *
     * @param fileId   文件id
     * @param response 请求响应
     */
    void showFile(Long fileId, HttpServletResponse response) throws IOException;

    /**
     * description: 查询文件列表
     *
     * @param fileId 文件id
     * @return FileDto
     */
    FileDto findFile(Long fileId) throws IOException;

    /**
     * description: 上传头像图片
     *
     * @param base64Str base64图片
     * @return List<FileDto>
     */
    List<FileDto> uploadFaceImgBase64(String base64Str, UserInfo userInfo) throws Exception;

    /**
     * description: 上传头像图片
     *
     * @param file 图片文件
     * @return List<FileDto>
     */
    List<FileDto> uploadFaceImgFile(MultipartFile file,UserInfo userInfo) throws Exception;

    /**
     * 上传图片
     * @param multipartFile 图片文件
     * @param userInfo 用户信息
     * @param thumbWith 缩略图宽（0=无）
     * @param thumbHeight 缩略图高（0=无）
     * @return 第1项：原图，第2项：缩略图（可能没有）
     */
    List<FileDto> uploadImgFile(MultipartFile multipartFile, UserInfo userInfo, int thumbWith,
                                int thumbHeight) throws Exception;

    /**
     * 上传图片Base64
     * @param imgBase64 图片文件Base64
     * @param userInfo 用户信息
     * @param thumbWith 缩略图宽（0=无）
     * @param thumbHeight 缩略图高（0=无）
     * @return 第1项：原图，第2项：缩略图（可能没有）
     */
    List<FileDto> uploadImgBase64(String imgBase64, UserInfo userInfo, int thumbWith,
                                  int thumbHeight) throws Exception;

    /**
     * 上传BAE64格式图片
     *
     * @param base64Str
     * @return
     */
    Long uploadBase64Img(String base64Str);

    /**
     * description: 获取文件流
     *
     * @param fileId 文件id
     */
    ByteArrayOutputStream findFileOutputStreamByFileId(Long fileId) throws IOException;

    /**
     * 根据文件编号列表获得文件列表信息
     *
     * @param fileIdList
     * @return
     * @author zgpi
     * @date 2019/11/7 18:33
     **/
    List<FileDto> listFileDtoByIdList(List<Long> fileIdList);

    /**
     * 拷贝文件
     *
     * @param fileId
     * @return
     * @author zgpi
     * @date 2020/3/2 17:26
     */
    FileDto copyFile(Long fileId);

    /**
     * 删除文件列表
     * @param fileIdList
     */
    void deleteFileList(List<Long> fileIdList);
}
