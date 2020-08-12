package com.zjft.usp.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: CK
 * @create: 2020-02-26 11:01
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OauthClient {

    private String clientId;

    private Long corpId; // 租户信息

    private String additionalInformation = "{}";
}
