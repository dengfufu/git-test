package com.zjft.usp.gateway.authentication;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.zjft.usp.common.constant.RedisRightConstants;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.Right;
import com.zjft.usp.common.oauth2.service.impl.DefaultPermissionServiceImpl;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.gateway.service.UserRightFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限过滤
 *
 * @author CK
 * @date 2019-08-06 10:51
 */
@Slf4j
@Component
public class PermissionAuthManager extends DefaultPermissionServiceImpl implements ReactiveAuthorizationManager<AuthorizationContext> {

    /**
     * value 序列化
     */
    private static final JdkSerializationRedisSerializer OBJECT_SERIALIZER = new JdkSerializationRedisSerializer();

    @Resource
    private UserRightFeignService userRightFeignService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
        return authentication
                .flatMap(auth -> {
                    ServerHttpRequest request = authorizationContext.getExchange().getRequest();
                    boolean decision = super.hasPermission(auth, request);
                    return Mono.just(new AuthorizationDecision(decision));
                }).defaultIfEmpty(new AuthorizationDecision(false));
    }

    /**
     * 查询用户拥有的资源权限
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/8 10:50
     **/
    @Override
    public Map<String, List<Right>> mapUserRightList(Long userId, Long corpId) {
        if (this.exists(RedisRightConstants.getUserRoleKey(userId, corpId))) {
            List<Long> roleIdList = (List<Long>) this.getRedis(RedisRightConstants.getUserRoleKey(userId, corpId));
            if (CollectionUtil.isNotEmpty(roleIdList)) {
                Map<String, List<Right>> map = new HashMap<>();
                for (Long roleId : roleIdList) {
                    Map<String, List<Right>> rightListMap = (Map<String, List<Right>>) this.getRedis(RedisRightConstants.getRoleRightKey(roleId));
                    if (CollectionUtil.isNotEmpty(rightListMap)) {
                        map.putAll(rightListMap);
                    }
                }
                return map;
            }
        } else {
            // 不存在，需要重新初始化
            Result<List<Long>> roleIdListResult = userRightFeignService.initUserCorpRoleRedis(userId, corpId);
            if (Result.isSucceed(roleIdListResult)) {
                List<Long> roleIdList = roleIdListResult.getData();
                if (CollectionUtil.isNotEmpty(roleIdList)) {
                    Map<String, List<Right>> map = new HashMap<>();
                    for (Long roleId : roleIdList) {
                        Map<String, List<Right>> rightListMap = (Map<String, List<Right>>) this.getRedis(RedisRightConstants.getRoleRightKey(roleId));
                        if (CollectionUtil.isNotEmpty(rightListMap)) {
                            map.putAll(rightListMap);
                        }
                    }
                    return map;
                }
            }
        }
        return null;
    }

    /**
     * 获得公共权限列表
     *
     * @return
     * @author zgpi
     * @date 2019/12/2 09:59
     **/
    @Override
    public Map<String, List<Right>> mapCommonStarRight() {
        Object resultStr = this.getRedis(RedisRightConstants.getRightStarCommon());
        return (Map<String, List<Right>>) resultStr;
    }

    /**
     * 获得系统管理员权限
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/17 21:07
     **/
    @Override
    public Map<String, List<Right>> mapSysRoleRightList(Long userId, Long corpId) {
        List<Long> userIdList = (List<Long>) this.getRedis(RedisRightConstants.getCorpSysRoleUserKey(corpId));
        if (CollectionUtil.isEmpty(userIdList)
                || CollectionUtil.isNotEmpty(userIdList) && !userIdList.contains(userId)) {
            return null;
        }
        Map<String, List<Right>> map = new HashMap<>();
        String tenant = (String) this.getRedis(RedisRightConstants.getCorpTenantKey(corpId));
        if (StrUtil.isNotBlank(tenant)) {
            if ("Y".equalsIgnoreCase(JsonUtil.parseString(tenant, "serviceDemander"))) {
                Map<String, List<Right>> rightMap = (Map<String, List<Right>>)
                        this.getRedis(RedisRightConstants.getTenantRightKey("service_demander"));
                for (Map.Entry<String, List<Right>> entry : rightMap.entrySet()) {
                    List<Right> rightList = new ArrayList<>();
                    if (map.containsKey(entry.getKey())) {
                        rightList = map.get(entry.getKey());
                    }
                    rightList.addAll(entry.getValue());
                    map.put(entry.getKey(), rightList);
                }
            }
            if ("Y".equalsIgnoreCase(JsonUtil.parseString(tenant, "serviceProvider"))) {
                Map<String, List<Right>> rightMap = (Map<String, List<Right>>)
                        this.getRedis(RedisRightConstants.getTenantRightKey("service_provider"));
                for (Map.Entry<String, List<Right>> entry : rightMap.entrySet()) {
                    List<Right> rightList = new ArrayList<>();
                    if (map.containsKey(entry.getKey())) {
                        rightList = map.get(entry.getKey());
                    }
                    rightList.addAll(entry.getValue());
                    map.put(entry.getKey(), rightList);
                }
            }
            if ("Y".equalsIgnoreCase(JsonUtil.parseString(tenant, "deviceUser"))) {
                Map<String, List<Right>> rightMap = (Map<String, List<Right>>)
                        this.getRedis(RedisRightConstants.getTenantRightKey("device_user"));
                for (Map.Entry<String, List<Right>> entry : rightMap.entrySet()) {
                    List<Right> rightList = new ArrayList<>();
                    if (map.containsKey(entry.getKey())) {
                        rightList = map.get(entry.getKey());
                    }
                    rightList.addAll(entry.getValue());
                    map.put(entry.getKey(), rightList);
                }
            }
            if ("Y".equalsIgnoreCase(JsonUtil.parseString(tenant, "cloudManager"))) {
                Map<String, List<Right>> rightMap = (Map<String, List<Right>>)
                        this.getRedis(RedisRightConstants.getTenantRightKey("cloud_manager"));
                for (Map.Entry<String, List<Right>> entry : rightMap.entrySet()) {
                    List<Right> rightList = new ArrayList<>();
                    if (map.containsKey(entry.getKey())) {
                        rightList = map.get(entry.getKey());
                    }
                    rightList.addAll(entry.getValue());
                    map.put(entry.getKey(), rightList);
                }
            }
        }
        return map;
    }

    /**
     * 根据url获得权限列表
     *
     * @param url
     * @return
     * @author zgpi
     * @date 2019/12/3 15:21
     **/
    @Override
    public List<Right> listCommonRight(String url) {
        Object resultStr = this.getRedis(RedisRightConstants.getRightUrlCommon(url));
        return (List<Right>) resultStr;
    }

    /**
     * 根据key获取对象
     *
     * @param key the key
     * @return the string
     */
    private Object getRedis(final String key) {
        Object resultStr = redisTemplate.execute((RedisCallback<Object>) connection -> {
            RedisSerializer<String> serializer = getRedisSerializer();
            byte[] keys = serializer.serialize(key);
            byte[] values = connection.get(keys);
            return OBJECT_SERIALIZER.deserialize(values);
        });
        return resultStr;
    }

    /**
     * 判断某个主键是否存在
     *
     * @param key the key
     * @return the boolean
     */
    private boolean exists(final String key) {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.exists(key.getBytes(StandardCharsets.UTF_8)));
    }

    protected RedisSerializer<String> getRedisSerializer() {
        return redisTemplate.getStringSerializer();
    }
}
