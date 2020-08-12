package com.zjft.usp.auth.business.dto;

import lombok.Data;

/**
 * @author ljzhu
 * @Package com.zjft.usp.auth.business.dto
 * @date 2020-05-22 15:35
 * @note
 */
@Data
public class WeiXinDto {

    /** 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同 **/
    private String access_token;

    /** access_token接口调用凭证超时时间 **/
    private Integer expires_in;

    /** 用户刷新access_token **/
    private String refresh_token;

    /** 用户的唯一标识 请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID **/
    private String openid;

    /** 用户授权的作用域，使用逗号（,）分隔 **/
    private String scope;

    /** 用户昵称 **/
    private String nickname;

    /** 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知 **/
    private Integer sex;

    /** 用户个人资料填写的省份 **/
    private String province;

    /** 普通用户个人资料填写的城市 **/
    private String city;

    /** 国家 **/
    private String country;

    /** 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），
     * 用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。**/
    private String headimgurl;

    /** 用户特权信息，json 数组 **/
    private String[] privilege;

    /** 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段 **/
    private String unionid;

    /** 错误码 **/
    private Integer errcode;

    /** 错误信息 **/
    private String errmsg;

}
