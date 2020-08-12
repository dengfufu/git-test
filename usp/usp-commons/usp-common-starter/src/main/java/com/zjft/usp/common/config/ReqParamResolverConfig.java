package com.zjft.usp.common.config;

import com.zjft.usp.common.resolver.ClientArgumentResolver;
import com.zjft.usp.common.resolver.ReqParamArgumentResolver;
import com.zjft.usp.common.resolver.TokenArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 *
 * @author zgpi
 * @version 1.0
 * @date 2019-09-04 11:17
 **/
public class ReqParamResolverConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new ReqParamArgumentResolver());
    }
}
