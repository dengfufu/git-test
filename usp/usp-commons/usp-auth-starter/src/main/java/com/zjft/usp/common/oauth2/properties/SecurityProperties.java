package com.zjft.usp.common.oauth2.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author CK
 * @date 2019-08-05 13:35
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "usp.security")
@RefreshScope
public class SecurityProperties {

    private AuthProperties auth = new AuthProperties();

    private PermitProperties ignore = new PermitProperties();

    private ValidateCodeProperties code = new ValidateCodeProperties();

}
