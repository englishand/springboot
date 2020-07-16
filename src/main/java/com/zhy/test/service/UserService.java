package com.zhy.test.service;

import com.zhy.test.entity.Role;

import java.util.List;

public interface UserService {

    List<Role> selectByUserId(String userId);
}
