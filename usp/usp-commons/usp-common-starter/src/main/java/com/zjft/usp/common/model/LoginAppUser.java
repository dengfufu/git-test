package com.zjft.usp.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;

/**
 * principal
 *
 * @author CK
 * @date 2019-08-02 14:49
 */
@Getter
@Setter
@Accessors(chain = true)
public class LoginAppUser extends UserInfo implements UserDetails {

    private static final long serialVersionUID = -3685249101751401211L;

    /**
     * 登录名: 使用mobile，来确定唯一性
     * 区别于系统的username，只用来oauth2的登录
     **/
    private String username;

    /**
     * 登录密码
     */
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new HashSet<>();
        if (!CollectionUtils.isEmpty(super.getRoles())) {
            super.getRoles().parallelStream().forEach(role -> collection.add(new SimpleGrantedAuthority(role.getRoleId())));
        }
        return collection;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
