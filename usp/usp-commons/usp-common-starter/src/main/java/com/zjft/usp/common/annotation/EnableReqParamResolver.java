package com.zjft.usp.common.annotation;

import com.zjft.usp.common.config.ReqParamResolverConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ReqParamResolverConfig.class)
public @interface EnableReqParamResolver {
}
