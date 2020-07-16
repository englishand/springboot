package com.zhy.test.service.Impl;

import com.zhy.test.entity.Role;
import com.zhy.test.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    @Override
    public List<Role> selectByUserId(String userId) {
        return null;
    }


}
