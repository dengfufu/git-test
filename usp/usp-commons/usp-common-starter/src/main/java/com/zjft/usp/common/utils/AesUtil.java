package com.zjft.usp.common.utils;

import java.nio.charset.Charset;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;

/**
 * AES对称加密。
 *
 * @author JFZOU
 */
public class AesUtil {

    private static String RIJNDAEL = "Rijndael";
    private static String CHARSET_FOR_NAME = "UTF-8";
    /**
     * 密钥,16Byte/128位 对称加密密钥需要双方持有
     */
    private static byte[] SECRETKEY = "_abedintflstggas".getBytes();

    /**
     *
     */
    private static Charset charset = Charset.forName(CHARSET_FOR_NAME);

    /**
     * 生成AES密钥(推荐)。
     *
     * @param len 密钥长度。Rijndael为128位。
     * @return
     */
    public static byte[] GenerateAESKey(int len) {
        try {
            KeyGenerator rijndaelKeyGenerator = KeyGenerator.getInstance(RIJNDAEL);
            rijndaelKeyGenerator.init(len);
            Key rijndaelKey = rijndaelKeyGenerator.generateKey();
            byte[] newKey = rijndaelKey.getEncoded();
            return newKey;

        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * 生成AES密钥(推荐)。
     *
     * @return
     * @throws Exception
     */
    public static String GenerateAESKey() {
        byte[] key = GenerateAESKey(128);
        String keyHex = ConvertUtil.toHexString(key);
        return keyHex;
    }

    /**
     * 加密(推荐)。
     *
     * @param originData
     * @return
     */
    public static String encrypt(String originData) {
        // 要使用UTF-8处理中文
        byte[] cprBytes = ConvertUtil.encryptAES(originData.getBytes(charset), SECRETKEY);
        // 字节数组转换成十六进制大写表示
        return ConvertUtil.toHexString(cprBytes);
    }

    /**
     * 解密(推荐)。
     *
     * @param encryptData
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(String encryptData) {
        /**十六进制表示转换为字节数组*/
        byte[] bytes = ConvertUtil.decryptAES(ConvertUtil.hexToBytes(encryptData), SECRETKEY);
        /**返回字节数组*/
        return bytes;
    }


    public static void main(String[] args) throws Exception {

        /**使用示例*/
        String originData = "{ “mobile”: “15920038441”, “sendTimestamp”: “2020-02-23 20:49:10.120”}";

        /**对称加密*/
        String encryptContent = encrypt(originData);
        System.out.println("encryptContent=" + encryptContent);

        /**对称解密*/
        String decryptContent = new String(decrypt(encryptContent), CHARSET_FOR_NAME);
        System.out.println("decryptContent=" + decryptContent);

    }
}