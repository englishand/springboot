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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return username;
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

    public String toString() {
        return "JwtUser{"+
                "id="+id+
                ",username="+username+
                ",password="+password+
                ",authorities="+authorities+
                "}";
    }
}
