package com.zjft.usp.common.config;

import com.zjft.usp.common.feign.service.AuthFeignService;
import com.zjft.usp.common.resolver.ClientArgumentResolver;
import com.zjft.usp.common.resolver.TokenArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 公共配置类, 一些公共工具配置
 *
 * @author CK
 * @date 2019-08-06 22:19
 */
public class LoginArgResolverConfig implements WebMvcConfigurer {

//    @Lazy
//    @Autowired
//    private UserProvider userProvider;

    @Lazy
    @Autowired
    private AuthFeignService authFeignService;

    /**
     * Token参数解析
     *
     * @param argumentResolvers 解析类
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        //注入用户信息
//        argumentResolvers.add(new TokenArgumentResolver(userProvider));
        argumentResolvers.add(new TokenArgumentResolver(null));
        //注入应用信息
        argumentResolvers.add(new ClientArgumentResolver(authFeignService));
    }
}
