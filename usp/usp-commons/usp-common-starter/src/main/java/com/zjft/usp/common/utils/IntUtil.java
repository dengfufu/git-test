package com.zjft.usp.common.utils;

/**
 * Int工具类
 *
 * @author jfzou
 * @version 1.0
 * @date 2019/11/08 3:00 下午
 **/
public class IntUtil {

    public static boolean isNotZero(Integer number){
        if(number != null && number > 0L){
            return true;
        }
        return false;
    }

    public static boolean isZero(Integer number){
        if(number == null || number == 0L){
            return true;
        }
        return false;
    }
}
