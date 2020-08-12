package com.zjft.usp.common.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * SSA加密工具
 * @author zphu
 * @Description
 * @date 2019/8/8 14:35
 * @Version 1.0
 **/
public class ShaUtil {
    public static final String KEY_SHA = "SHA";
    public static final String ra="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * 字符串sha加密
     * @param s 要加密的字符串
     * @return 加密后的字符串(长度不超过20)
     */
    public static String sha(String s)
    {
        BigInteger sha =null;
        byte[] bys = s.getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(KEY_SHA);
            messageDigest.update(bys);
            sha = new BigInteger(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String shaStr =  sha.toString(32);
        return shaStr.length() > 20 ? shaStr.substring(0,20) : shaStr;
    }

    /**
     * 字符串+盐 sha加密
     * @param s 要加密的字符串
     * @param salt 盐（user+password)
     * @return 加密后的字符串
     */
    public static String getResult(String s,String salt){
        return sha(s+salt);//加密后的密码
    }

    /**
     * 字符串+随机盐 sha加密
     * @param s 要加密的字符串
     * @return 盐和加密后的字符串
     */
    public static Map<String,String> getResult(String s){
        Map<String,String> map=new HashMap<String,String>();
        String salt=getSalt();
        map.put("salt", salt);//盐
        map.put("password", sha(s+salt));//加密后的密码
        return map;
    }

    /**
     * 生成随机盐
     * @return 随机生成的盐
     */
    private static String getSalt() {
        SecureRandom random=new SecureRandom();
        int length=random.nextInt(5)+8;//盐的长度，这里是8-12可自行调整
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = ra.charAt(random.nextInt(ra.length()));
        }
        return new String(text);
    }

}
