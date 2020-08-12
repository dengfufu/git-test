package com.zjft.usp.common.utils;

import cn.hutool.core.codec.Base64;
import org.springframework.util.Base64Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author zphu
 * @version V1.0
 * @desc RSA 加密工具类
 */
public class RsaUtil {

    private static String ALGORITHM = "RSA";

    private static String CHARSET_NAME = "UTF-8";

    private static String SIGNATURE_ALGORITHM = "MD5withRSA";

    public static String PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAOCTUQn03LzKg" +
            "/sU4mccDIECwKhn9a4H9Qj2huyMnepGt1HT2QUPCXUyD4RVNR3wjowB78jYkYN" +
            "/jGSLcJcyfQrZZenQmWqLYgmMUaZ2XLmXsRukplGoyzghnwg3AkHCfheobzwoKZGWG52jTF3Fvi" +
            "/R5mrmcYthzxo7xzyfoaBbAgMBAAECgYEAnZqlT9B5LUEOuKGidew4Vj3BSS6UO0s4Ss8AR3gzxttUAegjsKop4AZ" +
            "+30cRHO7aiyU8y345OMjFlTZKkMGNAER0k9vxsnfVSbyTck9dLzltJlH0lfcsx+13S0HAXY+3u+2Fry9i+4TJ/A6" +
            "/YsKonDyMZJEv9aX5RBVX6QCmX6ECQQD3grsW4GQ08I" +
            "//U2EwYJik4k6Og4Cavh2L4ICz6lw9ibQ8gTxQrIr0CaTsbIpw3b6zsIImD2qM1Fdirt8tyBurAkEA6Ecz9GWo1gH3d46r8MQDkT" +
            "/TuHtfhnO/QADcltDmUJk9j09LuqTj" +
            "/b2QIpNAFi2zDX9s1LrbmJaVMRg9mo1eEQJBAMCvWY75qYk9Lh0LgEH1MWBLffSck2XkU4QthoSKEr0lktb3lUkIyjQpvecAqNYtCuVAqFGQ9tYkCNOeZ8hwXHkCQQCXjR2Q8GNgfpgAIc59oObS9lolnBmPmCXIlMdYf6RGIjXQM2Sturgm7nAEnx7JWbfhrqpLgj+VJ2rgRXisRmCBAkA/nO3nLJN+hoKlMhQ7kSha45ttjUG7r3RrUYibOgsFturBCUufcj1MZs0YvhqLsG0OnEEL0bIGPEdEdk+nolLb";

    public static String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDgk1EJ9Ny8yoP7FOJnHAyBAsCoZ/WuB" +
            "/UI9obsjJ3qRrdR09kFDwl1Mg+EVTUd8I6MAe" +
            "/I2JGDf4xki3CXMn0K2WXp0Jlqi2IJjFGmdly5l7EbpKZRqMs4IZ8INwJBwn4XqG88KCmRlhudo0xdxb4v0eZq5nGLYc8aO8c8n6GgWwIDAQAB";

    //public static final String PUBLIC_KEY_VALUES =
    // "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlh/Xcy/eXlDF4aYjYZw85lbZWj
    // +yh6eyVwLrFAeY7suYAPdTxOOAO5hC133AmZPNE3WQ5jq3KregXOvnPWxVZQ94L5F4mWy6r/puq42FhzL9PJ4Eo8c8Jn7cof
    // rL9LdxdgRQJ39PVqhkhJWcz6xFryNL1DWpkEpLHsMTJTF0enbT9ffBrkvM1ECKazhTk5p3z2YPFRjCRid2lYaEGhizZWf+Z6TbFzPuiAOUedw
    // +SYqFYCgd9wINAQ5OF99P8a9ZpEz8uVo0DiVNd69ynRaCjLEMkEb2fVFpdPTgPHKvvIzPthJF
    // /uUtagfpbDH0ewCG5pH8atPnDvIu7TGCG99wIDAQAB";
    //public static final String PRIVATE_KEY_VALUES =
    // "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCWH9dzL95eUMXhpiNhnDzmVtlaP7KHp7JXAusUB5juy5gA91PE44A7mELXfcCZk80TdZDmOrcqt6Bc6+c9bFVlD3gvkXiZbLqv+m6rjYWHMv08ngSjxzwmftyh//+sv0t3F2BFAnf09WqGSElZzPrEWvI0vUNamQSksewxMlMXR6dtP198GuS8zUQIprOFOTmnfPZg8VGMJGJ3aVhoQaGLNlZ/5npNsXM+6IA5R53D5JioVgKB33Ag0BDk4X30/xr1mkTPy5WjQOJU13r3KdFoKMsQyQRvZ9UWl09OA8cq+8jM+2EkX+5S1qB+lsMfR7AIbmkfxq0+cO8i7tMYIb33AgMBAAECggEAb8atiBowG1FQxL4KwfaucWJg2w1qRA7cfmSx22v8tm/ocW+1c3dgVv77Za8/5BlkLATxKjq1fpzSTd1ngxhSTLjD58cMYE/QgI6aMCljLlMMskGFetwxMQhsBkKxf1G/QqCpETjRUBYahnj0BGTWobSFejchGyeriWTgU5EAxOdvZcFhLsNBAwBayCMMGfTqVe4sSZDsqGIExX2VZDvv4rw2w8xxxFnS+rrvEmy9YobD04pH2ibsd41ixC43Ikzxf1e3HGHcP/NzZOKVrTYt2b0/TPL15xV75T1cTqw5HUU4WiHFkqSZHUq59p0JWCMmfCaQwRRLdco7rOJGPs7MQQKBgQDuxYDD8VQulUMJYkoMWhxuEAbwg8Q06AHc9V2hiQA+xRD8/YjL9+zpOkhzrJnDTbJJG4bAxxM6IQ6jc1blWPaYCwq9p1PDd2bumVEjX05v65P0wmf2QeXwWOzqyUeU8/8wi0+NoBN4RIpthf8nfCMrwbGr7d+UgTx/lwvoMmabvwKBgQCg9OHudFHTD3ZuOq025EK8NygdHfj5AE8xdt6Ej4vR9hNrmTkdyK9+jqK5xrm7aELwlE+wrUTCBWlx4lp1mnAd9E/+FNd4AkKRPyJlh0zf1vhNWCrbNvQ6GEttCdJAupLMmOrDRaAGp+PdLQ+PcErn9dVUP8oc8jnSPrvucC/LyQKBgQCU33P/t9KDEPGxQiVlOjILtdZ908pcKmQu/pWYiDEs8fY7p2DJvBVs/bf6150vlpwY1R7Zx8YHMmFMLDf729I0zn2t7kxdzW7SSam7YksnoTypGSV093WEvA1L/p+xEckE3XeGmE5LaIDe9PM4DUBooeZDipciosmzy9vStMohRQKBgDi0DKQk9m6Uk/RhV4RvBdOo2sjnNj8CjOOwLNQmfeuJ9W2CnUyQ1sKPHWS1Tdln8a0vkqN7Zy2zGmK0i4nCzpy+5S9tyDrPEQR6aXO7Jqn04aTd2rs/sOqGhIv1WczT7cOGW65PaQDvRKc2qLSvqrPtNypnDp0D+LrIvwxRGYWBAoGBAMWaH0eA1q+0YysCV3ho7c/GchnMWnUxkANCiosW8BMC6BtED79lD4um3NjR4fXqU77Zxz57G5p9fH+hdHy0Wem4t3K+LgKuP5cylTpDnENWj3dojgu9OUdhwx/7UX0i4GJqeCCxCqJqRs7GZeX7K3avKdGSaf8QgkM+75X2qu2N";
    /**
     * RSA密钥长度必须是64的倍数，在512~65536之间。默认是1024
     */
    public static final int KEY_SIZE = 2048;


    /**
     * 生成公钥、私钥对(keysize=1024)
     */
    public static KeyPairInfo getKeyPair() {
        return getKeyPair(KEY_SIZE);
    }

    /**
     * 生成公钥、私钥对
     *
     * @param keySize
     * @return
     */
    public static KeyPairInfo getKeyPair(int keySize) {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(keySize);
            // 生成一个密钥对，保存在keyPair中
            KeyPair keyPair = keyPairGen.generateKeyPair();
            // 得到私钥
            RSAPrivateKey oraprivateKey = (RSAPrivateKey) keyPair.getPrivate();
            // 得到公钥
            RSAPublicKey orapublicKey = (RSAPublicKey) keyPair.getPublic();

            KeyPairInfo pairInfo = new KeyPairInfo(keySize);
            //公钥
            byte[] publicKeybyte = orapublicKey.getEncoded();
            String publicKeyString = Base64.encode(publicKeybyte);
            pairInfo.setPublicKey(publicKeyString);
            //私钥
            byte[] privateKeybyte = oraprivateKey.getEncoded();
            String privateKeyString = Base64.encode(privateKeybyte);
            pairInfo.setPrivateKey(privateKeyString);

            return pairInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取公钥对象
     *
     * @param publicKeyBase64
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static PublicKey getPublicKey(String publicKeyBase64)
            throws Exception {

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicpkcs8KeySpec =
                new X509EncodedKeySpec(Base64.decode(publicKeyBase64));
        PublicKey publicKey = keyFactory.generatePublic(publicpkcs8KeySpec);
        return publicKey;
    }

    /**
     * 获取私钥对象
     *
     * @param privateKeyBase64
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey getPrivateKey(String privateKeyBase64)
            throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec privatekcs8KeySpec =
                new PKCS8EncodedKeySpec(Base64.decode(privateKeyBase64));
        PrivateKey privateKey = keyFactory.generatePrivate(privatekcs8KeySpec);
        return privateKey;
    }

    /**
     * 使用公钥加密
     *
     * @param content         待加密内容
     * @param publicKeyBase64 公钥 base64 编码
     * @return 经过 base64 编码后的字符串
     */
    public static String encipher(String content, String publicKeyBase64) {
        return encipher(content, publicKeyBase64, KEY_SIZE / 8 - 11);
    }

    /**
     * 使用公司钥加密（分段加密）
     *
     * @param content         待加密内容
     * @param publicKeyBase64 公钥 base64 编码
     * @param segmentSize     分段大小,一般小于 keySize/8（段小于等于0时，将不使用分段加密）
     * @return 经过 base64 编码后的字符串
     */
    public static String encipher(String content, String publicKeyBase64, int segmentSize) {
        try {
            PublicKey publicKey = getPublicKey(publicKeyBase64);
            return encipher(content, publicKey, segmentSize);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分段加密
     *
     * @param ciphertext  密文
     * @param key         加密秘钥
     * @param segmentSize 分段大小，<=0 不分段
     * @return
     */
    public static String encipher(String ciphertext, java.security.Key key, int segmentSize) {
        try {
            // 用公钥加密
            byte[] srcBytes = ciphertext.getBytes();

            // Cipher负责完成加密或解密工作，基于RSA
            Cipher cipher = Cipher.getInstance("RSA");
            // 根据公钥，对Cipher对象进行初始化
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] resultBytes = null;

            if (segmentSize > 0) {
                //分段加密
                resultBytes = cipherDoFinal(cipher, srcBytes, segmentSize);
            } else {
                resultBytes = cipher.doFinal(srcBytes);
            }

            String base64Str = Base64Utils.encodeToString(resultBytes);
            return base64Str;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分段大小
     *
     * @param cipher
     * @param srcBytes
     * @param segmentSize
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws IOException
     */
    public static byte[] cipherDoFinal(Cipher cipher, byte[] srcBytes, int segmentSize)
            throws IllegalBlockSizeException, BadPaddingException, IOException {
        if (segmentSize <= 0) {
            throw new RuntimeException("分段大小必须大于0");
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int inputLen = srcBytes.length;
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > segmentSize) {
                cache = cipher.doFinal(srcBytes, offSet, segmentSize);
            } else {
                cache = cipher.doFinal(srcBytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * segmentSize;
        }
        byte[] data = out.toByteArray();
        out.close();
        return data;
    }

    /**
     * 使用私钥解密
     *
     * @param contentBase64    待加密内容,base64 编码
     * @param privateKeyBase64 私钥 base64 编码
     * @return
     * @segmentSize 分段大小
     */
    public static String decipher(String contentBase64, String privateKeyBase64) {
        return decipher(contentBase64, privateKeyBase64, KEY_SIZE / 8);
    }

    /**
     * 使用私钥解密（分段解密）
     *
     * @param contentBase64    待加密内容,base64 编码
     * @param privateKeyBase64 私钥 base64 编码
     * @return
     * @segmentSize 分段大小
     */
    public static String decipher(String contentBase64, String privateKeyBase64, int segmentSize) {
        try {
            PrivateKey privateKey = getPrivateKey(privateKeyBase64);
            return decipher(contentBase64, privateKey, segmentSize);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分段解密
     *
     * @param contentBase64 密文
     * @param key           解密秘钥
     * @param segmentSize   分段大小（小于等于0不分段）
     * @return
     */
    public static String decipher(String contentBase64, java.security.Key key, int segmentSize) {
        try {
            // 用私钥解密
            byte[] srcBytes = Base64Utils.decodeFromString(contentBase64);
            // Cipher负责完成加密或解密工作，基于RSA
            Cipher deCipher = Cipher.getInstance("RSA");
            // 根据公钥，对Cipher对象进行初始化
            deCipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decBytes = null;
            if (segmentSize > 0) {
                //分段加密
                decBytes = cipherDoFinal(deCipher, srcBytes, segmentSize);
            } else {
                decBytes = deCipher.doFinal(srcBytes);
            }
            return new String(decBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
        return Base64.encode(signature.sign());
    }

    /**
     * 秘钥对
     */
    public static class KeyPairInfo {
        String privateKey;
        String publicKey;
        int keySize = 0;

        public KeyPairInfo(int keySize) {
            setKeySize(keySize);
        }

        public KeyPairInfo(String publicKey, String privateKey) {
            setPrivateKey(privateKey);
            setPublicKey(publicKey);
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        public int getKeySize() {
            return keySize;
        }

        public void setKeySize(int keySize) {
            this.keySize = keySize;
        }
    }
}
