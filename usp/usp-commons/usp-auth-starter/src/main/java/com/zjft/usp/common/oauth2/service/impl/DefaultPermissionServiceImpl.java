package com.zjft.usp.common.oauth2.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.zjft.usp.common.model.LoginAppUser;
import com.zjft.usp.common.model.Right;
import com.zjft.usp.common.oauth2.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.Map;

/**
 * 请求权限判断service
 *
 * @author zgpi
 * @date 2019/11/26 16:11
 */
@Slf4j
public abstract class DefaultPermissionServiceImpl {

    @Autowired
    private SecurityProperties securityProperties;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 查询用户拥有的资源权限
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/8 10:50
     **/
    public abstract Map<String, List<Right>> mapUserRightList(Long userId, Long corpId);

    /**
     * 获得公共权限列表
     *
     * @return
     * @author zgpi
     * @date 2019/12/2 09:59
     **/
    public abstract Map<String, List<Right>> mapCommonStarRight();

    /**
     * 获得系统管理员权限
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/17 21:07
     **/
    public abstract Map<String, List<Right>> mapSysRoleRightList(Long userId, Long corpId);

    /**
     * 根据url获得权限列表
     *
     * @param url
     * @return
     * @author zgpi
     * @date 2019/12/3 15:21
     **/
    public abstract List<Right> listCommonRight(String url);

    public boolean hasPermission(Authentication authentication, ServerHttpRequest request) {
        String requestMethod = request.getMethodValue();
        String requestURI = request.getURI().getPath();
        String commonParamJson = request.getHeaders().getFirst("Common-Params");
        JSONObject jsonObject = new JSONObject(commonParamJson);
        Long corpId = jsonObject.getLong("corpId");
        // 前端跨域OPTIONS请求预检放行 也可通过前端配置代理实现
        if (HttpMethod.OPTIONS.name().equalsIgnoreCase(requestMethod)) {
            return true;
        }

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            //判断是否开启url权限验证
            if (!securityProperties.getAuth().getUrlPermission().getEnable()) {
                return true;
            }

            long start = System.currentTimeMillis();

            //判断不进行url权限认证的api，所有已登录用户都能访问的url
            for (String path : securityProperties.getAuth().getUrlPermission().getIgnoreUrls()) {
                if (antPathMatcher.match(path, requestURI)) {
                    return true;
                }
            }

            Object userObject = authentication.getPrincipal();
            LoginAppUser loginAppUser = new LoginAppUser();
            if (userObject instanceof LoginAppUser) {
                loginAppUser = (LoginAppUser) userObject;
            }

            log.info("检查用户:{}，请求url权限开始:{}", loginAppUser.getUserId(), requestURI);

            // 检查是不是系统管理员，且是系统管理员的权限
            Map<String, List<Right>> sysRoleRightListMap = this.mapSysRoleRightList(loginAppUser.getUserId(), corpId);
            if (CollectionUtil.isNotEmpty(sysRoleRightListMap)) {
                for (Map.Entry<String, List<Right>> entry : sysRoleRightListMap.entrySet()) {
                    if (StrUtil.isNotBlank(entry.getKey()) && antPathMatcher.match(entry.getKey(), requestURI)) {
                        List<Right> rightList = entry.getValue();
                        boolean isMatch = this.matchMethod(requestMethod, rightList);
                        if (isMatch) {
                            long end = System.currentTimeMillis();
                            log.info("检查用户:{}，请求url权限:{}，系统管理员权限耗时:{}毫秒", loginAppUser.getUserId(), requestURI, (end - start));
                            return true;
                        }
                    }
                }
            }

            // 检查是不是公共权限（不带通配符*）
            List<Right> commonRightList = this.listCommonRight(requestURI);
            if (CollectionUtil.isNotEmpty(commonRightList)) {
                boolean isMatch = this.matchMethod(requestMethod, commonRightList);
                if (isMatch) {
                    long end = System.currentTimeMillis();
                    log.info("检查用户:{}，请求url权限:{}，不带*公共权限耗时:{}毫秒", loginAppUser.getUserId(), requestURI, (end - start));
                    return true;
                }
            }

            // 检查是不是公共权限（带通配符*）
            Map<String, List<Right>> commonStarRightMap = this.mapCommonStarRight();
            if (CollectionUtil.isNotEmpty(commonStarRightMap)) {
                for (String url : commonStarRightMap.keySet()) {
                    if (StrUtil.isNotEmpty(url) && antPathMatcher.match(url, requestURI)) {
                        List<Right> rightList = commonStarRightMap.get(url);
                        boolean isMatch = this.matchMethod(requestMethod, rightList);
                        if (isMatch) {
                            long end = System.currentTimeMillis();
                            log.info("检查用户:{}，请求url权限:{}，带*公共权限耗时:{}毫秒", loginAppUser.getUserId(), requestURI, (end - start));
                            return true;
                        }
                    }
                }
            }

            // 检查用户权限
            Map<String, List<Right>> userRightListMap = this.mapUserRightList(loginAppUser.getUserId(), corpId);
            if (CollectionUtil.isNotEmpty(userRightListMap)) {
                for (Map.Entry<String, List<Right>> entry : userRightListMap.entrySet()) {
                    if (StrUtil.isNotBlank(entry.getKey()) && antPathMatcher.match(entry.getKey(), requestURI)) {
                        List<Right> rightList = entry.getValue();
                        boolean isMatch = this.matchMethod(requestMethod, rightList);
                        if (isMatch) {
                            long end = System.currentTimeMillis();
                            log.info("检查用户:{}，请求url权限:{}，带*用户权限耗时:{}毫秒", loginAppUser.getUserId(), requestURI, (end - start));
                            return true;
                        }
                    }
                }
            }

            long end = System.currentTimeMillis();
            log.info("检查用户:{}，请求url权限:{}，无权限耗时:{}毫秒", loginAppUser.getUserId(), requestURI, (end - start));
        }
        return false;
    }

    /**
     * 匹配请求方法
     * 若权限列表中有一个权限没有配置请求方法，则直接返回true
     * 若都有配置请求方法，则必须匹配才返回true
     *
     * @param requestMethod
     * @param rightList
     * @return
     * @author zgpi
     * @date 2019/12/3 18:48
     **/
    private boolean matchMethod(String requestMethod, List<Right> rightList) {
        boolean flag = false;
        for (Right right : rightList) {
            if (StrUtil.isBlank(right.getPathMethod())) {
                return true;
            }
            if (StrUtil.isNotBlank(right.getPathMethod())
                    && StrUtil.trimToEmpty(requestMethod).equalsIgnoreCase(StrUtil.trimToEmpty(right.getPathMethod()))) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
