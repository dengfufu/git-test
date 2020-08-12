package com.zjft.usp.auth.business.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author CK
 * @date 2019-09-23 13:27
 */
@Setter
@Getter
public class ClientDto {

    private String clientId;

    private String clientSecret;

    private String webServerRedirectUri;

    private Long corpId;
}
