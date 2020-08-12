package com.zjft.usp.auth.business.service;

/**
 * @author: CK
 * @create: 2020-04-01 14:38
 */
public interface LockAccountService {

    /**
     * 检查账号是否被锁定
     *
     * @param accountId
     * @return
     */
    boolean checkAccountIsLocked(String accountId);

    /**
     * 密码错误次数加1
     *
     * @param accountId
     */
    void addPasswordErrorNumber(String accountId);

    /**
     * 重置密码错误次数
     *
     * @param accountId
     */
    void resetPasswordErrorNumber(String accountId);
}
