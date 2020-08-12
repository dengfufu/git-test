package com.zjft.usp.uas.right.composite.impl;

import com.zjft.usp.uas.right.composite.RightRedisCompoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 应用启动后执行权限相关的聚合实现类
 *
 * @author zgpi
 * @date 2020/6/8 12:16
 */
@Component
public class RightStartCompoServiceImpl implements ApplicationRunner {

    @Autowired
    private RightRedisCompoService rightRedisCompoService;

    @Override
    public void run(ApplicationArguments args) {
        // 初始化系统公共权限
        rightRedisCompoService.initSysCommonRightRedis();
        // 初始化所有角色权限redis
        rightRedisCompoService.initRoleRightRedis();
        // 初始化租户类型
        rightRedisCompoService.initSysTenantRedis();
        // 初始化租户权限
        rightRedisCompoService.initTenantRightRedis();
        // 初始化系统管理员角色用户列表
        rightRedisCompoService.initCorpSysRoleUserListRedis();
    }
}
