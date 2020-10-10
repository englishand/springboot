package com.zhy.test.service;

import com.zhy.test.entity.Role;
import com.zhy.test.entity.User;

import java.util.List;

public interface UserService extends BaseService<User>{

    List<Role> selectByUserId(String userId);

    int update(User user);
}
