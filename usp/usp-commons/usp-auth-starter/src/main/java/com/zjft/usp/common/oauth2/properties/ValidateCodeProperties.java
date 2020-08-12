package com.zjft.usp.common.oauth2.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * 验证码配置
 *
 * @author CK
 * @date 2019-08-05 14:03
 */
@Setter
@Getter
public class ValidateCodeProperties {
    /**
     * 设置认证通时不需要验证码的clientId
     */
    private String[] ignoreClientCode = {};
}
