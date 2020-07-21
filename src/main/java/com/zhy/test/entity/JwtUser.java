package com.zhy.test.entity;

import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 *封装用户登录相关信息，用户名/密码/权限集合，需要实现UserDetails接口。
 */
public class JwtUser implements UserDetails {

    private String id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * 用User生成JwtUser
     * @param user
     */
    public JwtUser (User user){
        id = user.getId();
        username = user.getUsername();
        password = user.getPassword();
        authorities = Collections.singleton(new SimpleGrantedAuthority(user.getRoles().toString()));
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    public String toString() {
        return "JwtUser{"+
                "id="+id+
                ",username="+username+
                ",password="+password+
                ",authorities="+authorities+
                "}";
    }
}
