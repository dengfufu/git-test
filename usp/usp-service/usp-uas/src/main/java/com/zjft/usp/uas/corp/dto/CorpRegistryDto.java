package com.zjft.usp.uas.corp.dto;

import com.zjft.usp.uas.corp.model.CorpRegistry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * TODO
 *
 * @author user
 * @version 1.0
 * @date 2019-09-27 17:29
 **/
@Getter
@Setter
public class CorpRegistryDto extends CorpRegistry {

    @ApiModelProperty("完整地址")
    private String fullAddress;

    @ApiModelProperty("省市区")
    private String region;

    @ApiModelProperty("行业名称")
    private String industryName;

    @ApiModelProperty("是否认证")
    private boolean ifVerify;

    @ApiModelProperty("RSA公钥")
    private String publicKey;

    @ApiModelProperty("注册人姓名")
    private String regUserName;

    @ApiModelProperty("注册人手机")
    private String mobile;

    @ApiModelProperty("更新后的密码")
    private String newPasswd;

    @ApiModelProperty("更新本人企业")
    private boolean isMyCorp;

}
