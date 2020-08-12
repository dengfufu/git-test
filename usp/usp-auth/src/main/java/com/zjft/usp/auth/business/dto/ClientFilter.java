package com.zjft.usp.auth.business.dto;

import com.zjft.usp.common.model.Page;
import lombok.Getter;
import lombok.Setter;

/**
 * @author CK
 * @date 2019-09-23 13:22
 */
@Setter
@Getter
public class ClientFilter extends Page {

    private String clientId;

    private Long corpId;

}
