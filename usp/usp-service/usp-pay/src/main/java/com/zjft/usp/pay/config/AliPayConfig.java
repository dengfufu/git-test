package com.zjft.usp.pay.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: CK
 * @create: 2020-05-19 18:07
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "usp.pay.alipay")
public class AliPayConfig {

    // @formatter:off
    /** 支付宝gatewayUrl */
    private String gatewayUrl;
    /** 商户应用id */
    private String appid;

    /** RSA私钥，用于对商户请求报文加签 */
    private String appPrivateKey;
    /** 支付宝RSA公钥，用于验签支付宝应答 */
    private String alipayPublicKey;
    /** 消息内容加密秘钥*/
    private String encryptKey;

    /** 应用公钥证书路径*/
    private String appCertPath;
    /** 支付宝公钥证书路径 */
    private String alipayCertPath;
    /** 支付宝根证书路径 */
    private String alipayRootCertPath;

    /** 同步地址 */
    private String returnUrl;
    /** 异步地址 */
    private String notifyUrl;
    /** 签名类型 */
    private String signType = "RSA2";
    /** 格式 */
    private String format = "json";
    /** 编码 */
    private String charset = "UTF-8";
    /** 最大查询次数 */
    private static int maxQueryRetry = 5;
    /** 查询间隔（毫秒） */
    private static long queryDuration = 5000;
    /** 最大撤销次数 */
    private static int maxCancelRetry = 3;
    /** 撤销间隔（毫秒） */
    private static long cancelDuration = 3000;
    // @formatter:on


    @Bean
    public AlipayClient alipayClient() throws AlipayApiException {
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        certAlipayRequest.setServerUrl(gatewayUrl);
        certAlipayRequest.setAppId(appid);
        certAlipayRequest.setPrivateKey(appPrivateKey);
        certAlipayRequest.setFormat(format);
        certAlipayRequest.setCharset(charset);
        certAlipayRequest.setSignType(signType);
        certAlipayRequest.setCertPath(appCertPath);
        certAlipayRequest.setAlipayPublicCertPath(alipayCertPath);
        certAlipayRequest.setRootCertPath(alipayRootCertPath);
        certAlipayRequest.setEncryptor(encryptKey);
        certAlipayRequest.setEncryptType("AES");
//        return new DefaultAlipayClient(
//                gatewayUrl,
//                appid,
//                appPrivateKey,
//                formate,
//                charset,
//                alipayPublicKey,
//                signType);
        return  new DefaultAlipayClient(certAlipayRequest);
    }
}
