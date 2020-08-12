package com.zjft.usp.mail.utils;

import com.zjft.usp.mail.vo.Mail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @Author : dcyu
 * @Date : 2019年8月12日
 * @Desc : 邮件发送服务工具类
 */
@Slf4j
public class MailUtil {

    /*系统账户*/
    private static final String SYSTEM_SENDER = "zijin@zjft.com";

    private JavaMailSender mailSender;

    private TemplateEngine templateEngine;

    public MailUtil(JavaMailSender mailSender, TemplateEngine templateEngine){
        this.mailSender = mailSender;
        this.templateEngine =templateEngine;
    }
    /**
     * 发送相同内容邮件给单人或多人
     * 方式为直接内容填充，传入对象设置内容Content属性
     * @param mail : 邮件实体
     * @return
     */
    public void sendMail(Mail mail){
        try {
            //创建发送消息体
            MimeMessage message = mailSender.createMimeMessage();
            boolean hasAttachments = false;
            if(mail.getAttachments() != null && mail.getAttachments().size() >= 0){
                hasAttachments = true;
            }
            MimeMessageHelper helper = new MimeMessageHelper(message, hasAttachments);
            //发件人
            helper.setFrom(mail.getSender(),mail.getSendAlias());
            //收件人
            helper.setTo(mail.getReceivers());
            //抄送
            if(mail.getCc() != null && mail.getCc().length >= 0){
                helper.setCc(mail.getCc());
            }
            //主题
            helper.setSubject(mail.getSubject());
            //文本内容
            helper.setText(mail.getContent());
            //附件
            if(hasAttachments) {
                for(File file : mail.getAttachments()){
                    helper.addAttachment(file.getName(), file);
                }
            }
            //发送邮件
            mailSender.send(message);
        }catch(MessagingException e){
            log.error("邮件发送异常" , e.getMessage());
        }catch(Exception e){
            log.error("邮件其他异常" + e.getMessage());
        }
    }

    /**
     * 发送不同内容邮件给多人
     * 方式为模板填充，传入对象设置MailType属性和Params属性
     * @param mail ：邮件实体
     * @return
     */
    public void sendBatchMail(Mail mail){
        try {
            //创建发送消息体
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            //发件人
            helper.setFrom(mail.getSender(),mail.getSendAlias());
            //收件人
            helper.setTo(mail.getReceivers());
            //抄送
            if(mail.getCc() != null && mail.getCc().length >= 0){
                helper.setCc(mail.getCc());
            }
            //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //Date date = format.parse("2019-08-06 16:20:11");
            //指定邮件发送显示时间
            //helper.setSentDate(date);
            //主题
            helper.setSubject(mail.getSubject());
            //Logo标识 TODO 图片位置的选择
            helper.addInline("logo", new File("C:\\Users\\Public\\Pictures\\Sample Pictures\\Logo.gif"));
            //文本内容
            Context context = new Context();
            //TODO 参数的传递
            context.setVariable("corp","www.zijin.com");
            context.setVariable("name", "dcyu");
            String text = templateEngine.process("mail/sampleTemplate",context);
            helper.setText(text, true);
            //附件
            if(mail.getAttachments() != null && mail.getAttachments().size() >= 0) {
                for(File file : mail.getAttachments()){
                    helper.addAttachment(file.getName(), file);
                }
            }
            //发送邮件
            mailSender.send(message);
        }catch(MessagingException e){
            log.error("邮件发送异常" , e.getMessage());
        }catch(Exception e){
            log.error("邮件其他异常" , e.getMessage());
        }
    }

    public String queryMailSendDetail(String bizId){
        try{

        }catch(Exception e){

        }
        return null;
    }

    /**
     * 检查邮件内容
     * @param mail
     * @return
     */
    public static String checkMail(Mail mail){

        return "";
    }
}
