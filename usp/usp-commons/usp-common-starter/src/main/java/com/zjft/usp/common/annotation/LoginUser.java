package com.zjft.usp.common.annotation;

import java.lang.annotation.*;

/**
 * 请求的方法参数User上添加该注解，则注入当前登录人信息
 * 例1：public void test(@LoginUser UserInfo user) //只有username 和 roles
 * 例2：public void test(@LoginUser(isFull = true) UserInfo user) //能获取User对象的所有信息(会查询数据库)
 *
 * @author CK
 * @date 2019-08-06 22:22
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginUser {

    /**
     * 是否查询SysUser对象所有信息，true则通过rpc接口查询
     */
    boolean isFull() default false;
}
