package com.zhy.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name="user")
public class User implements Serializable , UserDetails {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "username")
    private String username;
    private String password;
    @Column(name = "user_desc")
    private String userDescript;
    @Column(name = "status")
    private int status;
    private Date createtime;
    private Date updatetime;
    @Column(name = "enabled")
    boolean enabled;//账号是否禁用

    @Transient
    boolean accountNonExpired;//是否没过期
    @Column(name = "nonLocked")
    boolean accountNonLocked;//是否没被锁定
    @Transient
    boolean credentailsNonExpired;//是否没过期
    @Transient
    public Collection<? extends GrantedAuthority> authorities;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id",updatable = false,insertable = false),
            inverseJoinColumns = @JoinColumn(name = "role_code",referencedColumnName = "rolecode",updatable = false,insertable = false))
    private List<Role> roles;

    /**
     * 用User生成JwtUser
     * @param user
     */
    public User(){
    }
    public User (User user){
        id = user.getId();
        username = user.getUsername();
        password = user.getPassword();
        authorities = Collections.singleton(new SimpleGrantedAuthority(user.getRoles().toString()));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserDescript() {
        return userDescript;
    }

    public void setUserDescript(String userDescript) {
        this.userDescript = userDescript;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String toString(){
        return "id:"+this.id+";username:"+this.username+";password:"+this.password+";descript:"+userDescript;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentailsNonExpired() {
        return credentailsNonExpired;
    }

    public void setCredentailsNonExpired(boolean credentailsNonExpired) {
        this.credentailsNonExpired = credentailsNonExpired;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}
