package com.zjft.usp.mail.vo;

import lombok.Data;

import java.io.File;
import java.util.List;

/**
 * @Author : dcyu
 * @Date : 2019年8月12日
 * @Desc : 邮件实体类
 */
@Data
public class Mail {
    /*发件人*/
    private String sender;
    /*发件人别名*/
    private String sendAlias;
    /*发送时间*/
    private String sendDate;
    /*收件人*/
    private String[] receivers;
    /*抄送人*/
    private String[] cc;
    /*邮件标题*/
    private String subject;

    /*方式一：直接填充*/
    /*邮件内容*/
    private String content;
    /*方式二：模板*/
    /*邮件类型*/
    private int mailType;
    /*邮件内容变量对应参数*/
    private String params;

    /*邮件附件*/
    private List<File> attachments;
}
