package com.zjft.usp.uas.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.utils.RandomUtil;
import com.zjft.usp.common.utils.RsaUtil;
import com.zjft.usp.redis.template.RedisRepository;
import com.zjft.usp.uas.user.dto.UserInfoDto;
import com.zjft.usp.uas.user.mapper.UserSafeMapper;
import com.zjft.usp.uas.user.model.UserLogonId;
import com.zjft.usp.uas.user.model.UserSafe;
import com.zjft.usp.uas.user.service.UserLogonIdService;
import com.zjft.usp.uas.user.service.UserSafeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.regex.Pattern;


/**
 * @author zphu
 * @Description
 * @date 2019/8/7 14:36
 * @Version 1.0
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class UserSafeServiceImpl extends ServiceImpl<UserSafeMapper, UserSafe> implements UserSafeService {
    @Resource
    private UserSafeMapper userSafeMapper;
    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private UserLogonIdService userLogonIdService;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public void setUserSafeInfo(UserInfoDto userInfoDto, Long userId) {
        Assert.notNull(userInfoDto, "userInfoDto 不能为空");
        Assert.notNull(userId, "userId 不能为空");

        UserSafe userSafe = new UserSafe();
        userSafe.setUserId(userId);
        if(StringUtils.isNotEmpty(userInfoDto.getLogonId())){
            UserSafe userSafe1 = this.userSafeMapper.selectOne(new QueryWrapper<UserSafe>().eq("logonid", userInfoDto.getLogonId()));
            if(userSafe1 == null && userId.equals(userSafe1.getUserId())){
                userSafe.setLogonId(userInfoDto.getLogonId());
            }else {
                throw new AppException("该登录名已经被占用，请重新输入");
            }
        }
        userSafe.setEmail(userInfoDto.getEmail());
        if(StringUtils.isNotEmpty(userInfoDto.getPassword())){
            userSafe.setPasswd(this.passwordEncoder.encode(userInfoDto.getPassword()));
        }
        this.saveOrUpdate(userSafe);
    }

    @Override
    public UserSafe getUserSafeInfo(Long userId) {
        Assert.notNull(userId, "userId 不能为空");
        UserSafe userSafe = new UserSafe();
        userSafe.setUserId(userId);
        return userSafe.selectById();
    }

    @Override
    public void setEmailAddress(String mailAddress, Long userId) {
        Assert.notNull(userId, "userId 不能为空");
        String regex = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        if (!Pattern.matches(regex, mailAddress)) {
            throw new AppException("邮箱地址格式不合法");
        }
        /*TODO 此处后续需增加 邮箱验证 验证通过 修改正式生效*/
        UserSafe userSafe = new UserSafe();
        userSafe.setUserId(userId);
        userSafe.setEmail(mailAddress);
        userSafe.updateById();
    }

    @Override
    public String getSavePasswd(String encryptedPw, String publicKey, Long userId) {
        Assert.notEmpty(encryptedPw, "encryptedPw 不能为空");
        Assert.notEmpty(publicKey, "publicKey 不能为空");
        Assert.notNull(userId, "userId 不能为空");

        Object privateKey = redisRepository.get(publicKey);
        if (privateKey == null) {
            throw new AppException("响应超时，公钥失效，请重试！");
        }
        //清除缓存，该公钥使用一次
        redisRepository.del(publicKey);
        String passwd = RsaUtil.decipher(encryptedPw, (String) privateKey);

        return this.passwordEncoder.encode(passwd);
    }

    @Override
    public boolean updateLoginId(String newLogonId, Long userId) {
        Assert.notNull(userId, "userId 不能为空");
        if (StringUtils.isNotEmpty(newLogonId)) {
            UserLogonId userLogonId = this.userLogonIdService.getById(newLogonId);
            if (userLogonId != null) {
                if(!userId.equals(userLogonId.getUserId())){
                    throw new AppException("该登录名已经被占用，请重新输入");
                }
                //用户名相同（未修改）无需更新
                return true;
            }
        }
        UserSafe userSafe = new UserSafe();
        userSafe.setUserId(userId);
        userSafe.setLogonId(newLogonId);
        return userSafe.updateById();
    }
}
