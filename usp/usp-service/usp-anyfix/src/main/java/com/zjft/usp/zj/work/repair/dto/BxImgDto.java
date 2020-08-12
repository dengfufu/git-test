package com.zjft.usp.zj.work.repair.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * ATM联机报修图片表
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-25 15:53
 **/
@Data
public class BxImgDto implements Serializable {
    // 交易序号
    private String tranID;
    // 顺序号，从0开始
    private int sortno;
    // 图片类别 1=接收 2=发送
    private String imgType;
    // 图片文件号
    private String fileId;
    // 图片文件类型
    private String fileType;
    // 图片查看Url
    private String picUrl;
    // 终端号
    private String deviceCode;
}
