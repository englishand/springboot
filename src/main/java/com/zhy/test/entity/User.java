package com.zhy.test.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="user")
public class User implements Serializable {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "user_name")
    private String username;
    private String password;
    @Column(name = "user_desc")
    private String userDescript;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",joinColumns =
    @JoinColumn(name = "user_name",referencedColumnName = "user_name",updatable = false,insertable = false),
            inverseJoinColumns = @JoinColumn(name = "role_code",referencedColumnName = "role_code",updatable = false,insertable = false))
    private List<Role> roles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public static void main(String[] args){
        Date date = new Date();

        System.out.println(date);
    }

}
