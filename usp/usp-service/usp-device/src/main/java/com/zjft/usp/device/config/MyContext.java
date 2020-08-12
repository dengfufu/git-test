package com.zjft.usp.device.config;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/10/17 9:14 上午
 **/
@Component
public class MyContext {

    private static final String KEY_CURRENT_TENANT_ID = "KEY_CURRENT_PROVIDER_ID";
    private static final Map<String, Object> M_CONTEXT = new ConcurrentHashMap<>();

    public void setCurrentTenantId(Long tenantId) {
        M_CONTEXT.put(KEY_CURRENT_TENANT_ID, tenantId);
    }

    public Long getCurrentTenantId() {
        return (Long) M_CONTEXT.get(KEY_CURRENT_TENANT_ID);
    }
}