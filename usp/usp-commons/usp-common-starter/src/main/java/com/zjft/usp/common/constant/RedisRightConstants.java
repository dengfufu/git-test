package com.zjft.usp.common.constant;

/**
 * Redis常量
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/12/4 09:03
 */
public class RedisRightConstants {

    /**
     * 系统公共权限初始化标记
     *
     * @param
     * @return
     * @author zgpi
     * @date 2019/12/26 15:10
     **/
    public static String getRightCommonInit() {
        return "right-common-init-flag";
    }

    /**
     * 角色权限初始化标记
     *
     * @param
     * @return
     * @author zgpi
     * @date 2020/6/8 14:13
     **/
    public static String getRightRoleInit() {
        return "right-role-init-flag";
    }

    /**
     * 带*的用户权限前缀
     *
     * @param
     * @return
     * @author zgpi
     * @date 2019/12/26 16:57
     **/
    public static String getUserStarKeyPatten() {
        return "right-star-user";
    }

    /**
     * 带*的用户权限前缀
     *
     * @param
     * @return
     * @author zgpi
     * @date 2019/12/26 16:57
     **/
    public static String getUserUrlKeyPatten() {
        return "right-url-user";
    }

    /**
     * 带*的公共权限
     *
     * @param
     * @return
     * @author zgpi
     * @date 2019/12/23 08:46
     **/
    public static String getRightStarCommon() {
        return "right-star-common";
    }

    /**
     * 不带*的公共权限
     *
     * @param url 请求路径
     * @return
     * @author zgpi
     * @date 2019/12/23 08:47
     **/
    public static String getRightUrlCommon(String url) {
        return "right-url-common:" + url;
    }

    /**
     * 用户权限编号
     *
     * @param corpId 企业编号
     * @param userId 用户编号
     * @return
     * @author zgpi
     * @date 2019/12/23 08:50
     **/
    public static String getRightUserRightId(Long corpId, Long userId) {
        return "right-user-rightId:" + corpId + ":" + userId;
    }

    /**
     * 用户范围权限
     *
     * @param corpId  企业编号
     * @param userId  用户编号
     * @param rightId 权限编号
     * @return
     * @author zgpi
     * @date 2020/3/13 19:42
     **/
    public static String getRightUserScope(Long corpId, Long userId, Long rightId) {
        return "right-user-scope:" + corpId + ":" + userId + ":" + rightId;
    }

    /**
     * 角色权限key
     *
     * @param roleId 角色编号
     * @return
     * @author zgpi
     * @date 2020/5/28 20:44
     **/
    public static String getRoleRightKey(Long roleId) {
        return "right-role-rightList:" + roleId;
    }

    /**
     * 角色权限key
     *
     * @param userId 人员编号
     * @param corpId 企业编号
     * @return
     * @author zgpi
     * @date 2020/5/28 20:44
     **/
    public static String getUserRoleKey(Long userId, Long corpId) {
        return "right-user-roleIdList:" + corpId + ":" + userId;
    }

    /**
     * 企业租户类型key
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/17 16:40
     **/
    public static String getCorpTenantKey(Long corpId) {
        return "corp-tenant:" + corpId;
    }

    /**
     * 租户权限key
     *
     * @param tenant
     * @return
     * @author zgpi
     * @date 2020/6/17 16:42
     **/
    public static String getTenantRightKey(String tenant) {
        return "right-tenant:" + tenant;
    }

    /**
     * 企业系统管理员角色人员key
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/17 19:40
     **/
    public static String getCorpSysRoleUserKey(Long corpId) {
        return "corp-sys-role-user:" + corpId;
    }

    /**
     * 企业租户类型初始化标记
     *
     * @param
     * @return
     * @author zgpi
     * @date 2020/6/8 14:13
     **/
    public static String getCorpTenantInit() {
        return "tenant-init-flag";
    }

    /**
     * 企业租户权限初始化标记
     *
     * @param
     * @return
     * @author zgpi
     * @date 2020/6/8 14:13
     **/
    public static String getTenantRightInit() {
        return "tenant-right-init-flag";
    }

    /**
     * 企业系统管理员角色人员初始化标记
     *
     * @param
     * @return
     * @author zgpi
     * @date 2020/6/8 14:13
     **/
    public static String getCorpSysRoleUserInit() {
        return "corp-sys-role-user-init-flag";
    }
}
