package com.zjft.usp.common.utils;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * AES配套使用工具类
 * @Author: JFZOU
 * @Date: 2020-02-24 17:52
 */
public class ConvertUtil {

    private ConvertUtil() {

    }

    /**
     * AES加密字节数组
     * @param txt
     * @param key
     * @return
     */
    public final static byte[] encryptAES(byte[] txt, byte[] key) {
        byte[] cpr = RijndaelUtil.encrypt(txt, key, (byte)0);
        return cpr;
    }

    /**
     * AES解密字节数组
     * @param cpr
     * @param key
     * @return
     */
    public final static byte[] decryptAES(byte[] cpr, byte[] key) {
        byte[] txt = RijndaelUtil.decrypt(cpr, key);
        return txt;
    }

    /**
     * 生成SHA1摘要
     * @param msg
     * @return
     */
    public final static byte[] toSHA1Digest(byte[] msg) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(msg);
            byte[] digest = messageDigest.digest();
            return digest;
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
    /**
     * 字节数组转换成十六进制大写表示
     * @param bytes
     * @return
     */
    public final static String toHexString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return toHexString(bytes, bytes.length);
    }
    /**
     * 字节数组转换成十六进制大写表示，指定字节数目
     * @param bytes
     * @param len
     * @return
     */
    public final static String toHexString(byte[] bytes, int len) {
        StringBuffer sb = new StringBuffer(len * 2);
        for (int i=0; i<len; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
    /**
     * 十六进制表示转换为字节数组
     * @param hex
     * @return
     */
    public final static byte[] hexToBytes(String hex) {
        int nSize = hex.length() / 2;
        if (nSize == 0) {
            return null;
        }
        byte[] txt = new byte[nSize];
        try {
            for (int i = 0; i < nSize; i++) {
                txt[i] = (byte)(Byte.parseByte(hex.substring(i*2,i*2+1), 16) * 16
                        + Byte.parseByte(hex.substring(i*2+1, i*2+2), 16));
            }
        } catch (NumberFormatException  e) {
            return null;
        }
        return txt;
    }
    /**
     * 长整数转换为字节数组
     * @param number
     * @return
     */
    public static byte[] toBytes(long number) {
        long temp = number;
        byte[] b = new byte[8];
        for (int i = b.length - 1; i > -1; i--) {
            b[i] = (byte)(temp & 0xff);
            temp >>>= 8;
        }
        return b;
    }

    /**
     * 长整数转换为字节数组
     * @param number
     * @return
     */
    public static byte[] intToBytes(int number) {
        long temp = number;
        byte[] b = new byte[4];
        for (int i = b.length - 1; i > -1; i--) {
            b[i] = (byte)(temp & 0xff);
            temp >>>= 8;
        }
        return b;
    }

    /**
     * 字节数组转换为长整数。最多转换前8个字节。
     * @param bytes
     * @return
     */
    public static long toLong(byte[] bytes) {
        long temp = 0;
        int len = bytes.length;
        if (len > 8) {
            len = 8;
        }
        for (int i = 0; i < len; i++) {
            temp <<= 8;
            int t = bytes[i];
            if (t < 0 && i != 0) {
                t = t + 256;
            }
            temp += t;
        }
        return temp;
    }

    /**
     * 字节数组异或运算
     * @param a1
     * @param pos1
     * @param a2
     * @param pos2
     * @param length
     * @return
     */
    public static byte[] arrayXor(byte[] a1, int pos1, byte[] a2, int pos2, int length) {
        byte[] b = new byte[length];
        for (int i = 0; i < length; i++) {
            b[i] = (byte)(a1[i + pos1] ^ a2[i + pos2]);
        }
        return b;
    }

    /**
     * 字节数组比较
     * @param a1
     * @param pos1
     * @param a2
     * @param pos2
     * @param length
     * @return
     */
    public static boolean arrayCompare(byte[] a1, int pos1, byte[] a2, int pos2, int length) {
        int b;
        for (int i = 0; i < length; i++) {
            b = a1[i + pos1] ^ a2[i + pos2];
            if (b != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 生成AES密钥。
     * @param len 密钥长度。Rijndael为128位。
     * @return
     */
    public static byte[] genAESKey(int len) {
        try {
            KeyGenerator rijndaelKeyGenerator = KeyGenerator.getInstance("Rijndael");
            rijndaelKeyGenerator.init(len);
            Key rijndaelKey = rijndaelKeyGenerator.generateKey();
            byte[] newKey = rijndaelKey.getEncoded();
            return newKey;

        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * 获取校验码
     * @param a1
     * @param length
     * @return
     */
    public static byte[] toMac(byte[] a1, int length) {
        byte[] b = new byte[length];
        int cnt = a1.length / length;
        System.arraycopy(a1, 0, b, 0, length);
        for (int i = 1; i < cnt; i++) {
            for (int j = 0; j < length; j++) {
                b[j] = (byte) (a1[i * length + j] ^ b[j]);
            }
        }
        return b;
    }

    /**
     * 逆转一个字节数组
     * @param a
     */
    public static void reverse(byte[] a) {
        int len = a.length;
        for (int i = 0; i < len / 2; i++) {
            byte b = a[i];
            a[i] = a[len - i - 1];
            a[len - i - 1] = b;
        }
    }

    public static void main(String[] args) {

    }
}
