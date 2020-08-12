package com.zjft.usp.auth.business.service.impl;

import com.zjft.usp.auth.business.service.LockAccountService;
import com.zjft.usp.redis.template.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 账户锁定
 *
 * @author: CK
 * @create: 2020-04-01 14:39
 */
@Slf4j
@Service
public class LockAccountServiceImpl implements LockAccountService {

    private static String DEFAULT_AUTH_ACCOUNT_LOCK_KEY = "usp_auth_account_lock_key";
    private static int DEFAULT_ACCOUNT_MAX_LOCK_NUMBER = 4; // 密码错误最大次数
    private static int DEFAULT_ACCOUNT_LOCK_EXPIRE = 7200; // 单位秒，账户锁定2小时
    private static int DEFAULT_ACCOUNT_INPUT_EXPIRE = 600; // 一天内输入错误4次

    @Autowired
    private RedisRepository redisRepository;

    @Override
    public boolean checkAccountIsLocked(String accountId) {
        int errorNumber = getErrorNumber(accountId);
        if (errorNumber >= DEFAULT_ACCOUNT_MAX_LOCK_NUMBER) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addPasswordErrorNumber(String accountId) {
        int errorNumber = getErrorNumber(accountId) + 1;
        if (errorNumber == DEFAULT_ACCOUNT_MAX_LOCK_NUMBER) {
            log.info("【{}】账号被锁定120分钟", accountId);
            redisRepository.setExpire(buildKey(accountId), errorNumber, DEFAULT_ACCOUNT_LOCK_EXPIRE);
        } else {
            log.info("【{}】账号密码输入次数+1,目前错误次数:{}", accountId, errorNumber);
            redisRepository.setExpire(buildKey(accountId), errorNumber, DEFAULT_ACCOUNT_INPUT_EXPIRE);
        }
    }

    @Override
    public void resetPasswordErrorNumber(String accountId) {
        log.info("重置账户【{}】锁定信息", accountId);
        redisRepository.del(buildKey(accountId));
    }

    private int getErrorNumber(String accountId) {
        Object errorNumber = redisRepository.get(buildKey(accountId));
        if (errorNumber != null) {
            return (int) errorNumber;
        }
        return 0;
    }

    private String buildKey(String accountId) {
        return DEFAULT_AUTH_ACCOUNT_LOCK_KEY + ":" + accountId;
    }
}
