package com.zjft.usp.mail;

import com.zjft.usp.mail.utils.MailUtil;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;

/**
 * @Author : dcyu
 * @Date : 2019年8月15日
 * @Desc : 邮件配置类
 */
@EnableCaching
public class MailAutoConfigure {

    @Bean
    public MailUtil mailUtil(JavaMailSender mailSender, TemplateEngine templateEngine){
        return new MailUtil(mailSender, templateEngine);
    }

}
