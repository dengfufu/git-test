package com.zjft.usp.uas.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.uas.user.dto.UserInfoDto;
import com.zjft.usp.uas.user.model.UserSafe;

/**
 * 职业用户表
 *
 * @author zphu
 * @Description
 * @date 2019/8/7 14:36
 * @Version 1.0
 **/
public interface UserSafeService extends IService<UserSafe> {

    /**
     * 设置用户职业表信息
     *
     * @param userInfoDto
     * @param userId
     * @return void
     * @author zphu
     * @date 2019/9/10 11:28
     * @throws Exception
    **/
    void setUserSafeInfo(UserInfoDto userInfoDto, Long userId);

    /**
     * 获取用户信息
     * @datetime 2019/9/11 10:29
     * @version
     * @author dcyu
     * @param userId
     * @return com.zjft.usp.uas.user.model.UserSafe
     */
    UserSafe getUserSafeInfo(Long userId);


    /**
     * 设置邮箱账号
     *
     * @param mailAddress
     * @param userId
     */
    void setEmailAddress(String mailAddress, Long userId);

    /**
     * rsa解密前端发送过来的已经加密的密码，去除解密后的密码中的随机字符串
     * 而后进行sha加密，得到保存在数据库中的密码字符串
     *
     * @param encryptedPw     rsa加密后的密码
     * @param publicKeyBase64 rsa公钥
     * @param userId          用户id
     * @return java.lang.String 返回保持在数据库中的密码（最终密码)
     * @throws Exception 抛出异常
     * @author zphu
     * @date 2019/8/13 9:59
     */
    String getSavePasswd(String encryptedPw, String publicKeyBase64, Long userId);

    /**
     * 更新用户登录id
     *
     * @param newLogonId 用户新登录id
     * @param userId     用户id
     * @return boolean true=更新成功
     * @author zphu
     * @date 2019/8/13 11:30
     **/
    boolean updateLoginId(String newLogonId, Long userId);
}
