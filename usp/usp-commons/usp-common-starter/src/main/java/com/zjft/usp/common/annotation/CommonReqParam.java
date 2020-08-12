package com.zjft.usp.common.annotation;

import java.lang.annotation.*;

/**
 * 请求的方法参数ReqParam上添加该注解，则注入公共参数信息
 * 例：public void test(@CommonReqParam ReqParam reqParam)
 * @author zgpi
 * @version 1.0
 * @date 2019-08-15 15:35
 **/
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CommonReqParam {
}
