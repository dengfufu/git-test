package com.zjft.usp.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码工具类
 * uaa与gateway与uas共用
 *
 * @author CK
 * @date 2019-08-01 09:36
 */
public class DefaultPasswordConfig {

    /**
     * 装配BCryptPasswordEncoder用户密码的匹配
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
//        return new MessageDigestPasswordEncoder("SHA-256");
        return new BCryptPasswordEncoder();
    }
}
