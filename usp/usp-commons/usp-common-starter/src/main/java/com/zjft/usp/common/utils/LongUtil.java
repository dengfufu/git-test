package com.zjft.usp.common.utils;

/**
 * Long工具类
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/10/16 3:00 下午
 **/
public class LongUtil {

    public static boolean isNotZero(Long number){
        if(number != null && number > 0L){
            return true;
        }
        return false;
    }

    public static boolean isZero(Long number){
        if(number == null || number == 0L){
            return true;
        }
        return false;
    }
}
