package com.zjft.usp.file.dto;

import lombok.Data;

import java.util.Date;

/**
 * @description: 文件dto
 * @author chenxiaod
 * @date 2019/9/2 10:05
 */
@Data
public class FileDto {
    /** 文件ID **/
    private Long fileId;
    /** 应用ID **/
    private int appId;
    /** 业务类别 **/
    private int bizType;
    /** 文件格式 **/
    private int format;
    /** 文件路径 **/
    private String path;
    /** 原文件名 **/
    private String fileName;
    /** 文件大小 **/
    private Long size;
    /** 宽 **/
    private int width;
    /** 高 **/
    private int height;
    /** 时长 **/
    private Long duration;
    /** 上传用户ID **/
    private Long addUserId;
    /** 上传时间 **/
    private Date addTime;
}
