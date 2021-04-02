package com.zhy.service;

import com.zhy.entity.Role;
import com.zhy.entity.User;

import java.util.List;

public interface UserService extends BaseService<User>{

    List<Role> selectByUserId(String userId);
    User selectById(String userId);

    List<User> selectByUser(User user);

    int update(User user);

    int updateById(String userId);

    int insert(User user);
}
