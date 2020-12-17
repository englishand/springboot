package com.zhy.test.service;

import com.zhy.test.entity.Role;
import com.zhy.test.entity.User;

import java.util.List;

public interface UserService extends BaseService<User>{

    List<Role> selectByUserId(String userId);

    List<User> selectByUser(User user);

    int update(User user);

    int updateById(String userId);

    int insert(User user);
}
