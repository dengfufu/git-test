package com.zjft.usp.common.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA非对称加密管理类(首次设计用于新平台与旧平台进行非对称加密，由于时间关系，暂时不去修改RsaUtil类)
 *
 * @Author: JFZOU
 * @Date: 2020-02-24 19:29
 */
public class RsaNewUtil {

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    private static final int INIT_SIZE = 1024;

    private static String ALGORITHM = "RSA";

    private static String CHARSET_NAME = "UTF-8";

    private static String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 部署到NACOS后请删除
     */
    private static String PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAOCTUQn03LzKg/sU4mccDIECwKhn9a4H9Qj2huyMnepGt1HT2QUPCXUyD4RVNR3wjowB78jYkYN/jGSLcJcyfQrZZenQmWqLYgmMUaZ2XLmXsRukplGoyzghnwg3AkHCfheobzwoKZGWG52jTF3Fvi/R5mrmcYthzxo7xzyfoaBbAgMBAAECgYEAnZqlT9B5LUEOuKGidew4Vj3BSS6UO0s4Ss8AR3gzxttUAegjsKop4AZ+30cRHO7aiyU8y345OMjFlTZKkMGNAER0k9vxsnfVSbyTck9dLzltJlH0lfcsx+13S0HAXY+3u+2Fry9i+4TJ/A6/YsKonDyMZJEv9aX5RBVX6QCmX6ECQQD3grsW4GQ08I//U2EwYJik4k6Og4Cavh2L4ICz6lw9ibQ8gTxQrIr0CaTsbIpw3b6zsIImD2qM1Fdirt8tyBurAkEA6Ecz9GWo1gH3d46r8MQDkT/TuHtfhnO/QADcltDmUJk9j09LuqTj/b2QIpNAFi2zDX9s1LrbmJaVMRg9mo1eEQJBAMCvWY75qYk9Lh0LgEH1MWBLffSck2XkU4QthoSKEr0lktb3lUkIyjQpvecAqNYtCuVAqFGQ9tYkCNOeZ8hwXHkCQQCXjR2Q8GNgfpgAIc59oObS9lolnBmPmCXIlMdYf6RGIjXQM2Sturgm7nAEnx7JWbfhrqpLgj+VJ2rgRXisRmCBAkA/nO3nLJN+hoKlMhQ7kSha45ttjUG7r3RrUYibOgsFturBCUufcj1MZs0YvhqLsG0OnEEL0bIGPEdEdk+nolLb";
    /**
     * 部署到NACOS后请删除
     */
    private static String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDgk1EJ9Ny8yoP7FOJnHAyBAsCoZ/WuB/UI9obsjJ3qRrdR09kFDwl1Mg+EVTUd8I6MAe/I2JGDf4xki3CXMn0K2WXp0Jlqi2IJjFGmdly5l7EbpKZRqMs4IZ8INwJBwn4XqG88KCmRlhudo0xdxb4v0eZq5nGLYc8aO8c8n6GgWwIDAQAB";

    /**
     * 获取密钥对
     *
     * @return 密钥对
     */
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
        generator.initialize(INIT_SIZE);
        return generator.generateKeyPair();
    }

    /**
     * 获得默认私钥
     *
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey() throws Exception {
        return getPrivateKey(PRIVATE_KEY);
    }

    /**
     * 获取私钥
     *
     * @param privateKey 私钥字符串
     * @return
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 获得默认公钥
     *
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey() throws Exception {
        return getPublicKey(PUBLIC_KEY);
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥字符串
     * @return
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * RSA加密
     *
     * @param data      待加密数据
     * @param publicKey 公钥
     * @return
     */
    public static String encrypt(String data, PublicKey publicKey) throws Exception {

        if (publicKey == null) {
            publicKey = getPublicKey(PUBLIC_KEY);
        }

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        int inputLen = data.getBytes(CHARSET_NAME).length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        /**对数据分段加密*/
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(CHARSET_NAME), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(CHARSET_NAME), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        /** 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
        // 加密后的字符串*/
        return new String(Base64.encodeBase64String(encryptedData));
    }

    /**
     * RSA解密
     *
     * @param data       待解密数据
     * @param privateKey 私钥
     * @return
     */
    public static String decrypt(String data, PrivateKey privateKey) throws Exception {

        if (privateKey == null) {
            privateKey = getPrivateKey(PRIVATE_KEY);
        }

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] dataBytes = Base64.decodeBase64(data);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        /**对数据分段解密*/
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        /**解密后的内容*/
        return new String(decryptedData, CHARSET_NAME);
    }

    /**
     * 签名
     *
     * @param data       待签名数据
     * @param privateKey 私钥
     * @return 签名
     */
    public static String sign(String data, PrivateKey privateKey) throws Exception {

        byte[] keyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(key);
        signature.update(data.getBytes());
        return new String(Base64.encodeBase64(signature.sign()));
    }

    /**
     * 验签
     *
     * @param srcData   原始字符串
     * @param publicKey 公钥
     * @param sign      签名
     * @return 是否验签通过
     */
    public static boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception {
        byte[] keyBytes = publicKey.getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(key);
        signature.update(srcData.getBytes());
        return signature.verify(Base64.decodeBase64(sign.getBytes()));
    }

    /**
     * 单独测试本方法
     */
    public static void testRsa() {
        try {
            /**生成密钥对*/
            KeyPair keyPair = getKeyPair();

            String privateKey = new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded()));
            String publicKey = new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()));
            System.out.println("私钥:" + privateKey);
            System.out.println("公钥:" + publicKey);

            /**RSA加密*/
            String data = "待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111待加密的文字内容&+1111";

            String encryptData = encrypt(data, null);
            System.out.println("加密后内容:" + encryptData);

            /**RSA解密*/
            String decryptData = decrypt(encryptData, null);
            System.out.println("解密后内容:" + decryptData);

            /**RSA签名*/
            String sign = sign(data, getPrivateKey(privateKey));

            System.out.println("sign:" + sign);

            /**RSA验签*/
            boolean result = verify(data, getPublicKey(publicKey), sign);

            System.out.print("验签结果:" + result);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("加解密异常");
        }
    }

    /**
     * 模拟新平台访问旧平台全部过程(包括加密，解密)
     * 1、新平台使用对称加密原文
     * 2、密文再用非对称加密生成签名
     * 3、用RSA公钥验证签名
     * 4、再用对称解开密文
     * 5、解释数据
     * 6、转换对象
     * <p>
     * 2个字段，一个是密文（cipherParam），一个签名（signatureParam）这2个参数。
     * 把BODY的全部内容用对称加密算法（AES）加密成密文，然后对密文进行非对称加密算法（RSA）私钥签名。
     *
     * @throws Exception
     */
    public static void testAesAndRsa() throws Exception {

        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("startCreateDate", "2019-02-01");
        requestMap.put("endCreateDate", "2019-04-01");
        requestMap.put("mobile", "15920038441");
        requestMap.put("sendTimestamp", currentTimeMillis);

        String originData = JsonUtil.toJson(requestMap);

        /**1.使用对称加密工具类，生成第一个参数密文*/
        String aesEncryptContent = AesUtil.encrypt(originData);
        System.out.println("使用对称加密AES生成的密文:" + aesEncryptContent);

        /**2、对称加密的密文再用非对称加密算法进行签名生成第二个参数签名*/
        String rsaEncryptDataSign = sign(aesEncryptContent, getPrivateKey(PRIVATE_KEY));
        System.out.println("使用非对称加密RSA生成签名:" + rsaEncryptDataSign);

        /**3、RSA验签*/
        boolean result = verify(aesEncryptContent, getPublicKey(PUBLIC_KEY), rsaEncryptDataSign);
        System.out.println("验签名结果=" + result);

        if (result) {
            String decryptContent = new String(AesUtil.decrypt(aesEncryptContent), CHARSET_NAME);
            System.out.println("decryptContent=" + decryptContent);
        }
    }

    public static void main(String[] args) throws Exception {
        testAesAndRsa();

    }
}
