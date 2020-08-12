package com.zjft.usp.anyfix.common.feign.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 文件信息
 *
 * @author zgpi
 * @date 2019/11/07 18:32
 */
@Getter
@Setter
public class FileInfoDto {
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
}
