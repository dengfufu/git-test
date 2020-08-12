package com.zjft.usp.uas.user.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * @author zphu
 * @Descpription 用户信息类
 * @date 2019/8/6 16:22
 * @Version 1.0
 **/
@Getter
@Setter
public class UserInfoDto extends Page {


    /**
     * 用户id
     **/
    private String userId;

    /** 姓名 **/
    private String userName;

    /**
     * 用户昵称
     **/
    private String nickname;

    /**
     * 用户密码
     **/
    private String password;

    /**
     * 用户性别
     **/
    private String sex;

    /**
     * 用户头像id
     **/
    private String faceImg;

    /**
     * 用户大头像id
     *
     **/
    private String faceImgBig;

    /**
     * 登录名
     **/
    private String logonId;

    /**
     * 邮箱
     **/
    private String email;

    /** 手机号 **/
    private String mobile;

    /** 国家 **/
    private String country;

    /** 行政区划 **/
    private String district;

    /** 地区编码 **/
    private String regionCode;

    /** 地区名称 **/
    private String regionName;

    /** 省份名称 **/
    private String provinceName;

    /** 地市名称 **/
    private String cityName;

    /** 区县名称 **/
    private String districtName;

    /** 1=正常 2=失效 **/
    private String status;

    /** 个性签名 **/
    private String signature;

    /** 注册时间 **/
    private Date regTime;

    /** 是否实名 **/
    private String isUserReal;

    @NotEmpty(message = " publicKeyBase64（公钥） 不能为空")
    /** 公钥 **/
    private String publicKey;

    /** 应用客户端号**/
    private String clientId;

    /** 短信验证码**/
    private String smsCode;

    /** 服务网点名称**/
    private String serviceBranchNames;

    /** 服务网点名称**/
    boolean isExistPassword;

    /** 所属公司ID列表 **/
    private List<Long> corpIdList;

    /** 所属公司名称列表 **/
    private String corpName;

    /** 微信 openid **/
    private String openId;
}
