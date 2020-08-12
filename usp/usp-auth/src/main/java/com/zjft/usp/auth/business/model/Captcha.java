package com.zjft.usp.auth.business.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 验证码
 *
 * @author: CK
 * @create: 2020-02-12 21:28
 */
@Setter
@Getter
public class Captcha implements Serializable {

    String code; // 验证码值

    long createTime; // 创建时间
}
